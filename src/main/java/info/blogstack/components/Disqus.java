package info.blogstack.components;

import info.blogstack.entities.Post;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

@Import(module = { "app/disqus" })
public class Disqus {

	@Parameter
	@Property
	private Post post;
}