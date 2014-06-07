package info.blogstack.pages;

import info.blogstack.entities.Post;
import info.blogstack.entities.Source;
import info.blogstack.misc.Feed;
import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;
import info.blogstack.services.MainService;
import info.blogstack.services.dao.Direction;
import info.blogstack.services.dao.Pagination;
import info.blogstack.services.dao.Sort;

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
import org.lazan.t5.offline.services.OfflineRequestGlobals;

public class Archive {

	private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormat.forPattern("MMMM").withLocale(Globals.LOCALE);
	
	@Property
	private Integer year;
	
	@Property
	private Integer month;
	
	@Property
	private Integer i;
	
	@Property
	private Map archive;
	
	@Property
	private Post post;
	
	@Inject
	private MainService service;
	
	@Inject
	private OfflineRequestGlobals requestGlobals;

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
		List<Map> archive = service.getPostDAO().getArchiveByDates();
		TreeMap<Integer, List<Map>> m = new TreeMap<>();
		for (Map date : archive) {
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

	public List<Map> getLabels() {
		return service.getPostDAO().getArchiveByLabels();
	}
	
	public List<Map> getSources() {
		return service.getPostDAO().getArchiveBySources();
	}
	
	public info.blogstack.entities.Label getLabel() {
		return (info.blogstack.entities.Label) archive.get("label");
	}
	
	public Source getSource() {
		return (Source) archive.get("source");
	}
	
	public Post getLabelPost() {
		List<Sort> sort = new ArrayList<>();
		sort.add(new Sort("date", Direction.DESCENDING));
		Pagination pagination = new Pagination(0, 1, sort);
		return service.getPostDAO().findAllByLabel(getLabel(), pagination).get(0);
	}
	
	public Feed getLabelFeed() {
		return new Feed(requestGlobals.getContextPath() + service.getPublicGeneratorService().getToRss(getLabel()).getPath(), String.format("Fuente de la etiqueta %s", getLabel().getName()));
	}
	
	public Post getSourcePost() {
		List<Sort> sort = new ArrayList<>();
		sort.add(new Sort("date", Direction.DESCENDING));
		Pagination pagination = new Pagination(0, 1, sort);
		return service.getPostDAO().findAllBySource(getSource(), pagination).get(0);
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