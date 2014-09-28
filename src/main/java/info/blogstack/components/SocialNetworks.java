package info.blogstack.components;

import info.blogstack.pages.Index;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

@Import(module = { "app/facebook", "app/googleplus" })
public class SocialNetworks {
	
	@Inject
	PageRenderLinkSource source;
	
	public String getUrl() {
		return source.createPageRenderLink(Index.class).toAbsoluteURI();
	}
}