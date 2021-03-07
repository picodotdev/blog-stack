/**
 * This class is generated by jOOQ
 */
package info.blogstack.persistence.jooq;


import info.blogstack.persistence.jooq.tables.Adsense;
import info.blogstack.persistence.jooq.tables.ImportSource;
import info.blogstack.persistence.jooq.tables.Indexation;
import info.blogstack.persistence.jooq.tables.Label;
import info.blogstack.persistence.jooq.tables.Newsletter;
import info.blogstack.persistence.jooq.tables.Post;
import info.blogstack.persistence.jooq.tables.PostsIndexations;
import info.blogstack.persistence.jooq.tables.PostsLabels;
import info.blogstack.persistence.jooq.tables.Source;
import info.blogstack.persistence.jooq.tables.records.AdsenseRecord;
import info.blogstack.persistence.jooq.tables.records.ImportSourceRecord;
import info.blogstack.persistence.jooq.tables.records.IndexationRecord;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.NewsletterRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord;
import info.blogstack.persistence.jooq.tables.records.PostsLabelsRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>BLOGSTACK</code> 
 * schema
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.1"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------

	public static final Identity<IndexationRecord, Long> IDENTITY_INDEXATION = Identities0.IDENTITY_INDEXATION;
	public static final Identity<AdsenseRecord, Long> IDENTITY_ADSENSE = Identities0.IDENTITY_ADSENSE;
	public static final Identity<ImportSourceRecord, Long> IDENTITY_IMPORT_SOURCE = Identities0.IDENTITY_IMPORT_SOURCE;
	public static final Identity<NewsletterRecord, Long> IDENTITY_NEWSLETTER = Identities0.IDENTITY_NEWSLETTER;
	public static final Identity<PostRecord, Long> IDENTITY_POST = Identities0.IDENTITY_POST;
	public static final Identity<LabelRecord, Long> IDENTITY_LABEL = Identities0.IDENTITY_LABEL;
	public static final Identity<PostsIndexationsRecord, Long> IDENTITY_POSTS_INDEXATIONS = Identities0.IDENTITY_POSTS_INDEXATIONS;
	public static final Identity<PostsLabelsRecord, Long> IDENTITY_POSTS_LABELS = Identities0.IDENTITY_POSTS_LABELS;
	public static final Identity<SourceRecord, Long> IDENTITY_SOURCE = Identities0.IDENTITY_SOURCE;

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<IndexationRecord> PK_INDEXATION = UniqueKeys0.PK_INDEXATION;
	public static final UniqueKey<AdsenseRecord> PK_ADSENSE = UniqueKeys0.PK_ADSENSE;
	public static final UniqueKey<ImportSourceRecord> PK_IMPORT_SOURCE = UniqueKeys0.PK_IMPORT_SOURCE;
	public static final UniqueKey<NewsletterRecord> PK_NEWSLETTER = UniqueKeys0.PK_NEWSLETTER;
	public static final UniqueKey<PostRecord> PK_POST = UniqueKeys0.PK_POST;
	public static final UniqueKey<PostRecord> CONSTRAINT_2 = UniqueKeys0.CONSTRAINT_2;
	public static final UniqueKey<LabelRecord> PK_LABEL = UniqueKeys0.PK_LABEL;
	public static final UniqueKey<PostsIndexationsRecord> PK_POSTS_INDEXATIONS = UniqueKeys0.PK_POSTS_INDEXATIONS;
	public static final UniqueKey<PostsLabelsRecord> PK_POSTS_LABELS = UniqueKeys0.PK_POSTS_LABELS;
	public static final UniqueKey<SourceRecord> PK_SOURCE = UniqueKeys0.PK_SOURCE;
	public static final UniqueKey<SourceRecord> CONSTRAINT_9 = UniqueKeys0.CONSTRAINT_9;
	public static final UniqueKey<SourceRecord> CONSTRAINT_92 = UniqueKeys0.CONSTRAINT_92;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final ForeignKey<PostRecord, IndexationRecord> POST_INDEXATION_ID = ForeignKeys0.POST_INDEXATION_ID;
	public static final ForeignKey<PostRecord, SourceRecord> POST_SOURCE_ID = ForeignKeys0.POST_SOURCE_ID;
	public static final ForeignKey<PostRecord, NewsletterRecord> POST_NEWSLETTER_ID = ForeignKeys0.POST_NEWSLETTER_ID;
	public static final ForeignKey<PostsIndexationsRecord, PostRecord> POSTS_INDEXATIONS_POST_ID = ForeignKeys0.POSTS_INDEXATIONS_POST_ID;
	public static final ForeignKey<PostsIndexationsRecord, IndexationRecord> POSTS_INDEXATIONS_INDEXATION_ID = ForeignKeys0.POSTS_INDEXATIONS_INDEXATION_ID;
	public static final ForeignKey<PostsLabelsRecord, PostRecord> POSTS_LABELS_POST_ID = ForeignKeys0.POSTS_LABELS_POST_ID;
	public static final ForeignKey<PostsLabelsRecord, LabelRecord> POSTS_LABELS_LABEL_ID = ForeignKeys0.POSTS_LABELS_LABEL_ID;
	public static final ForeignKey<SourceRecord, AdsenseRecord> SOURCE_ADSENSE_ID = ForeignKeys0.SOURCE_ADSENSE_ID;
	public static final ForeignKey<SourceRecord, ImportSourceRecord> SOURCE_IMPORTSOURCE_ID = ForeignKeys0.SOURCE_IMPORTSOURCE_ID;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Identities0 extends AbstractKeys {
		public static Identity<IndexationRecord, Long> IDENTITY_INDEXATION = createIdentity(Indexation.INDEXATION, Indexation.INDEXATION.ID);
		public static Identity<AdsenseRecord, Long> IDENTITY_ADSENSE = createIdentity(Adsense.ADSENSE, Adsense.ADSENSE.ID);
		public static Identity<ImportSourceRecord, Long> IDENTITY_IMPORT_SOURCE = createIdentity(ImportSource.IMPORT_SOURCE, ImportSource.IMPORT_SOURCE.ID);
		public static Identity<NewsletterRecord, Long> IDENTITY_NEWSLETTER = createIdentity(Newsletter.NEWSLETTER, Newsletter.NEWSLETTER.ID);
		public static Identity<PostRecord, Long> IDENTITY_POST = createIdentity(Post.POST, Post.POST.ID);
		public static Identity<LabelRecord, Long> IDENTITY_LABEL = createIdentity(Label.LABEL, Label.LABEL.ID);
		public static Identity<PostsIndexationsRecord, Long> IDENTITY_POSTS_INDEXATIONS = createIdentity(PostsIndexations.POSTS_INDEXATIONS, PostsIndexations.POSTS_INDEXATIONS.ID);
		public static Identity<PostsLabelsRecord, Long> IDENTITY_POSTS_LABELS = createIdentity(PostsLabels.POSTS_LABELS, PostsLabels.POSTS_LABELS.ID);
		public static Identity<SourceRecord, Long> IDENTITY_SOURCE = createIdentity(Source.SOURCE, Source.SOURCE.ID);
	}

	private static class UniqueKeys0 extends AbstractKeys {
		public static final UniqueKey<IndexationRecord> PK_INDEXATION = createUniqueKey(Indexation.INDEXATION, Indexation.INDEXATION.ID);
		public static final UniqueKey<AdsenseRecord> PK_ADSENSE = createUniqueKey(Adsense.ADSENSE, Adsense.ADSENSE.ID);
		public static final UniqueKey<ImportSourceRecord> PK_IMPORT_SOURCE = createUniqueKey(ImportSource.IMPORT_SOURCE, ImportSource.IMPORT_SOURCE.ID);
		public static final UniqueKey<NewsletterRecord> PK_NEWSLETTER = createUniqueKey(Newsletter.NEWSLETTER, Newsletter.NEWSLETTER.ID);
		public static final UniqueKey<PostRecord> PK_POST = createUniqueKey(Post.POST, Post.POST.ID);
		public static final UniqueKey<PostRecord> CONSTRAINT_2 = createUniqueKey(Post.POST, Post.POST.URL);
		public static final UniqueKey<LabelRecord> PK_LABEL = createUniqueKey(Label.LABEL, Label.LABEL.ID);
		public static final UniqueKey<PostsIndexationsRecord> PK_POSTS_INDEXATIONS = createUniqueKey(PostsIndexations.POSTS_INDEXATIONS, PostsIndexations.POSTS_INDEXATIONS.ID);
		public static final UniqueKey<PostsLabelsRecord> PK_POSTS_LABELS = createUniqueKey(PostsLabels.POSTS_LABELS, PostsLabels.POSTS_LABELS.ID);
		public static final UniqueKey<SourceRecord> PK_SOURCE = createUniqueKey(Source.SOURCE, Source.SOURCE.ID);
		public static final UniqueKey<SourceRecord> CONSTRAINT_9 = createUniqueKey(Source.SOURCE, Source.SOURCE.PAGEURL);
		public static final UniqueKey<SourceRecord> CONSTRAINT_92 = createUniqueKey(Source.SOURCE, Source.SOURCE.URL);
	}

	private static class ForeignKeys0 extends AbstractKeys {
		public static final ForeignKey<PostRecord, IndexationRecord> POST_INDEXATION_ID = createForeignKey(info.blogstack.persistence.jooq.Keys.PK_INDEXATION, Post.POST, Post.POST.INDEXATION_ID);
		public static final ForeignKey<PostRecord, SourceRecord> POST_SOURCE_ID = createForeignKey(info.blogstack.persistence.jooq.Keys.PK_SOURCE, Post.POST, Post.POST.SOURCE_ID);
		public static final ForeignKey<PostRecord, NewsletterRecord> POST_NEWSLETTER_ID = createForeignKey(info.blogstack.persistence.jooq.Keys.PK_NEWSLETTER, Post.POST, Post.POST.NEWSLETTER_ID);
		public static final ForeignKey<PostsIndexationsRecord, PostRecord> POSTS_INDEXATIONS_POST_ID = createForeignKey(info.blogstack.persistence.jooq.Keys.PK_POST, PostsIndexations.POSTS_INDEXATIONS, PostsIndexations.POSTS_INDEXATIONS.POST_ID);
		public static final ForeignKey<PostsIndexationsRecord, IndexationRecord> POSTS_INDEXATIONS_INDEXATION_ID = createForeignKey(info.blogstack.persistence.jooq.Keys.PK_INDEXATION, PostsIndexations.POSTS_INDEXATIONS, PostsIndexations.POSTS_INDEXATIONS.INDEXATION_ID);
		public static final ForeignKey<PostsLabelsRecord, PostRecord> POSTS_LABELS_POST_ID = createForeignKey(info.blogstack.persistence.jooq.Keys.PK_POST, PostsLabels.POSTS_LABELS, PostsLabels.POSTS_LABELS.POST_ID);
		public static final ForeignKey<PostsLabelsRecord, LabelRecord> POSTS_LABELS_LABEL_ID = createForeignKey(info.blogstack.persistence.jooq.Keys.PK_LABEL, PostsLabels.POSTS_LABELS, PostsLabels.POSTS_LABELS.LABEL_ID);
		public static final ForeignKey<SourceRecord, AdsenseRecord> SOURCE_ADSENSE_ID = createForeignKey(info.blogstack.persistence.jooq.Keys.PK_ADSENSE, Source.SOURCE, Source.SOURCE.ADSENSE_ID);
		public static final ForeignKey<SourceRecord, ImportSourceRecord> SOURCE_IMPORTSOURCE_ID = createForeignKey(info.blogstack.persistence.jooq.Keys.PK_IMPORT_SOURCE, Source.SOURCE, Source.SOURCE.IMPORTSOURCE_ID);
	}
}