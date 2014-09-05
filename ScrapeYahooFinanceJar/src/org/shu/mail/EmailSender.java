package org.shu.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
	 
public class EmailSender {
	
	Properties props = new Properties();
	Session session = null;
	
	public static void main(String[] args){
		EmailSender sender = new EmailSender();
		sender.sendEmail("shu.ghosh@gmail.com", "test", "test");
	}
	
	public EmailSender(){		
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");		
		setSession();			
	}
	
	private void setSession(){
		session = Session.getDefaultInstance(props,	new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("stocktipsfromshu","StockEmails");
			}
		});	
		
	}
	
	public void sendEmail(String recipients, String subject, String content){
		try {
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from@no-spam.com"));
			//message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse("shu.ghosh@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse(recipients));			
			message.setSubject(subject);
			message.setText(content);
			
			Transport.send(message); 
			System.out.println("Email message sent");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}



