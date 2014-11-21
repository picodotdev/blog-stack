package info.blogstack.services;

import info.blogstack.misc.AppConfiguration;
import info.blogstack.misc.BlogStackStack;
import info.blogstack.misc.Configuration;
import info.blogstack.misc.Globals;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.javascript.JavaScriptStack;

public class AppModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(IndexService.class, IndexServiceImpl.class);
		binder.bind(ShareService.class, ShareServiceImpl.class);
		binder.bind(MainService.class, MainServiceImpl.class);
	}
	
	public static void contributeApplicationDefaults(MappedConfiguration<String, Object> config) {
		config.add(SymbolConstants.APPLICATION_VERSION, "0.2");
		config.add(SymbolConstants.APPLICATION_CATALOG, "/app.properties");
		config.add(SymbolConstants.SUPPORTED_LOCALES, "es");
		config.add(SymbolConstants.INCLUDE_CORE_STACK, false);
		config.add(SymbolConstants.PRODUCTION_MODE, false);
	}
	
	public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration) {
		configuration.addInstance("blogstack", BlogStackStack.class);
	}
	
	public static Configuration<String, Object> buildConfiguration() {
		return new AppConfiguration();
	}

	public static GenerateService buildGenerateService(MainService service) {
		return new GenerateServiceImpl(service, Globals.PUBLIC, Globals.STATICS);
	}
}