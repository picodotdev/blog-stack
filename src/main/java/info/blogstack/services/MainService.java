package info.blogstack.services;

import info.blogstack.misc.Configuration;
import info.blogstack.persistence.daos.ImportSourceDAO;
import info.blogstack.persistence.daos.IndexationDAO;
import info.blogstack.persistence.daos.LabelDAO;
import info.blogstack.persistence.daos.PostDAO;
import info.blogstack.persistence.daos.PostsLabelsDAO;
import info.blogstack.persistence.daos.SourceDAO;

import org.apache.tapestry5.services.PageRenderLinkSource;
import org.jooq.DSLContext;

public interface MainService {

	DSLContext getContext();
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
	PostsLabelsDAO getPostsLabelsDAO();
}