package info.blogstack.services;

import info.blogstack.misc.AppConfiguration;
import info.blogstack.misc.BlogStackStack;
import info.blogstack.misc.Configuration;
import info.blogstack.misc.Globals;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.compatibility.Trait;
import org.apache.tapestry5.services.javascript.JavaScriptStack;

public class AppModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(IndexService.class, IndexServiceImpl.class);
		binder.bind(ShareService.class, ShareServiceImpl.class);
		binder.bind(MainService.class, MainServiceImpl.class);
	}

	public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
		configuration.add(SymbolConstants.APPLICATION_VERSION, "0.3");
		configuration.add(SymbolConstants.APPLICATION_CATALOG, "/app.properties");
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "es");
		configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
		configuration.add(SymbolConstants.INCLUDE_CORE_STACK, false);
		configuration.add(SymbolConstants.PRODUCTION_MODE, false);
		configuration.add(SymbolConstants.ENABLE_PAGELOADING_MASK, false);
	}

	public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration) {
		configuration.addInstance("blogstack", BlogStackStack.class);
	}
	
	public static void contributeCompatibility(MappedConfiguration<Trait, Boolean> configuration) {
		configuration.add(Trait.SCRIPTACULOUS, false);
	}

	public static Configuration<String, Object> buildConfiguration() {
		return new AppConfiguration();
	}

	public static GenerateService buildGenerateService(MainService service) {
		return new GenerateServiceImpl(service, Globals.PUBLIC, Globals.STATICS);
	}
}