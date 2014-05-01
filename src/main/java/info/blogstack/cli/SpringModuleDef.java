package info.blogstack.cli;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.tapestry5.internal.AbstractContributionDef;
import org.apache.tapestry5.internal.spring.CustomizingContextLoader;
import org.apache.tapestry5.internal.spring.SpringBeanServiceDef;
import org.apache.tapestry5.internal.spring.StaticObjectCreator;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.ioc.Invokable;
import org.apache.tapestry5.ioc.ModuleBuilderSource;
import org.apache.tapestry5.ioc.ObjectCreator;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.ObjectProvider;
import org.apache.tapestry5.ioc.OperationTracker;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBuilderResources;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.def.ContributionDef;
import org.apache.tapestry5.ioc.def.DecoratorDef;
import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.ioc.def.ServiceDef;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.plastic.PlasticUtils;
import org.apache.tapestry5.spring.ApplicationContextCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.web.context.ConfigurableWebApplicationContext;

/**
 * A wrapper that converts a Spring {@link ApplicationContext} into a set of service definitions,
 * compatible with Tapestry 5 IoC, for the beans defined in the context, as well as the context
 * itself.
 */
public class SpringModuleDef implements ModuleDef {

	static final String SERVICE_ID = "ApplicationContext";

	private final Map<String, ServiceDef> services = CollectionFactory.newMap();

	private final boolean compatibilityMode;

	private final AtomicBoolean applicationContextCreated = new AtomicBoolean(false);
	
	private final ApplicationContext context;

	public SpringModuleDef(String... resourceLocations) {
		compatibilityMode = true;

		final ApplicationContext externalContext = locateExternalContext(resourceLocations);
		context = externalContext;

		if (compatibilityMode)
			addServiceDefsForSpringBeans(externalContext);

		ServiceDef applicationContextServiceDef = new ServiceDef() {
			public ObjectCreator createServiceCreator(final ServiceBuilderResources resources) {
				if (compatibilityMode)
					return new StaticObjectCreator(externalContext, "externally configured Spring ApplicationContext");

				ApplicationContextCustomizer customizer = resources.getService("ApplicationContextCustomizer", ApplicationContextCustomizer.class);

				return constructObjectCreatorForApplicationContext(resources, customizer);
			}

			public String getServiceId() {
				return SERVICE_ID;
			}

			public Set<Class> getMarkers() {
				return Collections.emptySet();
			}

			public Class getServiceInterface() {
				return compatibilityMode ? externalContext.getClass() : ConfigurableWebApplicationContext.class;
			}

			public String getServiceScope() {
				return ScopeConstants.DEFAULT;
			}

			public boolean isEagerLoad() {
				return false;
			}
		};

		services.put(SERVICE_ID, applicationContextServiceDef);
	}
	
	private ApplicationContext locateExternalContext(String...resourceLocations) {
		ApplicationContext context = locateApplicationContext(resourceLocations);

		applicationContextCreated.set(true);

		return context;
	}

	/**
	 * Invoked to obtain the Spring ApplicationContext, presumably stored in the ServletContext.
	 * This method is only used in Tapestry 5.0 compatibility mode (in Tapestry 5.1 and above, the
	 * default is for Tapestry to <em>create</em> the ApplicationContext).
	 * 
	 * @param servletContext
	 *            used to locate the ApplicationContext
	 * @return the ApplicationContext itself
	 * @throws RuntimeException
	 *             if the ApplicationContext could not be located or is otherwise invalid
	 * @since 5.2.0
	 */
	protected ApplicationContext locateApplicationContext(String... resourceLocations) {
		ApplicationContext context = new GenericXmlApplicationContext(resourceLocations);

		if (context == null) {
			throw new NullPointerException("No Spring ApplicationContext");
		}

		return context;
	}

	private void addServiceDefsForSpringBeans(ApplicationContext context) {
		for (final String beanName : context.getBeanDefinitionNames()) {
			String trueName = beanName.startsWith("&") ? beanName.substring(1) : beanName;

			services.put(trueName, new SpringBeanServiceDef(trueName, context));
		}
	}

	private ObjectCreator constructObjectCreatorForApplicationContext(final ServiceBuilderResources resources, @Primary ApplicationContextCustomizer customizer) {
		final CustomizingContextLoader loader = new CustomizingContextLoader(customizer);

		final RegistryShutdownHub shutdownHub = resources.getService(RegistryShutdownHub.class);

		return new ObjectCreator() {
			public Object createObject() {
				return resources.getTracker().invoke("Creating Spring ApplicationContext via ContextLoader", new Invokable<Object>() {
					public Object invoke() {
						resources.getLogger().info(String.format("Starting Spring (version %s)", SpringVersion.getVersion()));

						applicationContextCreated.set(true);

						return context;
					}
				});
			}

			@Override
			public String toString() {
				return "ObjectCreator for Spring ApplicationContext";
			}
		};
	}

	public Class getBuilderClass() {
		return null;
	}

	/**
	 * Returns a contribution, "SpringBean", to the MasterObjectProvider service. It is ordered
	 * after the built-in contributions.
	 */
	public Set<ContributionDef> getContributionDefs() {
		ContributionDef def = createContributionToMasterObjectProvider();

		return CollectionFactory.newSet(def);
	}

	private ContributionDef createContributionToMasterObjectProvider() {

		ContributionDef def = new AbstractContributionDef() {
			public String getServiceId() {
				return "MasterObjectProvider";
			}

			@Override
			public void contribute(ModuleBuilderSource moduleSource, ServiceResources resources, OrderedConfiguration configuration) {
				final OperationTracker tracker = resources.getTracker();

				final ApplicationContext context = resources.getService(SERVICE_ID, ApplicationContext.class);

				final ObjectProvider springBeanProvider = new ObjectProvider() {
					public <T> T provide(Class<T> objectType, AnnotationProvider annotationProvider, ObjectLocator locator) {

						Map beanMap = context.getBeansOfType(objectType);

						switch (beanMap.size()) {
							case 0:
								return null;

							case 1:

								Object bean = beanMap.values().iterator().next();

								return objectType.cast(bean);

							default:

								String message = String.format("Spring context contains %d beans assignable to type %s: %s.", beanMap.size(),
										PlasticUtils.toTypeName(objectType), InternalUtils.joinSorted(beanMap.keySet()));

								throw new IllegalArgumentException(message);
						}
					}
				};

				final ObjectProvider springBeanProviderInvoker = new ObjectProvider() {
					public <T> T provide(final Class<T> objectType, final AnnotationProvider annotationProvider, final ObjectLocator locator) {
						return tracker.invoke("Resolving dependency by searching Spring ApplicationContext", new Invokable<T>() {
							public T invoke() {
								return springBeanProvider.provide(objectType, annotationProvider, locator);
							}
						});
					}
				};

				ObjectProvider outerCheck = new ObjectProvider() {
					public <T> T provide(Class<T> objectType, AnnotationProvider annotationProvider, ObjectLocator locator) {
						// I think the following line is the only reason we put the
						// SpringBeanProvider here,
						// rather than in SpringModule.

						if (!applicationContextCreated.get())
							return null;

						return springBeanProviderInvoker.provide(objectType, annotationProvider, locator);
					}
				};

				configuration.add("SpringBean", outerCheck, "after:AnnotationBasedContributions", "after:ServiceOverride");
			}
		};

		return def;
	}

	/**
	 * Returns an empty set.
	 */
	public Set<DecoratorDef> getDecoratorDefs() {
		return Collections.emptySet();
	}

	public String getLoggerName() {
		return SpringModuleDef.class.getName();
	}

	public ServiceDef getServiceDef(String serviceId) {
		return services.get(serviceId);
	}

	public Set<String> getServiceIds() {
		return services.keySet();
	}
}