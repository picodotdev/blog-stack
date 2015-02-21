package info.blogstack.components;

import info.blogstack.persistence.jooq.tables.records.AdsenseRecord;

import java.io.File;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.joda.time.DateTime;

// No debería hacer falta incluir los módulos aquí, los debería incluir el stack
@Import(stack = "blogstack", module = "app/analytics")
public class Layout {

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property(read = false)
	private String title;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property(read = false)
	private String subtitle;
	
	@Parameter
	@Property
	private AdsenseRecord adsense;

	@Property
	private String page;	
	
	@Inject
	ComponentResources resources;
	
	@Environmental
	private JavaScriptSupport support;

	void setupRender() {
		page = resources.getPageName();
	}
	
	void afterRender() {
		File backgoundsDirectory = new File("src/main/webapp/images/backgrounds");
		String[] backgrounds = backgoundsDirectory.list();
		
		JSONObject spec = new JSONObject();
		spec.put("backgrounds", new JSONArray((Object[]) backgrounds));

		support.require("app/background").invoke("init").with(spec);
	}
	
	public boolean isHome() {
		return resources.getPageName().equals("Index");
	}
	
	public int getYear() {
		return DateTime.now().getYear();
	}
	
	public String getTitle() {
		if (title == null) {
			return String.format("%s", getSubtitle());			
		} else {
			return String.format("%s | %s", title, getSubtitle());
		}
	}
	
	public String getSubtitle() {
		return (subtitle == null) ? "Blog Stack" : subtitle; 
	}
}