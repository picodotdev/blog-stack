package info.blogstack.services.dao;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface LabelDAO extends GenericDAO<Label> {

	@Transactional(readOnly = true)
	Label findByName(String name);
	
	@Transactional(readOnly = true)
	Label findByHash(String hash);
}