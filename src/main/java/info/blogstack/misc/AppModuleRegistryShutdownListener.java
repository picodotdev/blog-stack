package info.blogstack.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppModuleRegistryShutdownListener implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(AppModuleRegistryShutdownListener.class);

	public AppModuleRegistryShutdownListener() {
	}

	@Override
	public void run() {
		logger.info("Tapestry registry shutdown");
	}
}