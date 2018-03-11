package Parser;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.jsoup.Jsoup;

public class MailList {

	String from;
	String subject;
	String body;
	String date;
	ArrayList<MimeBodyPart> attachments;
	
	public MailList(){
		
	}

	public MailList(String from, String subject, String body, String date, ArrayList<MimeBodyPart> attachments) {
		this.from = from;
		this.subject = subject;
		this.body = body;
		this.date = date;
		this.attachments = attachments;
	}

	public MailList getContent(MimeMessage message) throws MessagingException, IOException {
		String body = "";
		String from = "";
		ArrayList<MimeBodyPart> attachments = new ArrayList<MimeBodyPart>();
		String contentType = message.getContentType();
		Address[] addresses = message.getFrom();
		if (addresses.length == 1)
			from = addresses[0].toString();
		else {
			for (int num = 0; num < addresses.length - 1; num++)
				from += addresses[num].toString() + ", ";
			from += addresses[addresses.length].toString();
		}
		if (contentType.contains("TEXT/PLAIN") || contentType.contains("text/plain")) {
			Object content = message.getContent();
			if (content != null)
				body += content.toString();
		} else if (contentType.contains("TEXT/HTML") || contentType.contains("text/html")) {
			Object content = message.getContent();
			if (content != null)
				body += Jsoup.parse((String) content).text();
		} else if (contentType.contains("multipart")) {
			Multipart mp = (Multipart) message.getContent();
			int numParts = mp.getCount();
			for (int count = 0; count < numParts; count++) {
				MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(count);
				String content = part.getContent().toString();
				if (MimeBodyPart.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
					attachments.add(part);
				else if (part.getContentType().contains("TEXT/HTML") || contentType.contains("text/html"))
					body += Jsoup.parse(content).text();
				else
					body += content;
			}
		}
		return new MailList(from, message.getSubject(), body, message.getSentDate().toString(), attachments);
	}
}
