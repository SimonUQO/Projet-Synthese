package Parser;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.jsoup.Jsoup;

/**
 * Classe permettant de garder une liste de courriels et pouvoir les parser
 *
 */
public class Mail {

	String from;
	String subject;
	String body;
	String date;
	ArrayList<MimeBodyPart> attachments;
	
	public Mail(){
		
	}

	//Constructeur principale
	public Mail(String from, String subject, String body, String date, ArrayList<MimeBodyPart> attachments) {
		this.from = from;
		this.subject = subject;
		this.body = body;
		this.date = date;
		this.attachments = attachments;
	}

	//Méthode permettant de séparer les champs principales d'un courriel:
	//Body, From, Date, Subject & Attachments
	public Mail getContent(MimeMessage message) throws MessagingException, IOException {
		String body = ""; //Initialisation
		String from = "";
		ArrayList<MimeBodyPart> attachments = new ArrayList<MimeBodyPart>(); //Création d'une liste pour les attachements
		String contentType = message.getContentType(); //Variable permettant d'identifer le type de contenu lu
		Address[] addresses = message.getFrom(); //Tableau contenant les addresses courriels de l'auteur du courriel
		
		//Provient d'une seule source
		if (addresses.length == 1)
			from = addresses[0].toString();
		//Provient de plusieurs sources
		else {
			for (int num = 0; num < addresses.length - 1; num++)
				from += addresses[num].toString() + ", ";
			from += addresses[addresses.length].toString();
		}
		//Courriel formaté en texte simple
		if (contentType.contains("TEXT/PLAIN") || contentType.contains("text/plain")) {
			Object content = message.getContent();
			if (content != null)
				body += content.toString(); 
		//Courriel formaté en HTML
		} else if (contentType.contains("TEXT/HTML") || contentType.contains("text/html")) {
			Object content = message.getContent();
			if (content != null)
				body += Jsoup.parse((String) content).text();
		//Courriel fomaté en multipart
		} else if (contentType.contains("multipart")) {
			Multipart mp = (Multipart) message.getContent();
			int numParts = mp.getCount();
			for (int count = 0; count < numParts; count++) {
				MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(count);
				String content = part.getContent().toString();
				//Quand on identifie un attachement, on l'ajoute
				if (MimeBodyPart.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
					attachments.add(part);
				//Sinon, on ajoute le reste
				else if (part.getContentType().contains("TEXT/HTML") || contentType.contains("text/html"))
					body += Jsoup.parse(content).text();
				else
					body += content.replace("\n", "").replace("\r", ""); //+= content
			}
		}
		return new Mail(from, message.getSubject(), body, message.getSentDate().toString(), attachments);
	}
}
