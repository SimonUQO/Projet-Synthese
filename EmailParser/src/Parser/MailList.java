package Parser;

import java.util.ArrayList;

public class MailList {
	
	ArrayList<Mail> list; //liste contenant tous nos objets de courriels
	ArrayList<String> listMail; //liste contenant tous nos noms de fichiers des courriels à parser
	
	public MailList() {
		listMail = new ArrayList<String>();
		list = new ArrayList<Mail>();
	}
	
	public void ajoutMail(Mail mail){
		list.add(mail);
	}
}
