package info.blogstack.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

public class AppWhitelist extends Whitelist {

	private List<Map<String, String>> attributes;

	private AppWhitelist() {
		attributes = new ArrayList<>();
	}

	public static Whitelist relaxed() {
		Whitelist wl = new AppWhitelist()
				.addTags("a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6", "i",
						"img", "li", "ol", "p", "pre", "q", "small", "strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul")

				.addAttributes("a", "href", "title")
				.addAttributes("blockquote", "cite")
				.addAttributes("col", "span", "width")
				.addAttributes("colgroup", "span", "width")
				.addAttributes("img", "align", "alt", "height", "src", "title", "width")
				.addAttributes("ol", "start", "type")
				.addAttributes("q", "cite")
				.addAttributes("table", "summary", "width")
				.addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width")
				.addAttributes("th", "abbr", "axis", "colspan", "rowspan", "scope", "width")
				.addAttributes("ul", "type")

				.addProtocols("a", "href", "ftp", "http", "https", "mailto")
				.addProtocols("blockquote", "cite", "http", "https")
				.addProtocols("cite", "cite", "http", "https")
				.addProtocols("img", "src", "http", "https")
				.addProtocols("q", "cite", "http", "https")
				
				//
				.addTags("script", "iframe", "noscript")
				.addAttributes("div", "style")
				.addAttributes("img", "style")
				.addAttributes("script", "src", "data-.*")
				.addAttributes("iframe", "src", "width", "height", "frameborder", "allowfullscreen", "style", "marginwidth", "marginheight", "frameborder", "scrolling");
		return wl;
	}

	public Whitelist addAttribute(String tag, String key, String regexp) {
		Map<String, String> attribute = new HashMap<>();
		attribute.put("tag", tag);
		attribute.put("key", key);
		attribute.put("regexp", regexp);
		return this;
	}

	@Override
	protected boolean isSafeAttribute(String tagName, Element el, Attribute attr) {
		for (Map<String, String> attribute : attributes) {
			String tag = attribute.get("tag");
			String key = attribute.get("key");
			String regexp = attribute.get("regexp");

			if (tag.equals(tagName) && attr.getKey().matches(key) && attr.getValue().matches(regexp)) {
				return true;
			}
		}
		return super.isSafeAttribute(tagName, el, attr);
	}
}
