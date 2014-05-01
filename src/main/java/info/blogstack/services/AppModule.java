package info.blogstack.services;

import info.blogstack.misc.BlogStackStack;
import info.blogstack.misc.Globals;
import info.blogstack.services.hibernate.HibernateSessionSourceImpl;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.beanvalidator.BeanValidatorConfigurer;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class AppModule {

	private static final Logger logger = LoggerFactory.getLogger(AppModule.class);

	public static void bind(ServiceBinder binder) {
		binder.bind(IndexerService.class, IndexerServiceImpl.class);
		binder.bind(MainService.class, MainServiceImpl.class);
	}
	
	public static void contributeApplicationDefaults(MappedConfiguration<String, Object> config) {
		config.add(SymbolConstants.APPLICATION_VERSION, "0.1");
		config.add(SymbolConstants.APPLICATION_CATALOG, "/app.properties");
		config.add(SymbolConstants.SUPPORTED_LOCALES, "es");
		config.add(SymbolConstants.INCLUDE_CORE_STACK, false);
		config.add(SymbolConstants.PRODUCTION_MODE, false);
	}
	
	// Servicio que delega en Spring la inicialización de Hibernate, solo obtiene la configuración
	// de Hibernate creada por Spring
	public static HibernateSessionSource buildAppHibernateSessionSource(ApplicationContext context) {
		return new HibernateSessionSourceImpl(context);
	}

	public static void contributeServiceOverride(MappedConfiguration<Class, Object> configuration, @Local HibernateSessionSource hibernateSessionSource) {
		configuration.add(HibernateSessionSource.class, hibernateSessionSource);
	}
	
	public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration) {
		configuration.addInstance("blogstack", BlogStackStack.class);
	}
	
	public static void contributeBeanValidatorSource(OrderedConfiguration<BeanValidatorConfigurer> configuration) {
		configuration.add("AppConfigurer", new BeanValidatorConfigurer() {
			public void configure(javax.validation.Configuration<?> configuration) {
				configuration.ignoreXmlConfiguration();
			}
		});
	}

	public static GeneratorService buildPublicGeneratorService(MainService service) {
		return new GeneratorServiceImpl(service, Globals.PUBLIC, Globals.STATICS);
	}
}