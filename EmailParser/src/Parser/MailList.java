package Parser;

import java.util.ArrayList;

public class MailList {
	
	ArrayList<Mail> list;
	
	public MailList() {
		list = new ArrayList<Mail>();
	}
	
	public void ajoutMail(Mail mail){
		list.add(mail);
	}
}
