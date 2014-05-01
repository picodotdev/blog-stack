package info.blogstack.services.dao;

import org.springframework.transaction.annotation.Transactional;

import info.blogstack.entities.Label;

public interface LabelDAO extends GenericDAO<Label> {

	@Transactional(readOnly = true)
	Label findByName(String name);
	
	@Transactional(readOnly = true)
	Label findByHash(String hash);
}