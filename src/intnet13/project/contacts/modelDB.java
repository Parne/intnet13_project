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
	 * Ifall rätt sätter user;
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean authenticate(String user, String password) {
		return true;
		
	}

	//ifall ArrayList är att föredra går det att ändra.
	/**
	 * Returns all groups with "Alla" first.
	 * @return
	 */
	public String[] getGroups() {
		return myClient.getGroups();
	}

	public String[] getContacts() {
		return myClient.getContacts();
	}
	
	//
	// Obsolete !!!!
	//
	/**public String[] search(String contactName) {
		return search(contactName, "Alla");		
	}
	*/
	/**
	 * Search for contacts within a group
	 * @param contactName
	 * @return
	 */
	public String[] search(String contactName, String groupName) {
		// Done
		return myClient.search(contactName, groupName);
		
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
		return myClient.getByGroup(groupName);
	}

	public void remove(String name) {
		// Done
		myClient.removeContact(name);		
	}

	// !!! FIX !!! Nar save into Alla, blir group = "",
	// satt group = "Alla"
	public void saveContact(String contactName, String phoneNumber, String email,
			String group) {
		myClient.saveContact(contactName, phoneNumber, email, group);		
	}

	public String[] getContactInfo(String contactName) {
		return myClient.getContactInfo(contactName);
	}
	public void deleteGroup(String group) {
		if(group.equals("Alla")) return;
		System.out.println("Delete:"+ group);		
	}

}
