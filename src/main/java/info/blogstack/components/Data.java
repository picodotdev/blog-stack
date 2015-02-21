package info.blogstack.components;

import info.blogstack.services.MainService;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.corelib.components.Any;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class Data {

	@Component
	private Any lastUpdated;
	
	@Inject
	private MainService service;
	
	@Environmental
	private JavaScriptSupport support;

	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("id", lastUpdated.getClientId());
		support.require("app/update").invoke("init").with(spec);
	}
	
	@Cached
	public Map<String, Object> getData() {		
		Map<String, Object> data = new HashMap<>();
		data.put("posts", service.getPostDAO().countAll());
		data.put("sources", service.getSourceDAO().countAll());
		data.put("labels", service.getLabelDAO().countAll());
		data.put("authors", service.getPostDAO().countAuthors());
		return data;
	}
}