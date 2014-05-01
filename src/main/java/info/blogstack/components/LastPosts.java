package info.blogstack.components;

import info.blogstack.entities.Source;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Any;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class LastPosts {

	@Parameter
	@Property
	private Source source;
	
	@Component
	private Any lastPosts;
	
	@Environmental
	private JavaScriptSupport support;
	
	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("id", lastPosts.getClientId());
		spec.put("source", (source == null) ? "blogstack" : source.getAlias());
		spec.put("name", (source == null) ? "Blog Stack" : source.getName());
		support.require("app/lastPosts").invoke("init").with(spec);
	}
}