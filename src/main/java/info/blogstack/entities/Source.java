package info.blogstack.entities;

import info.blogstack.misc.Globals;
import info.blogstack.services.MainService;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "source", schema = Globals.SCHEMA)
public class Source implements Serializable {

	private static final long serialVersionUID = -8922530118526978714L;
	
	@Id
	@GeneratedValue
	private Long id;

	@Basic
	@Type(type = Globals.PERSISTENT_DATETIME)
	@NotNull
	private DateTime creationDate;

	@Basic
	@Type(type = Globals.PERSISTENT_DATETIME)
	@NotNull
	private DateTime updateDate;

	@Basic
	@NotNull
	private String name;
	
	@Basic
	@NotNull
	private String alias;
	
	@Column(unique = true)
	@NotNull
	private String pageUrl;
	
	@Column(unique = true)
	@NotNull
	private String url;

	@Basic
	@NotNull
	private String author;
	
	@Basic
	@NotNull
	private String email;
	
	@Basic
	private String disqusShortname;
	
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private Adsense adsense;
	
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private ImportSource importSource;
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
	@OrderBy("publishDate asc")
	private List<Post> posts;

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
	}

	public DateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(DateTime updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getAutor() {
		return author;
	}

	public void setAutor(String autor) {
		this.author = autor;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getDisqusShortname() {
		return disqusShortname;
	}

	public void setDisqusShortname(String disqusShortname) {
		this.disqusShortname = disqusShortname;
	}

	public Adsense getAdsense() {
		return adsense;
	}

	public void setAdsense(Adsense adsense) {
		this.adsense = adsense;
	}
	
	public ImportSource getImportSource() {
		return importSource;
	}

	public void setImportSource(ImportSource importSource) {
		this.importSource = importSource;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {		
		this.posts = posts;
	}	
	
	private MainService getService() {
		return Globals.registry.getService(MainService.class);
	}

	@Override
	public boolean equals(Object o) {
	    if (o == this) return true;
	    if (o == null) return false;
	    if (!(o instanceof Source)) return false;
	    Source s = (Source) o;
	    return (id == s.getId());
	}

	@Override
	public int hashCode() {
		return (id == null) ? super.hashCode() : id.hashCode();
	}
}