package info.blogstack.misc;

import info.blogstack.services.MainService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppModuleRegistryWillShutdownListener implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(AppModuleRegistryWillShutdownListener.class);

	@SuppressWarnings("unused")
	private MainService service;
	
	public AppModuleRegistryWillShutdownListener(MainService service) {
		this.service = service;
	}

	@Override
	public void run() {
		try {
			logger.info("Stoping services...");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}