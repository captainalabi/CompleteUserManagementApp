package com.alabi.app.utility;


import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailMailSenderService {	
	
	public void sendEmail(String toEmail, String subject, String body)
			throws AddressException, MessagingException {
		
		String username = "captain.alabi@gmail.com";
		String password = "twtvpnumkaurlffj";
		
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("spring.mail.properties.mail.smtp.socketFactory.port", "25");
		//prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
		prop.put("mail.debug", true);		

		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {	
				return new PasswordAuthentication(username, password);
		}	
	};	
	Session session = Session.getInstance(prop, auth);
	
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("EmailDemo"));
		InternetAddress [] addresses = {new InternetAddress(toEmail)};
		msg.setFrom("info@SecuredUserApp.com");
		msg.setRecipients(Message.RecipientType.TO, addresses);
		msg.setSubject(subject);
		msg.addHeader("x-cloudmta-class", "standard");
		msg.addHeader("x-cloudmta-tags", "EmailDemo");
		msg.setText(body, "utf-8", "html");
		
		msg.setSender(new InternetAddress("info@SecuredUserApp.com"));
				
		Transport.send(msg);		
		System.out.println("Email successfully sent");
	
	}
		    
}
