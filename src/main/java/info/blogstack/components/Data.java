package info.blogstack.components;

import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.PostsLabelsRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;
import info.blogstack.persistence.records.AppPostRecord;
import info.blogstack.services.MainService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Any;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Data {

	private DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("EEEE, dd 'de' MMMM 'de' yyyy 'a las' HH:mm z").withLocale(Globals.LOCALE);
	private DateTimeFormatter MICRODATA_DATETIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
	
	@Parameter
	@Property
	private Object object;
	
	@Parameter(value = "false")
	private Boolean archive;
	
	@Property
	private PostRecord post;
	
	@Property
	private LabelRecord label;
	
	@Property
	private Integer i;
	
	@Inject
	private Block defaultBlock;
	
	@Inject
	private Block postBlock;
	
	@Inject
	private Block archivePostBlock;
	
	@Property
	private Block block;
	
	@Component
	private Any lastUpdated;
	
	@Inject
	private MainService service;
	
	@Environmental
	private JavaScriptSupport support;

	void beginRender() {
		if (object == null) {
			block = defaultBlock;
		} else if (object instanceof PostRecord) {
			block = (archive) ? archivePostBlock : postBlock;
			post = (PostRecord) object;
		}
	}
	
	void afterRender() {
		if (object == null) {
			JSONObject spec = new JSONObject();
			spec.put("id", lastUpdated.getClientId());
			support.require("app/update").invoke("init").with(spec);
		}
	}
	
	@Cached(watch = "object")
	public Map<String, Object> getData() {		
		Map<String, Object> data = new HashMap<>();
		data.put("posts", service.getPostDAO().countAll());
		data.put("sources", service.getSourceDAO().countAll());
		data.put("labels", service.getLabelDAO().countAll());
		data.put("authors", service.getPostDAO().countAuthors());

		return data;
	}
	
	@Cached(watch = "post")
	public Map<String, Object> getPostData() {
		AppPostRecord apost = post.into(AppPostRecord.class);
		Map<String, Object> datos = new HashMap<>();
		if (apost.getPublishdate() != null) {
			datos.put("publishDate", DATETIME_FORMATTER.print(apost.getPublishdate()));
			datos.put("microdataPublishDate", MICRODATA_DATETIME_FORMATTER.print(apost.getPublishdate()));
		}
		if (apost.getUpdatedate() != null) {
			datos.put("updateDate", DATETIME_FORMATTER.print(apost.getUpdatedate()));
			datos.put("microdataUpdateDate", MICRODATA_DATETIME_FORMATTER.print(apost.getUpdatedate()));
		}
		return datos;
	}

	@Cached(watch = "post")
	public List<LabelRecord> getLabels() {
		List<LabelRecord> labels = new ArrayList<>();		
		List<PostsLabelsRecord> pls = post.fetchChildren(Keys.POSTS_LABELS_POST_ID);
		for (PostsLabelsRecord pl : pls) {
			LabelRecord label = pl.fetchParent(Keys.POSTS_LABELS_LABEL_ID);
			labels.add(label);
		}
		return labels;
	}
	
	public Object[] getLabelContext() {
		return Utils.getContext(label);
	}
	
	public boolean isFirstLabel() {
		return i == 0;
	}
	
	public boolean isPublishDate() {
		return !isUpdateDate() && post.getPublishdate() != null;
	}
	
	public boolean isUpdateDate() {
		return post.getUpdatedate() != null;		
	}
	
	public SourceRecord getSource() {
		return post.fetchParent(Keys.POST_SOURCE_ID);
	}
}