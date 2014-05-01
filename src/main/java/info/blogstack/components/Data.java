package info.blogstack.components;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;
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
	
	@Property
	private Post post;
	
	@Property
	private Label label;
	
	@Property
	private Integer i;
	
	@Inject
	private Block defaultBlock;
	
	@Inject
	private Block postBlock;
	
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
		} else if (object instanceof info.blogstack.entities.Post) {
			block = postBlock;
			post = (info.blogstack.entities.Post) object;
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
		Map<String, Object> datos = new HashMap<>();
		if (post.getPublishDate() != null) {
			datos.put("publishDate", DATETIME_FORMATTER.print(post.getPublishDate()));
			datos.put("microdataPublishDate", MICRODATA_DATETIME_FORMATTER.print(post.getPublishDate()));
		}
		if (post.getUpdateDate() != null) {
			datos.put("updateDate", DATETIME_FORMATTER.print(post.getUpdateDate()));
			datos.put("microdataUpdateDate", MICRODATA_DATETIME_FORMATTER.print(post.getUpdateDate()));
		}
		return datos;
	}
	
	public String getLabels() {
		List<String> labels = new ArrayList<>();
		for (Label label : post.getLabels()) {
			labels.add(label.getName());
		}
		return StringUtils.join(labels, ", ");
	}
	
	public Object[] getLabelContext() {
		return Utils.getContext(label);
	}
	
	public boolean isFirstLabel() {
		return i == 0;
	}
}