package info.blogstack.pages;

import info.blogstack.entities.Post;
import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;
import info.blogstack.services.MainService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Archive {

	private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormat.forPattern("MMMM").withLocale(Globals.LOCALE);
	
	@Property
	private Integer year;
	
	@Property
	private Integer month;
	
	@Property
	private Integer i;
	
	@Property
	private Map date;
	
	@Property
	private Post post;
	
	@Inject
	private MainService service;

	void onActivate() {		
	}
	
	void onActivate(Integer year, Integer month) {
		this.year = year;
		this.month = month;
	}
	
	Object[] onPassivate() {
		return (isGeneric()) ? null : getContext(year, month);
	}
	
	public String getTitle() {
		return (isGeneric()) ? "Archivo" : String.format("Archivo %s/%s", String.valueOf(year), StringUtils.leftPad(String.valueOf(month), 2, "0"));
	}
	
	public boolean isGeneric() {
		return (year == null || month == null);
	}

	public Map getDates() {
		List<Map> dates = service.getPostDAO().getArchive();
		TreeMap<Integer, List<Map>> m = new TreeMap<>();
		for (Map date : dates) {
			Integer year = (Integer) date.get("year");
			List<Map> yl = m.get(year);
			if (yl == null) {
				yl = new ArrayList<>();
			}
			yl.add(date);
			m.put(year, yl);
		}
		return m.descendingMap();
	}
	
	/**
	 * Método que devuelve las articulos publicadas de una fecha.
	 */
	public List<Post> getPosts() {
		if (isGeneric()) {
			return Collections.EMPTY_LIST;
		}		
		return service.getPostDAO().findAllByYearMonth(year, month);
	}
	
	public Object[] getContext(Integer year, Integer month) {
		String y = String.valueOf(year);
		String m = StringUtils.leftPad(String.valueOf(month), 2, "0");
		return new Object[] { y, m } ;
	}
	
	public Object[] getContext(Post post) {
		return Utils.getContext(post);
	}
	
	public String getMonthName(Integer month) {
		MutableDateTime date = DateTime.now().toMutableDateTime();
		date.setMonthOfYear(month);
		return MONTH_FORMATTER.print(date);
	}
}