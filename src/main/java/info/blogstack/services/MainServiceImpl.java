package info.blogstack.services;

import info.blogstack.services.dao.PostDAO;
import info.blogstack.services.dao.LabelDAO;
import info.blogstack.services.dao.SourceDAO;
import info.blogstack.services.dao.IndexationDAO;
import info.blogstack.services.dao.RepositoryDAO;

import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MainServiceImpl implements MainService {

	private SessionFactory sessionFactory;
	private PageRenderLinkSource pageRenderLinkSource;
	
	private IndexerService indexerService;
	private GeneratorService publicGeneradorService;
	
	private PostDAO postDAO;
	private LabelDAO labelDAO;
	private SourceDAO sourceDAO;
	private IndexationDAO indexationDAO;
	private RepositoryDAO repositoryDAO;
	
	@Autowired
	public MainServiceImpl(SessionFactory sessionFactory, PageRenderLinkSource pageRenderLinkSource, IndexerService indexadorService, @Service("publicGeneratorService") GeneratorService publicGeneratorService, PostDAO postDAO, LabelDAO labelDAO, SourceDAO sourceDAO, IndexationDAO indextionDAO,  RepositoryDAO repositoryDAO) {
		this.sessionFactory = sessionFactory;
		this.pageRenderLinkSource = pageRenderLinkSource;
		
		this.indexerService = indexadorService;
		this.publicGeneradorService = publicGeneratorService;
		
		this.postDAO = postDAO;
		this.labelDAO = labelDAO;
		this.sourceDAO = sourceDAO;
		this.indexationDAO = indextionDAO;
		this.repositoryDAO = repositoryDAO;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override	
	public PageRenderLinkSource getPageRenderLinkSource() {
		return pageRenderLinkSource;
	}

	@Override
	public IndexerService getIndexerService() {
		return indexerService;
	}

	@Override
	public GeneratorService getPublicGeneratorService() {
		return publicGeneradorService;
	}

	@Override
	public PostDAO getPostDAO() {
		return postDAO;
	}
	
	@Override
	public LabelDAO getLabelDAO() {
		return labelDAO;
	}
	
	@Override
	public SourceDAO getSourceDAO() {
		return sourceDAO;
	}
	
	@Override
	public IndexationDAO getIndexationDAO() {
		return indexationDAO;
	}
	
	@Override
	public RepositoryDAO getRepositoryDAO() {
		return repositoryDAO;
	}
}