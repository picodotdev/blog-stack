package info.blogstack.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptAggregationStrategy;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

public class BlogStackStack implements JavaScriptStack {

	private final AssetSource assetSource;

	public BlogStackStack(final AssetSource assetSource) {
		this.assetSource = assetSource;
	}

	@Override
	public String getInitialization() {
		return null;
	}

	@Override
	public List<String> getModules() {
		List<String> r = new ArrayList<>();		
		r.add("app/analytics");
		return r;
	}
	
	@Override
	public JavaScriptAggregationStrategy getJavaScriptAggregationStrategy() {
		return JavaScriptAggregationStrategy.DO_NOTHING;
	}

	@Override
	public List<Asset> getJavaScriptLibraries() {
		return Collections.emptyList();
	}

	@Override
	public List<StylesheetLink> getStylesheets() {
		List<StylesheetLink> r = new ArrayList<StylesheetLink>();
		r.add(new StylesheetLink(assetSource.getContextAsset("/css/core.css", null)));
		return r;
	}

	@Override
	public List<String> getStacks() {
		return Collections.emptyList();
	}
}