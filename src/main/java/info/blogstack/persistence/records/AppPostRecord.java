package info.blogstack.persistence.records;

import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.interfaces.IPost;
import info.blogstack.persistence.jooq.tables.records.PostRecord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

public class AppPostRecord extends PostRecord {

	private static final long serialVersionUID = 2075090879800194733L;

	private String content;

	private Boolean fresh;

	public AppPostRecord() {
		this.fresh = Boolean.FALSE;
	}
	
	public String getContent() {
		if (content == null && getContentcompressed() != null) {
			try {
				GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(getContentcompressed()));
				StringWriter sw = new StringWriter();
				IOUtils.copy(zis, sw);
				zis.close();
				sw.close();

				content = sw.toString();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		try {
			String c = (content == null) ? "" : content;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream zos = new GZIPOutputStream(baos);
			StringReader sr = new StringReader(c);
			IOUtils.copy(sr, zos);
			sr.close();
			zos.close();

			setContentcompressed(baos.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getContentExcerpt() {
		Document document = Jsoup.parse(getContent());
		final StringBuffer excerpt = new StringBuffer();
		final List<Node> nodes = new ArrayList<>();
		document.traverse(new NodeVisitor() {
			@Override
			public void tail(Node node, int depth) {
			}

			@Override
			public void head(Node node, int depth) {
				if (excerpt.length() > Globals.POST_EXCERPT_LENGHT) {
					nodes.add(node);
				}
				if (node instanceof TextNode) {
					TextNode textNode = (TextNode) node;
					excerpt.append(textNode.text());
				}
			}
		});
		for (Node node : nodes) {
			node.remove();
		}
		return document.body().html();
	}

	public Boolean isFresh() {
		return fresh;
	}

	public void setFresh(Boolean fresh) {
		this.fresh = fresh;
	}

	public void updateHash() {		
		setHash(Utils.getHash(this, fetchParent(Keys.POST_SOURCE_ID)));
	}
	
	public DateTime getConsolidatedUpdateDate() {
		if (getUpdatedate() != null) {
			return getUpdatedate();
		} else if (getPublishdate() != null) {
			return getPublishdate();
		} else {
			return getCreationdate();
		}
	}

	public DateTime getConsolidatedPublishDate() {
		if (getPublishdate() != null) {
			return getPublishdate();
		} else {
			return getCreationdate();
		}
	}
	
	@Override
	public void setCreationdate(DateTime creationDate) {
		super.setCreationdate(creationDate);
		setDate(getConsolidatedUpdateDate());
	}
	
	@Override
	public void setUpdatedate(DateTime updateDate) {
		super.setUpdatedate(updateDate);
		setDate(getConsolidatedUpdateDate());
	}
	
	@Override
	public void setPublishdate(DateTime publishDate) {
		super.setPublishdate(publishDate);
		setDate(getConsolidatedUpdateDate());
	}

	@Override
	public void from(IPost from) {
		super.from(from);
		if (from instanceof AppPostRecord) {
			AppPostRecord afrom = (AppPostRecord) from;
			this.setFresh(afrom.isFresh());
			this.setContent(afrom.getContent());
		}
	}
}