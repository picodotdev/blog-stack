package info.blogstack.services.spring;

import info.blogstack.persistence.daos.AdsenseDAO;
import info.blogstack.persistence.daos.AdsenseDAOImpl;
import info.blogstack.persistence.daos.ImportSourceDAO;
import info.blogstack.persistence.daos.ImportSourceDAOImpl;
import info.blogstack.persistence.daos.IndexationDAO;
import info.blogstack.persistence.daos.IndexationDAOImpl;
import info.blogstack.persistence.daos.LabelDAO;
import info.blogstack.persistence.daos.LabelDAOImpl;
import info.blogstack.persistence.daos.PostDAO;
import info.blogstack.persistence.daos.PostDAOImpl;
import info.blogstack.persistence.daos.PostsIndexationsDAO;
import info.blogstack.persistence.daos.PostsIndexationsDAOImpl;
import info.blogstack.persistence.daos.PostsLabelsDAO;
import info.blogstack.persistence.daos.PostsLabelsDAOImpl;
import info.blogstack.persistence.daos.SourceDAO;
import info.blogstack.persistence.daos.SourceDAOImpl;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.ResourceTransactionManager;

@Configuration
@ComponentScan({ "info.blogstack" })
@EnableTransactionManagement
public class AppConfiguration {

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.h2.Driver");
		ds.setUrl("jdbc:h2:./misc/database/app");
		ds.setUsername("sa");
		ds.setPassword("sa");
		return ds;
	}

	@Bean
	public DataSource transactionAwareDataSource(DataSource dataSource) {
		return new TransactionAwareDataSourceProxy(dataSource);
	}
	
	@Bean
	public ResourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean
	public ConnectionProvider connectionProvider(DataSource dataSource) {
		return new DataSourceConnectionProvider(dataSource);
	}
	
	@Bean
	public org.jooq.Configuration config(ConnectionProvider connectionProvider) {
		DefaultConfiguration config = new DefaultConfiguration();
		config.set(connectionProvider);
		config.set(SQLDialect.H2);
		return config;
	}
	
	@Bean
	public DSLContext dsl(org.jooq.Configuration config) {
		return DSL.using(config);
	}
	
	@Bean
	public AdsenseDAO adsenseDAO(DSLContext context) {
		return new AdsenseDAOImpl(context);
	}
	
	@Bean
	public ImportSourceDAO importSourceDAO(DSLContext context) {
		return new ImportSourceDAOImpl(context);
	}
	
	@Bean
	public IndexationDAO indexationDAO(DSLContext context) {
		return new IndexationDAOImpl(context);
	}
	
	@Bean
	public LabelDAO labelDAO(DSLContext context) {
		return new LabelDAOImpl(context);
	}
	
	@Bean
	public PostDAO postDAO(DSLContext context) {
		return new PostDAOImpl(context);
	}
	
	@Bean
	public PostsIndexationsDAO postsIndexationsDAO(DSLContext context) {
		return new PostsIndexationsDAOImpl(context);
	}
	
	@Bean
	public PostsLabelsDAO postsLabelsDAO(DSLContext context) {
		return new PostsLabelsDAOImpl(context);
	}
	
	@Bean
	public SourceDAO sourceDAO(DSLContext context) {
		return new SourceDAOImpl(context);
	}
}