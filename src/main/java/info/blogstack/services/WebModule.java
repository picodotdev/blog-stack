package info.blogstack.services;

import info.blogstack.misc.Globals;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.services.Context;

@ImportModule(value = AppModule.class)
public class WebModule {

	@Startup
	public static void initApp(Context context, RegistryShutdownHub shutdownHub, MainService service) throws Exception {
		Globals.registry = (Registry)context.getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME);
	}
}
