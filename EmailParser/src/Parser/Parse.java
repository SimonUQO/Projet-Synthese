package Parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class Parse {

	public static void main(String[] args) throws MessagingException, IOException {
		String m = "/Users/simon/Desktop/test2.eml";
		Session s = Session.getInstance(new Properties());
		InputStream mailFileInputStream = new FileInputStream(m);
		MimeMessage message = new MimeMessage(s, mailFileInputStream);
		
		MailList mail = new MailList();
		mail = mail.getContent(message);
		
		System.out.println(mail.date);
		System.out.println(mail.from);
		System.out.println(mail.subject);
		System.out.println(mail.body);
		System.out.println(mail.attachments.isEmpty());
		
	}
}