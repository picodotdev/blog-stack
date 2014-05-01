package info.blogstack.services;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.entities.Source;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GeneratorService {

	File getTo();
	File getToPage(String page, Object[] context, Map<String, String> params);
	File getToRss();
	File getToRss(Label label);
	
	File generatePage(String page, Object[] context, Map<String, String> params) throws IOException;
	List<File> generatePosts(List<Post> posts) throws IOException;
	File generatePost(Post post) throws IOException;
	List<File> generateLastPosts(List<Source> sources) throws IOException;
	Collection<File> generateStatics(List<File> files) throws IOException;
	File generateLastUpdated() throws IOException;
	
	File generateRss() throws Exception;
	File generateRss(Label label) throws Exception;
	File generateSitemap() throws IOException;
}