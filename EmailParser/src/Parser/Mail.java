package Parser;

import javax.mail.Address;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.jsoup.Jsoup;

/**
 * Classe permettant de garder une liste de courriels et pouvoir les parser
 *
 */
public class Mail {
	
	String date;
	String sourceIP;
	String sourceDomain;
	String from;
	String to;
	String subject;
	String body;
	String attachment;
	String encoding;
	String ville;
	String pays;
	String geopoint;
	
	public Mail(){
		
	}

	//Constructeur principale
	public Mail(String sourceIP, String sourceDomain, String from, String to, String subject, String body, String date, String attachment, String encoding, String pays, String ville, String geopoint) {
		this.sourceIP = sourceIP;
		this.sourceDomain = sourceDomain;
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.body = body;
		this.date = date;
		this.attachment = attachment;
		this.encoding = encoding;
		this.ville = ville;
		this.pays = pays;
		this.geopoint = geopoint;
	}

	//Méthode permettant de séparer les champs principales d'un courriel:
	//Body, From, Date, Subject & Attachments
	public Mail getContent(MimeMessage message) throws Exception {
		String body = ""; //Initialisation
		String from = "";
		String to = "";
		String subject = "";
		String date = "";
		String sourceIP = "";
		String sourceDomain = "";
		String encoding = "";
		String attachment = "Aucun attachement";
		String pays = "";
		String ville = "";
		String geopoint = "";
		double lon = 0;
		double lat = 0;
		
		//ArrayList<MimeBodyPart> attachments = new ArrayList<MimeBodyPart>(); //Création d'une liste pour les attachements
		String contentType = message.getContentType(); //Variable permettant d'identifer le type de contenu lu
		Address[] addressesFrom = message.getFrom(); //Tableau contenant les addresses courriels de l'auteur du courriel
		Address[] addressesTo = message.getAllRecipients(); //Tableau contenant les addresses courriels de l'auteur du courriel
		
		sourceIP = message.getHeader("Received", "");
		int debut = 0;
		int fin = 0;
		for(int i = 0; i < sourceIP.length(); i++){
			if(sourceIP.charAt(i) == '['){
				debut = i + 1;
			} else if(sourceIP.charAt(i) == ']'){
				fin = i;
				sourceIP = sourceIP.substring(debut, fin);
			}
		}
		
		Geo g = new Geo(sourceIP);
		pays = g.pays;
		ville = g.ville;
		lon = g.lon;
		lat = g.lat;
		geopoint = (lat + "," + lon);
		
		sourceDomain = message.getHeader("Received", "");
		debut = 0;
		fin = 0;
		for(int i = 0; i < sourceDomain.length(); i++){
			if(sourceDomain.charAt(i) == '('){
				debut = i + 1;
			} else if(sourceDomain.charAt(i) == ')'){
				fin = i;
				sourceDomain = sourceDomain.substring(debut, fin);
			}
		}
		
		//On parse les Adresses TO qui provient d'une seule source
		if (addressesTo.length == 1)
			to = addressesTo[0].toString();
		//Provient de plusieurs sources
		else {
			to = null;
		}
		//On parse les Adresses FROM qui provient d'une seule source
		if (addressesFrom.length == 1)
			from = addressesFrom[0].toString();
		//Provient de plusieurs sources
		else {
			for (int num = 0; num < addressesFrom.length - 1; num++)
				from += addressesFrom[num].toString() + ", ";
			from += addressesFrom[addressesFrom.length].toString();
		}
		//On parse le Sujet
		if (message.getSubject() != null){
			subject = message.getSubject();
		}
		//On parse la Date
		if (message.getSentDate() != null){
			date = message.getSentDate().toString();
		}
		
		//Courriel formaté en texte simple
		if (contentType.contains("TEXT/PLAIN") || contentType.contains("text/plain")) {
			Object content = null;
			try{
				content = message.getContent();
			} catch(Exception e){
				
			}
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
				String content = "";
				try{
					content = part.getContent().toString();
				} catch(Exception e){
					content = "";
					System.out.println(e.toString());
				}
					//Quand on identifie un attachement, on l'ajoute
					if (MimeBodyPart.ATTACHMENT.equalsIgnoreCase(part.getDisposition())){
						attachment = part.getFileName();
						encoding = part.getEncoding();
					}
					//Sinon, on ajoute le reste
					else if (part.getContentType().contains("TEXT/HTML") || contentType.contains("text/html"))
						body += Jsoup.parse(content).text();
					else
						body += Jsoup.parse(content).text();
				}	
			}
		return new Mail(sourceIP, sourceDomain, from, to, subject, body, date, attachment, encoding, pays, ville, geopoint);
	}
}
