package info.blogstack.services.dao;

import info.blogstack.entities.Indexation;

public interface IndexationDAO extends GenericDAO<Indexation> {

	Indexation findLast();
}