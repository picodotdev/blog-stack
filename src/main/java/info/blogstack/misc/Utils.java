package info.blogstack.misc;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.services.MainService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class Utils {

	public static String getHash(Post post) {
		return Utils.getHash(Utils.getContext(post));
	}
	
	public static String getHash(Label label) {
		return Utils.getHash(Utils.getContext(label));
	}
	
	public static String getHash(Object[] context) {
		try {
			String[] s = new String[context.length];
			for (int i = 0; i < s.length; ++i) {
				s[i] = "%s";
			}
			String ss = String.format(StringUtils.join(s, "-"), context);
			byte[] h = MessageDigest.getInstance("MD5").digest(ss.getBytes());
			return Base64.encodeBase64String(h);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Object[] getContext(Post post) {
		String f = post.getSource().getAlias();
		String y = String.valueOf(post.getConsolidatedPublishDate().getYear());
		String m = StringUtils.leftPad(String.valueOf(post.getConsolidatedPublishDate().getMonthOfYear()), 2, "0");
		String e = Utils.urlize(post.getTitle());
		return new Object[] { f, y, m, e };
	}
	
	public static Object[] getContext(Label label) {
		String l = Utils.urlize(label.getName());
		return new Object[] { l };
	}

	public static String urlize(String text) {
		return Utils.trasnliterate(text.toLowerCase().replaceAll("[\\p{Space}¡[\\p{Punct}&&[^-]]]", " ").replaceAll("[\\p{Punct}&&[^-]]", "").replaceAll("[\\p{Space}]", "-").replaceAll("-+", "-").replaceAll("^-+", "").replaceAll("-+$", ""));
	}
	
	public static String trasnliterate(String s) {
		try {
			Process p = Runtime.getRuntime().exec("iconv -f UTF-8 -t ASCII//TRANSLIT");
			Writer w = new OutputStreamWriter(p.getOutputStream());
			Reader r = new InputStreamReader(p.getInputStream());
			IOUtils.copy(new StringReader(s), w);
			w.close();

			Writer sw = new StringWriter();
			IOUtils.copy(r, sw);
			r.close();

			return sw.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getUrl(MainService service, Post post) {
		return service.getPageRenderLinkSource().createPageRenderLinkWithContext(info.blogstack.pages.Post.class, getContext(post)).toAbsoluteURI();
	}
}