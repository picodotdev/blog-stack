package info.blogstack.services;

import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GenerateService {

	void init() throws IOException;
	
	File getTo();
	File getToPage(String page, Object[] context, Map<String, String> params);
	File getToRss();
	File getToRss(LabelRecord label);
	
	File generatePage(String page, Object[] context, Map<String, String> params) throws IOException;
	List<File> generateIndex() throws IOException;
	List<File> generateLabels(List<LabelRecord> labels) throws IOException;
	List<File> generatePosts(List<PostRecord> posts) throws IOException;	
	List<File> generateArchive(Collection<PostRecord> posts) throws IOException;
	File generatePost(PostRecord post) throws IOException;
	List<File> generateLastPosts(List<SourceRecord> sources) throws IOException;
	Collection<File> generateStatics(List<File> files) throws IOException;
	File generateLastUpdated() throws IOException;
	
	File generateRss() throws Exception;
	File generateRss(LabelRecord label) throws Exception;
	File generateSitemap() throws IOException;
}