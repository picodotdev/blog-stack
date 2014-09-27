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
import org.springframework.beans.factory.annotation.Autowired;

public class MainServiceImpl implements MainService {

	private SessionFactory sessionFactory;
	private PageRenderLinkSource pageRenderLinkSource;
	
	private Configuration configuration;
	private IndexService indexService;
	private GenerateService generateService;
	private ShareService shareService;
	
	private PostDAO postDAO;
	private LabelDAO labelDAO;
	private SourceDAO sourceDAO;
	private ImportSourceDAO importSourceDAO;
	private IndexationDAO indexationDAO;
	private RepositoryDAO repositoryDAO;
	
	@Autowired
	public MainServiceImpl(SessionFactory sessionFactory, PageRenderLinkSource pageRenderLinkSource, Configuration configuration, IndexService indexService, GenerateService generateService, ShareService shareService, PostDAO postDAO, LabelDAO labelDAO, SourceDAO sourceDAO, ImportSourceDAO importSourceDAO, IndexationDAO indextionDAO,  RepositoryDAO repositoryDAO) {
		this.sessionFactory = sessionFactory;
		this.pageRenderLinkSource = pageRenderLinkSource;
		
		this.configuration = configuration;
		this.indexService = indexService;
		this.generateService = generateService;
		this.shareService = shareService;
		
		this.postDAO = postDAO;
		this.labelDAO = labelDAO;
		this.sourceDAO = sourceDAO;
		this.importSourceDAO = importSourceDAO;
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
	public Configuration getConfiguracion() {
		return configuration;
	}

	@Override
	public IndexService getIndexService() {
		return indexService;
	}

	@Override
	public GenerateService getGenerateService() {
		return generateService;
	}
	
	@Override
	public ShareService getShareService() {
		return shareService;
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
	public ImportSourceDAO getImportSourceDAO() {
		return importSourceDAO;
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