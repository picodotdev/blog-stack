package info.blogstack.components;

import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;
import info.blogstack.persistence.records.AppPostRecord;
import info.blogstack.services.MainService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PostComponent {

	private DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("EEEE, dd 'de' MMMM 'de' yyyy").withLocale(Globals.LOCALE);
	private DateTimeFormatter MICRODATA_DATETIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
	
	enum Mode {
		HOME, POST, ARCHIVE, DEFAULT
	}
	
	private static int NUMBER_LABELS = 4;
	
	@Parameter
	@Property
	private PostRecord post;
	
	@Parameter(value = "default", defaultPrefix = BindingConstants.LITERAL)
	@Property
	private Mode mode;
	
	@Parameter(value = "false")
	@Property
	private boolean excerpt;
	
	@Parameter(value = "false")
	@Property
	private Boolean sharethis;
	
	@Property
	private LabelRecord label;
	
	@Inject
	private MainService service;
	
	@Inject
	private Block smallBlock;
	
	public Object[] getContext() {
		return Utils.getContext(post, post.fetchParent(Keys.POST_SOURCE_ID));
	}
	
	public Block getBlock() {
		switch (mode) {
			case HOME:			
			case ARCHIVE:
			case POST:
			case DEFAULT:
				return smallBlock;
			default:
				throw new IllegalArgumentException();
			
		}
	}
	
	public String getTag(String key) {
		Map<String, String> m = new HashMap<String, String>();
		m.put("h1:open", "<h1>");
		m.put("h1:close", "</h1>");
		m.put("h2:open", "<h2>");
		m.put("h2:close", "</h2>");

		String tag = null;
		switch (mode) {
			case HOME:			
			case ARCHIVE:
			case DEFAULT:
				tag = "h2";
				break;
			case POST:
				tag = "h1";
				break;
			default:
				throw new IllegalArgumentException();
			
		}
		
		String k = String.format("%s:%s", tag, key);
		return m.get(k);
	}
	
	@Cached(watch = "post")
	public List<LabelRecord> getLabels() {
		return service.getLabelDAO().findByPost(post, NUMBER_LABELS, true);
	}
	
	@Cached(watch = "post")
	public String getContentExcerpt() {
		AppPostRecord apost = post.into(AppPostRecord.class);
		return apost.getContentExcerpt();
	}
	
	@Cached(watch = "post")
	public Map<String, Object> getData() {
		AppPostRecord apost = post.into(AppPostRecord.class);
		Map<String, Object> datos = new HashMap<>();
		if (apost.getPublishdate() != null) {
			datos.put("date", DATETIME_FORMATTER.print(apost.getPublishdate()));
			datos.put("microdataDate", MICRODATA_DATETIME_FORMATTER.print(apost.getPublishdate()));
		}
		if (apost.getUpdatedate() != null) {
			datos.put("date", DATETIME_FORMATTER.print(apost.getUpdatedate()));
			datos.put("microdataDate", MICRODATA_DATETIME_FORMATTER.print(apost.getUpdatedate()));
		}
		return datos;
	}
	
	public String getTarget() {
		return (mode == Mode.POST) ? null : "_blank";
	}
	
	public SourceRecord getSource() {
		return post.fetchParent(Keys.POST_SOURCE_ID);
	}
	
	public Object[] getLabelContext() {
		return Utils.getContext(label);
	}
}