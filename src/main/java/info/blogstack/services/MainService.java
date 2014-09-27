package info.blogstack.services;

import info.blogstack.misc.Configuration;
import info.blogstack.services.dao.ImportSourceDAO;
import info.blogstack.services.dao.IndexationDAO;
import info.blogstack.services.dao.LabelDAO;
import info.blogstack.services.dao.PostDAO;
import info.blogstack.services.dao.RepositoryDAO;
import info.blogstack.services.dao.SourceDAO;

import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.SessionFactory;

public interface MainService {

	SessionFactory getSessionFactory();
	PageRenderLinkSource getPageRenderLinkSource();
	
	Configuration getConfiguracion();
	IndexService getIndexService();
	GenerateService getGenerateService();
	ShareService getShareService();
	
	PostDAO getPostDAO();
	LabelDAO getLabelDAO();
	SourceDAO getSourceDAO();
	ImportSourceDAO getImportSourceDAO();
	IndexationDAO getIndexationDAO();
	RepositoryDAO getRepositoryDAO();
}
