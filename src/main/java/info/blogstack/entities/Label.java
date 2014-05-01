package info.blogstack.entities;

import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "label", schema = Globals.SCHEMA)
public class Label implements Serializable {

	private static final long serialVersionUID = 1130894427514131716L;

	@Id
	@GeneratedValue
	private Long id;

	@Basic
	@Type(type = Globals.PERSISTENT_DATETIME)
	@NotNull
	private DateTime creationDate;

	@Basic
	@NotNull
	private String name;
	
	@Basic
	@NotNull
	private String hash;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, mappedBy = "labels", fetch = FetchType.LAZY)
	private Set<Post> posts;
	
	public Label() {		
	}
	
	public Label(String name) {
		this.creationDate = DateTime.now();
		this.name = name;
		this.posts = new HashSet<>();
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}
	
	public void updateHash() {		
		setHash(Utils.getHash(this));
	}
	
	@Override
	public boolean equals (Object o) {
	    if (o == this) return true;
	    if (o == null) return false;
	    if (!(o instanceof Label)) return false;
		Label l = (Label) o;
		return id == l.getId();			
	}
	
	@Override
	public int hashCode() {
		return (id == null) ? super.hashCode() : id.hashCode();
	}
}