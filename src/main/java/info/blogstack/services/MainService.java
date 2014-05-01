package info.blogstack.services;

import info.blogstack.services.dao.PostDAO;
import info.blogstack.services.dao.LabelDAO;
import info.blogstack.services.dao.SourceDAO;
import info.blogstack.services.dao.IndexationDAO;
import info.blogstack.services.dao.RepositoryDAO;

import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.SessionFactory;

public interface MainService {

	SessionFactory getSessionFactory();
	PageRenderLinkSource getPageRenderLinkSource();
	
	IndexerService getIndexerService();
	GeneratorService getPublicGeneratorService();
	
	PostDAO getPostDAO();
	LabelDAO getLabelDAO();
	SourceDAO getSourceDAO();
	IndexationDAO getIndexationDAO();
	RepositoryDAO getRepositoryDAO();
}
