package info.blogstack.components;

import info.blogstack.entities.Post;
import info.blogstack.misc.Utils;
import info.blogstack.services.MainService;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Any;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class Karmacracy {

	@Parameter
	@Property
	private Post post;
	
	@Inject
	private MainService service;
	
	@Environmental
	private JavaScriptSupport support;
	
	@Component
	private Any widget;
	
	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("selector", String.format("#%s", widget.getClientId()));
		spec.put("id", post.getId());
		spec.put("url", Utils.getUrl(service, post));
		support.require("app/karmacracy").invoke("init").with(spec);
	}
}
