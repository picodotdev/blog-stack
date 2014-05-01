package info.blogstack.services.dao;

import info.blogstack.entities.Post;
import info.blogstack.entities.Source;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class RepositoryDAOImpl implements RepositoryDAO {

	private File directory;

	public RepositoryDAOImpl() {
		this(new File("misc/database/repository"));
	}

	public RepositoryDAOImpl(File directory) throws IllegalArgumentException {
		directory.mkdirs();
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException();
		}
		this.directory = directory;
	}

	@Override
	public File getFile(Post post) {
		File f = getPostFile(post);
		return (f.exists()) ? f : null;
	}

	@Override
	public String getContent(Post post) {
		try {
			File f = getPostZipFile(post);
			InputStream is = new FileInputStream(f);
			ZipInputStream zis = new ZipInputStream(is);
			ZipEntry zi = zis.getNextEntry();
			StringWriter sw = new StringWriter();
			IOUtils.copy(zis, sw);
			sw.close();
			zis.close();
			return sw.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public File persist(Source source) {
		File f = getSourceFile(source);
		f.mkdirs();
		return f;
	}

	@Override
	public File persist(Post post) {
		try {
			File f = getPostFile(post);
			File ff = getPostZipFile(post);
			if (ff.exists()) {
				ff.delete();
			} else { 
				ff.getParentFile().mkdirs();				
			}
			Reader r = new StringReader(post.getContent());
			OutputStream os = new FileOutputStream(ff);
			ZipOutputStream zos = new ZipOutputStream(os);
			zos.putNextEntry(new ZipEntry(f.getName()));
			OutputStreamWriter osw = new OutputStreamWriter(zos, Charset.forName("UTF-8"));
			IOUtils.copy(r, osw);
			r.close();
			osw.close();
			return f;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private File getSourceFile(Source source) {
		String n = String.format("%s-%s", transformId(source.getId()), source.getName());
		n = filterName(n);
		return new File(directory, n);
	}

	private File getPostFile(Post post) {
		File f = getSourceFile(post.getSource());
		String n = String.format("%s-%s.html", transformId(post.getId()), post.getTitle());
		n = filterName(n);
		return new File(f, n);
	}
	
	private File getPostZipFile(Post post) {
		File f = getPostFile(post);
		return new File(f.getParent(), f.getName().replace(".html", ".zip"));
	}

	/**
	 * Devuelve un id con el formato aaaa999.
	 */
	private String transformId(Long id) {
		BigInteger max = new BigInteger("1000");
		String leters = "abcdefghijklmnopqrstuvwxyz";
		BigInteger lettersLength = new BigInteger(String.valueOf(leters.length()));

		BigInteger n = new BigInteger(id.toString());
		BigInteger n1 = n.divide(max);
		BigInteger r1 = n.remainder(max);

		BigInteger n2 = n1.divide(lettersLength);
		BigInteger r2 = n1.remainder(lettersLength);

		BigInteger n3 = n2.divide(lettersLength);
		BigInteger r3 = n2.remainder(lettersLength);

		BigInteger n4 = n3.divide(lettersLength);
		BigInteger r4 = n3.remainder(lettersLength);

		BigInteger n5 = n4.divide(lettersLength);
		BigInteger r5 = n4.remainder(lettersLength);

		Integer s1 = r1.intValue();
		String s2 = String.valueOf(leters.charAt(r2.intValue()));
		String s3 = String.valueOf(leters.charAt(r3.intValue()));
		String s4 = String.valueOf(leters.charAt(r4.intValue()));
		String s5 = String.valueOf(leters.charAt(r5.intValue()));

		return String.format("%s%s%s%s%03d", s5, s4, s3, s2, s1);
	}

	private String filterName(String s) {
		return s.replaceAll("[\\/:\"*?<>|]+", " ");
	}
}