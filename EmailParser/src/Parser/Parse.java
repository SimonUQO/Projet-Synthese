package Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
		String fichier = "/Users/simon/Desktop/Courriels/test5.eml"; //Chemin du fichier à parser
		MailList ml = new MailList();
		
		MimeMessage message = chargerMessage(fichier);
	
		Mail mail = new Mail(); //Création d'un courriel parsé
		mail = mail.getContent(message); 
		
		ml.ajoutMail(mail); //Ajout de notre courriel à la liste
		
		ecrireFichier(ml);
		
	}
	
	
	public static MimeMessage chargerMessage(String fichier) throws MessagingException, FileNotFoundException{
		Session s = Session.getInstance(new Properties()); 
		InputStream mailFileInputStream = new FileInputStream(fichier); //Création d'un FileInputStream avec notre fichier
		return new MimeMessage(s, mailFileInputStream); //Instanciation de l'objet à parser
	}
	
	public static void ecrireFichier(MailList ml) throws FileNotFoundException{
		  PrintWriter pw = new PrintWriter(new File("/Users/simon/Desktop/Courriels/data/data.csv"));
		  StringBuilder sb = new StringBuilder();
	      sb.append('"');
	      sb.append((ml.list.get(0).date));
	      sb.append('"');
	      sb.append(',');
	      
	      sb.append('"');
	      sb.append((ml.list.get(0).from));
	      sb.append('"');
	      sb.append(',');
	      
	      sb.append('"');
	      sb.append((ml.list.get(0).subject));
	      sb.append('"');
	      sb.append(',');
	      
	      sb.append('"');
	      sb.append((ml.list.get(0).body));
	      sb.append('"');
	      sb.append(',');
	      
	      sb.append('"');
	      sb.append((ml.list.get(0).attachments.isEmpty()));
	      sb.append('"');
	      sb.append('\n');

	      pw.write(sb.toString());
	      pw.close();
	      System.out.println("done!");
	}
}