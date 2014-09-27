package info.blogstack.services;

import info.blogstack.cli.OfflineComponentRendererImpl;
import info.blogstack.misc.AppAssetPathConverter;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.services.AssetPathConverter;
import org.lazan.t5.offline.services.OfflineComponentRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ImportModule(value = AppModule.class)
public class GenerateModule {

	private static final Logger logger = LoggerFactory.getLogger(GenerateModule.class);

	public static void bind(ServiceBinder binder) {
		binder.bind(OfflineComponentRenderer.class, OfflineComponentRendererImpl.class).withId("BlogStackOfflineComponentRenderer");
	}
	
	public static void contributeApplicationDefaults(MappedConfiguration<String, Object> config) {
		config.add("tapestry.app-name", "blogstack");
		config.add("tapestry.app-package", "info.blogstack");
		
	    // the host name of the server to which the request was sent. It is the value of the part
	    // before ":" in the Host header value, if any, or the resolved server name, or the 
	    // server IP address.
	    config.add("tapestry-offline.serverName", "www.blogstack.info");

	    //  the fully qualified name of the client or the last proxy that sent the request
	    config.add("tapestry-offline.remoteHost", "localhost");

	    // the Internet Protocol (IP) port number of the interface on which the request was received.
	    config.add("tapestry-offline.localPort", "4001");

	    // the port number to which the request was sent.
	    config.add("tapestry-offline.serverPort", "80");
	    
	    config.add(SymbolConstants.GZIP_COMPRESSION_ENABLED, false);
	}
	
	public static void contributeServiceOverride(MappedConfiguration<Class, Object> configuration) {
		configuration.addInstance(AssetPathConverter.class, AppAssetPathConverter.class);
	}
}