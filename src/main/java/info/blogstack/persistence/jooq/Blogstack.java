/**
 * This class is generated by jOOQ
 */
package info.blogstack.persistence.jooq;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.2"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Blogstack extends org.jooq.impl.SchemaImpl {

	private static final long serialVersionUID = 928563707;

	/**
	 * The reference instance of <code>BLOGSTACK</code>
	 */
	public static final Blogstack BLOGSTACK = new Blogstack();

	/**
	 * No further instances allowed
	 */
	private Blogstack() {
		super("BLOGSTACK");
	}

	@Override
	public final java.util.List<org.jooq.Sequence<?>> getSequences() {
		java.util.List result = new java.util.ArrayList();
		result.addAll(getSequences0());
		return result;
	}

	private final java.util.List<org.jooq.Sequence<?>> getSequences0() {
		return java.util.Arrays.<org.jooq.Sequence<?>>asList(
			info.blogstack.persistence.jooq.Sequences.SYSTEM_SEQUENCE_2E02FFE3_8701_48C3_81FE_341F2CC2BF06,
			info.blogstack.persistence.jooq.Sequences.SYSTEM_SEQUENCE_39562988_6D23_4305_B39C_27DBA18DB8DE,
			info.blogstack.persistence.jooq.Sequences.SYSTEM_SEQUENCE_8CD3AFD5_2A41_4A0E_8369_E4F6CCA4D07D,
			info.blogstack.persistence.jooq.Sequences.SYSTEM_SEQUENCE_9C8E7D15_5525_4D62_8CAD_84D8646A6A56,
			info.blogstack.persistence.jooq.Sequences.SYSTEM_SEQUENCE_A8FC292C_948E_4DBF_B050_6B6905BDA5E8,
			info.blogstack.persistence.jooq.Sequences.SYSTEM_SEQUENCE_AAC90520_24FC_45CF_B7F2_BBA672F82CC1,
			info.blogstack.persistence.jooq.Sequences.SYSTEM_SEQUENCE_C958AC1C_6F7A_4310_8564_E5406FB1FFD2,
			info.blogstack.persistence.jooq.Sequences.SYSTEM_SEQUENCE_E696ABD2_9724_469F_9EF9_59427C97B083);
	}

	@Override
	public final java.util.List<org.jooq.Table<?>> getTables() {
		java.util.List result = new java.util.ArrayList();
		result.addAll(getTables0());
		return result;
	}

	private final java.util.List<org.jooq.Table<?>> getTables0() {
		return java.util.Arrays.<org.jooq.Table<?>>asList(
			info.blogstack.persistence.jooq.tables.Indexation.INDEXATION,
			info.blogstack.persistence.jooq.tables.Adsense.ADSENSE,
			info.blogstack.persistence.jooq.tables.ImportSource.IMPORT_SOURCE,
			info.blogstack.persistence.jooq.tables.Post.POST,
			info.blogstack.persistence.jooq.tables.Label.LABEL,
			info.blogstack.persistence.jooq.tables.PostsIndexations.POSTS_INDEXATIONS,
			info.blogstack.persistence.jooq.tables.PostsLabels.POSTS_LABELS,
			info.blogstack.persistence.jooq.tables.Source.SOURCE);
	}
}
