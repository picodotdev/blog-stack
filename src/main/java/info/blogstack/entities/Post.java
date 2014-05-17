package info.blogstack.entities;

import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

@Entity
@Table(name = "post", schema = Globals.SCHEMA)
public class Post implements Serializable {

	private static final long serialVersionUID = 4336687073751320951L;

	@Id
	@GeneratedValue
	private Long id;

	@Basic
	@Type(type = Globals.PERSISTENT_DATETIME)
	@NotNull
	private DateTime creationDate;

	@Basic
	@Type(type = Globals.PERSISTENT_DATETIME)
	private DateTime updateDate;

	@Basic
	@Type(type = Globals.PERSISTENT_DATETIME)
	@NotNull
	private DateTime publishDate;

	@Basic
	@Type(type = Globals.PERSISTENT_DATETIME)
	@NotNull
	private DateTime date;

	@Column(unique = true)
	@NotNull
	private String url;

	@Basic
	@NotNull
	private String title;

	@Basic
	@NotNull
	private String hash;
	
	@Basic
	@NotNull
	private String author;

	@Transient
	private String content;
	
	@Lob
	@NotNull
	private Blob contentCompressed;
	
	@Basic
	private Boolean visible;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_id", nullable = false)
	private Source source;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "posts_indexations", schema = Globals.SCHEMA, joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "indexation_id", referencedColumnName = "id", nullable = false))
	private Set<Indexation> indexations;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
	@JoinTable(name = "posts_labels", schema = Globals.SCHEMA, joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id", nullable = false))
	private Set<Label> labels;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
		setDate(getConsolidatedUpdateDate());
	}

	public DateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(DateTime updateDate) {
		this.updateDate = updateDate;
		setDate(getConsolidatedUpdateDate());
	}

	public DateTime getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(DateTime publishDate) {
		this.publishDate = publishDate;
		setDate(getConsolidatedUpdateDate());
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Transient
	public String getContent() {
		if (content == null && contentCompressed != null) {
			try {
				GZIPInputStream zis = new GZIPInputStream(contentCompressed.getBinaryStream());
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

	public void setContent(String content, Session session) {
		this.content = content;
		try {
			String c = (content == null) ? "" : content;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream zos = new GZIPOutputStream(baos);
			StringReader sr = new StringReader(c);
			IOUtils.copy(sr, zos);
			sr.close();
			zos.close();
			
			byte[] bytes = baos.toByteArray();
			Blob blob = session.getLobHelper().createBlob(bytes);

			setContentCompressed(blob);
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
	
	public Blob getContentCompressed() {
		return contentCompressed;
	}

	public void setContentCompressed(Blob contentCompressed) {
		this.contentCompressed = contentCompressed;
	}
	
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}
	
	public Set<Indexation> getIndexations() {
		return indexations;
	}

	public void setIndexations(Set<Indexation> indexations) {
		this.indexations = indexations;
	}

	public Set<Label> getLabels() {
		return labels;
	}

	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public void updateHash() {		
		setHash(Utils.getHash(this));
	}
	
	public DateTime getConsolidatedUpdateDate() {
		if (updateDate != null) {
			return updateDate;
		} else if (publishDate != null) {
			return publishDate;
		} else {
			return creationDate;
		}
	}
	
	public DateTime getConsolidatedPublishDate() {
		if (publishDate != null) {
			return publishDate;
		} else {
			return creationDate;
		}
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) return true;
	    if (o == null) return false;
	    if (!(o instanceof Post)) return false;
	    Post p = (Post) o;
	    return (id != null && id.equals(p.getId()));
	}

	@Override
	public int hashCode() {
		return (id == null) ? super.hashCode() : id.hashCode();
	}
}