package info.blogstack.misc;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.ioc.Registry;

public class Globals {

	public enum Environment {
		DEVELOPMENT, PRODUCTION
	}
	
	public static Registry registry;
	public static Environment environment;
	
	public static final File PUBLIC = new File("_public");
	public static final File DEPLOY = new File("_deploy");
	public static final File MISC = new File("misc");
	public static final File IMPORT = new File(MISC, "imports");
	public static final List<File> STATICS = Arrays.asList(new File("src/main/webapp"), new File("src/main/resources/META-INF"));
	
	public static final String SCHEMA = "blogstack";
	public static final String PERSISTENT_DATETIME = "org.jadira.usertype.dateandtime.joda.PersistentDateTime";
	
	public static final Integer NUMBER_PAGES_INDEX = 0;
	public static final Integer NUMBER_PAGES_LABEL = 0;
	public static final Integer NUMBER_POSTS_PAGE = 10;
	public static final Integer NUMBER_POSTS_LASTS = 20;
	public static final Integer NUMBER_POSTS_FEATURED = 5;
	public static final Integer NUMBER_POSTS_FEATURED_LABEL = 8;
	public static final Integer NUMBER_POSTS_FEED = 7;
	public static final Integer NUMBER_POSTS_SITEMAP = 50;
	public static final Integer POST_EXCERPT_LENGHT = 800;
	public static final Locale LOCALE = new Locale("es", "ES");
	
	public static final String DISQUS_BLOGSTACK = "blogstack";
}