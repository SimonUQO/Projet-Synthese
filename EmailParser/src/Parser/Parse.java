package Parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Classe principale de l'application pour lancer le parseur
 * Contient notre classe main lancé à l'exécution
 */
public class Parse {

	public static void main(String[] args) throws MessagingException, IOException {
		String fichier = "/Users/simon/Desktop/test2.eml"; //Chemin du fichier à parser
		Session s = Session.getInstance(new Properties()); 
		InputStream mailFileInputStream = new FileInputStream(fichier); //Création d'un FileInputStream avec notre fichier
		MimeMessage message = new MimeMessage(s, mailFileInputStream); //Instanciation de l'objet à parser
		
		MailList mail = new MailList(); //Création d'une liste de courriels
		mail = mail.getContent(message); //Ajout de notre courriel à la liste
		
		System.out.println(mail.date);
		System.out.println(mail.from);
		System.out.println(mail.subject);
		System.out.println(mail.body);
		System.out.println(mail.attachments.isEmpty());
		
	}
}