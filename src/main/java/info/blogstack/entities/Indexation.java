package info.blogstack.entities;

import info.blogstack.misc.Globals;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "indexation", schema = Globals.SCHEMA)
public class Indexation implements Serializable {

	private static final long serialVersionUID = -7008830978353174270L;

	@Id
	@GeneratedValue
	private Long id;

	@Basic
	@Type(type = Globals.PERSISTENT_DATETIME)
	@NotNull
	private DateTime creationDate;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, mappedBy = "indexations", fetch = FetchType.LAZY)
	@OrderBy("id asc")
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
	
	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {		
		this.posts = posts;
	}
}