package intnet13.project.contacts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import android.widget.EditText;

public class modelDB implements Serializable {
	private DatabaseClient myClient;
	public modelDB() {

		myClient = new DatabaseClient("130.237.223.174", 4545, "admin", "admin");
	}
	/**
	 * Ifall r�tt s�tter user;
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean authenticate(String user, String password) {
		//myClient = new DatabaseClient("130.237.223.174", 4545, user, password);
		return myClient.authenticate();
		//return true;
		
	}

	//ifall ArrayList �r att f�redra g�r det att �ndra.
	/**
	 * Returns all groups with "Alla" first.
	 * @return
	 */
	public String[] getGroups() {
		// Done
		//String[] temp = {"Alla", "Mat", "Jobb","Skola"};
		//return temp;
		return myClient.getGroups();
	}

	public String[] getContacts() {
		// Done
		//String[] temp = {"P�lsson", "Tard","Noob"};
		//return temp;
		return myClient.getContacts();
	}

	public String[] search(String contactName) {
		// Done
		return new String[]{contactName};
		
	}
	
	/**
	 * Get contacts by group. Returns all groups if string = "Alla" 
	 * @param string
	 * @return
	 */
	public String[] getByGroup(String groupName) {
		// Done
		if(groupName.equals("Alla"))
			return getContacts();
		//String[] temp = {groupName+"1", groupName+"2", groupName+"3"};
		//return temp;	
		return myClient.getByGroup(groupName);
	}

	public void remove(String name) {
		// Done
		myClient.removeContact(name);
		
	}

	public void saveContact(String surname, String firstname, String phoneNumber, String email,
			String group) {
		// Done
		myClient.saveContact(surname, firstname, phoneNumber, email, group);
		
	}

	public String[] getContactInfo(String contactName) {
		// Done
		//return new String[]{contactName, "070-0707070", "test@noob.com"};
		return myClient.getContactInfo(contactName);
	}

}
