package info.blogstack.components;

import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

@Import(module = { "app/disqus" })
public class Disqus {

	@Parameter
	@Property
	private PostRecord post;
	
	public SourceRecord getSource() {
		return post.fetchParent(Keys.POST_SOURCE_ID);
	}
}