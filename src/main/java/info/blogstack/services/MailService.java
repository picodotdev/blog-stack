package info.blogstack.services;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.transaction.annotation.Transactional;

public interface MailService {

	@Transactional(readOnly = true)
	void sendNewsletter(String subject, String content) throws AddressException, MessagingException;
}