package info.blogstack.entities;

import info.blogstack.misc.Globals;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "import_source", schema = Globals.SCHEMA)
public class ImportSource implements Serializable {

	private static final long serialVersionUID = 2741616265287320302L;

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
}