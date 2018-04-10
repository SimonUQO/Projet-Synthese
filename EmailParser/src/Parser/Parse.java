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

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;



/**
 * Classe principale de l'application pour lancer le parseur
 * Contient notre classe main lancé à l'exécution
 */
public class Parse {

	public static void main(String[] args) throws Exception, NullPointerException{

		File fichierJSON = new File("/Users/simon/Desktop/Courriels/data/datauqah.json");
		PrintWriter pw = new PrintWriter(fichierJSON); 
		StringBuilder sb = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper(); //Permet d'écrire des objets en JSON
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY); //Permet la visiblité de nos propriétés
		
		MailList ml = new MailList();
		final File dossier = new File("/Users/simon/Desktop/Test"); //Chemin du dossier où sont les courriels
		listCourrielsDossier(dossier, ml); //Ajouter tous les courriels du dossier à la liste
		
		//Pour chaque courriel, on crée un objet MimeMessage et on le parse
		for (int i = 0; i < ml.listMail.size(); i++){
			String fichier = "/Users/simon/Desktop/Test/";
			fichier += ml.listMail.get(i);
			System.out.println(fichier);
			MimeMessage message = chargerMessage(fichier);
//			try{
				Mail mail = new Mail(); //Création d'un courriel parsé
				mail = mail.getContent(message);
				ml.ajoutMail(mail); //Ajout de notre courriel à la liste
				ecrireFichierJSON(sb, pw, mapper, fichierJSON, ml, i);
//			} catch(Exception e) {
//				System.out.println("Fichier Non Parsé!");
//				System.out.println(e.toString());
//			}		
		}
	    pw.write(sb.toString());
	    pw.flush();
		pw.close();
		System.out.println("done!");
	}
	
	//Création de l'objet MimeMessage
	public static MimeMessage chargerMessage(String fichier) throws MessagingException, FileNotFoundException{
		Session s = Session.getInstance(new Properties()); 
		InputStream mailFileInputStream = new FileInputStream(fichier); //Création d'un FileInputStream avec notre fichier
		return new MimeMessage(s, mailFileInputStream); //Instanciation de l'objet à parser
	}
	
	public static void ecrireFichierJSON(StringBuilder sb, PrintWriter pw, ObjectMapper mapper, File fichier, MailList ml, int i) throws JsonGenerationException, JsonMappingException, IOException{  
	    // Writing to a file
		sb.append("{\"index\":{\"_index\":\"mailuqah\",\"_type\":\"mail\",\"_id\":" + i + "}}");
		sb.append('\n');
		sb.append(mapper.writeValueAsString(ml.list.get(i))); 
	    sb.append('\n');  
	}
	
	//Ajouter nos courriels à notre MailList
	public static void listCourrielsDossier(final File dossier, MailList ml) {
		for (final File fichier : dossier.listFiles()) {
	        if (fichier.isDirectory()) {
	            listCourrielsDossier(fichier, ml);
	        } else {
	            ml.listMail.add((fichier.getName())); //ajout du nom du courriel dans notre liste
	        }
	    }
	}
}