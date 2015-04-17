package info.blogstack.services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailServiceImpl implements MailService {

	private static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	private MainService service;

	private Properties sessionProperties;

	public MailServiceImpl(MainService service) {
		this.service = service;

		sessionProperties = new Properties();
		sessionProperties.put("mail.smtp.auth", true);
		sessionProperties.put("mail.smtp.starttls.enable", "true");
		sessionProperties.put("mail.smtp.socketFactory.port", "587");
		sessionProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	}

	@Override
	public void sendNewsletter(String subject, String content) throws AddressException, MessagingException {
		logger.info("Sending newletter with {} characters", content.length());

		String from = (String) service.getConfiguration().get().get("newsletter.from");
		String to = (String) service.getConfiguration().get().get("newsletter.to");

		Session session = Session.getInstance(sessionProperties);

		MimeMessage mm = new MimeMessage(session);

		mm.setFrom(new InternetAddress(from));
		mm.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		// Establecer el contenido del mensaje
		mm.setSubject(subject);
		mm.setContent(content, "text/html; charset=utf-8");

		// Send email
		Transport t = session.getTransport("smtp");
		t.connect("smtp.gmail.com", 587, (String) service.getConfiguration().get().get("newsletter.user"), (String) service.getConfiguration().get()
				.get("newsletter.password"));
		t.sendMessage(mm, mm.getAllRecipients());
		t.close();
	}
}