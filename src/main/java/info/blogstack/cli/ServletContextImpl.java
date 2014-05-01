package info.blogstack.cli;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

public class ServletContextImpl implements ServletContext {

	private String contextPath;
	private String contextRoot;
	private Map<String, String> initParameter;
	private Map<String, Object> attributes;

	public ServletContextImpl(String contextPath, String contextRoot) {
		this.contextPath = contextPath;
		this.contextRoot = contextRoot;
		this.initParameter = new HashMap<>();
		this.attributes = new HashMap<>();
	}

	@Override
	public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
	}

	@Override
	public boolean setInitParameter(String name, String value) {
		initParameter.put(name, value);
		return true;
	}

	@Override
	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public void log(String message, Throwable throwable) {
	}

	@Override
	public void log(Exception exception, String msg) {
	}

	@Override
	public void log(String msg) {
	}

	@Override
	public String getVirtualServerName() {
		return null;
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		return null;
	}

	@Override
	public Enumeration<Servlet> getServlets() {
		return null;
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return null;
	}

	@Override
	public ServletRegistration getServletRegistration(String servletName) {
		return null;
	}

	@Override
	public Enumeration<String> getServletNames() {
		return null;
	}

	@Override
	public String getServletContextName() {
		return "blogstack";
	}

	@Override
	public Servlet getServlet(String name) throws ServletException {
		return null;
	}

	@Override
	public String getServerInfo() {
		return "Blog Stack Generator/1.0";
	}

	@Override
	public Set<String> getResourcePaths(String path) {
		File f = new File(contextRoot, path);		
		return new HashSet(Arrays.asList(f.list()));
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		try {
			return getResource(path).openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		File f = new File(contextRoot, path);
		if (!f.exists()) {
			return null;
		}
		return f.toURI().toURL();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return null;
	}

	@Override
	public String getRealPath(String path) {
		File f = new File(contextRoot, path);
		if (!f.exists()) {
			return null;
		}
		return f.getAbsolutePath();
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		return null;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public String getMimeType(String file) {
		try {
			return Files.probeContentType(new File(file).toPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		return null;
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(initParameter.keySet());
	}

	@Override
	public String getInitParameter(String name) {
		return initParameter.get(name);
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return null;
	}

	@Override
	public FilterRegistration getFilterRegistration(String filterName) {
		return null;
	}

	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		return new HashSet<SessionTrackingMode>(Arrays.asList(SessionTrackingMode.COOKIE));
	}

	@Override
	public int getEffectiveMinorVersion() {
		return 0;
	}

	@Override
	public int getEffectiveMajorVersion() {
		return 1;
	}

	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		return new HashSet<SessionTrackingMode>(Arrays.asList(SessionTrackingMode.COOKIE));
	}

	@Override
	public String getContextPath() {
		return contextPath;
	}

	@Override
	public ServletContext getContext(String uripath) {
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		return getClassLoader();
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return Collections.enumeration(attributes.keySet());
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public void declareRoles(String... roleNames) {
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
		return null;
	}

	@Override
	public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
		return null;
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
		return null;
	}

	@Override
	public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
		return null;
	}

	@Override
	public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
		return null;
	}

	@Override
	public ServletRegistration.Dynamic addServlet(String servletName, String className) {
		return null;
	}

	@Override
	public void addListener(Class<? extends EventListener> listenerClass) {
	}

	@Override
	public <T extends EventListener> void addListener(T t) {
	}

	@Override
	public void addListener(String className) {
	}

	@Override
	public Dynamic addFilter(String filterName, String className) {
		return null;
	}

	@Override
	public Dynamic addFilter(String filterName, Filter filter) {
		return null;
	}

	@Override
	public Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
		return null;
	}
}