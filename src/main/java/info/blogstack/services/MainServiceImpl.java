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
import org.springframework.beans.factory.annotation.Autowired;

public class MainServiceImpl implements MainService {

	private Configuration configuration;
	private DSLContext context;

	private PageRenderLinkSource pageRenderLinkSource;
	
	private IndexService indexService;
	private GenerateService generateService;
	private ShareService shareService;
	
	private PostDAO postDAO;
	private LabelDAO labelDAO;
	private SourceDAO sourceDAO;
	private ImportSourceDAO importSourceDAO;
	private IndexationDAO indexationDAO;
	private PostsLabelsDAO postsLabelsDAO;
	
	@Autowired
	public MainServiceImpl(Configuration configuration, DSLContext context, PageRenderLinkSource pageRenderLinkSource, IndexService indexService, GenerateService generateService, ShareService shareService, PostDAO postDAO, LabelDAO labelDAO, SourceDAO sourceDAO, ImportSourceDAO importSourceDAO, IndexationDAO indextionDAO, PostsLabelsDAO postsLabelsDAO) {
		this.configuration = configuration;
		this.context = context;

		this.pageRenderLinkSource = pageRenderLinkSource;
		
		this.indexService = indexService;
		this.generateService = generateService;
		this.shareService = shareService;
		
		this.postDAO = postDAO;
		this.labelDAO = labelDAO;
		this.sourceDAO = sourceDAO;
		this.importSourceDAO = importSourceDAO;
		this.indexationDAO = indextionDAO;
		this.postsLabelsDAO = postsLabelsDAO;
	}
	
	@Override
	public DSLContext getContext() {
		return context;
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
	public PostsLabelsDAO getPostsLabelsDAO() {
		return postsLabelsDAO;
	}
}