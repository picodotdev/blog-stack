package info.blogstack.components;

import info.blogstack.persistence.jooq.tables.records.AdsenseRecord;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
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

	@Parameter(defaultPrefix = BindingConstants.BLOCK)
	@Property
	private Block featured;
	
	@Parameter(defaultPrefix = BindingConstants.BLOCK)
	@Property
	private Block aside1;
	
	@Parameter(defaultPrefix = BindingConstants.BLOCK)
	@Property
	private Block aside2;
	
	@Parameter(defaultPrefix = BindingConstants.BLOCK)
	@Property
	private Block aside3;
	
	@Parameter(defaultPrefix = BindingConstants.BLOCK)
	@Property
	private Block aside4;
	
	@Parameter
	@Property
	private AdsenseRecord adsense;

	@Property
	private String page;	
	
	@Inject
	ComponentResources resources;

	void setupRender() {
		page = resources.getPageName();
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
	
	public String getContentClass() {
		return (isAside()) ? "col-xs-12 col-sm-12 col-md-8 content" : "col-xs-12 col-sm-12 col-md-12 content";
	}
	
	public boolean isAside() {
		return (aside1 != null || aside2 != null || aside3 != null || aside4 != null);
	}
}