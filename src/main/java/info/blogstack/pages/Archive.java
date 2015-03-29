package info.blogstack.pages;

import static info.blogstack.persistence.jooq.Tables.POST;
import info.blogstack.misc.Feed;
import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;
import info.blogstack.persistence.daos.Pagination;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;
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
	private Map<String,Object> archive;
	
	@Property
	private PostRecord post;
	
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
		return (isGeneric()) ? null : getContextDate(year, month);
	}
	
	public String getTitle() {
		return (isGeneric()) ? "Archivo" : String.format("Archivo %s/%s", String.valueOf(year), StringUtils.leftPad(String.valueOf(month), 2, "0"));
	}
	
	public boolean isGeneric() {
		return (year == null || month == null);
	}

	public Map<Integer,List<Map<String,Object>>> getDates() {
		List<Map<String,Object>> archive = service.getPostDAO().getArchiveByDates();
		TreeMap<Integer, List<Map<String,Object>>> m = new TreeMap<>();
		for (Map<String,Object> date : archive) {
			Integer year = (Integer) date.get("YEAR");
			List<Map<String,Object>> yl = m.get(year);
			if (yl == null) {
				yl = new ArrayList<>();
			}
			yl.add(date);
			m.put(year, yl);
		}
		return m.descendingMap();
	}

	public List<Map<String,Object>> getLabels() {
		return service.getPostDAO().getArchiveByLabels();
	}
	
	public List<Map<String,Object>> getSources() {
		return service.getPostDAO().getArchiveBySources();
	}
	
	public LabelRecord getLabel() {
		return (LabelRecord) archive.get("LABEL");
	}
	
	public SourceRecord getSource() {
		return (SourceRecord) archive.get("SOURCE");
	}
	
	public PostRecord getLabelPost() {
		Pagination pagination = new Pagination(0, 1, POST.DATE.desc(), POST.ID.desc());
		return service.getPostDAO().findAllByLabel(getLabel(), pagination).get(0);
	}
	
	public Feed getLabelFeed() {
		return new Feed(requestGlobals.getContextPath() + service.getGenerateService().getToRss(getLabel()).getPath(), String.format("Fuente de la etiqueta %s", getLabel().getName()));
	}
	
	public PostRecord getSourcePost() {
		Pagination pagination = new Pagination(0, 1, POST.DATE.desc(), POST.ID.desc());
		return service.getPostDAO().findAllBySource(getSource(), pagination).get(0);
	}
	
	/**
	 * Returns the posts publiched in a date.
	 */
	public List<PostRecord> getPosts() {
		if (isGeneric()) {
			return Collections.emptyList();
		}		
		return service.getPostDAO().findAllByYearMonth(year, month);
	}
	
	public Object[] getContextDate(Integer year, Integer month) {
		String y = String.valueOf(year);
		String m = StringUtils.leftPad(String.valueOf(month), 2, "0");
		return new Object[] { y, m } ;
	}
	
	public Object[] getContextPost(PostRecord post) {
		return Utils.getContext(post, post.fetchParent(Keys.POST_SOURCE_ID));
	}
	
	public Object[] getContextLabel(LabelRecord label) {
		return Utils.getContext(label);
	}
	
	public String getMonthName(Integer month) {
		MutableDateTime date = DateTime.now().toMutableDateTime();
		date.setMonthOfYear(month);
		return MONTH_FORMATTER.print(date);
	}
}