package intnet13.project.contacts;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;

import android.widget.EditText;

public class modelDB implements Serializable {
	private DatabaseClient myClient;
	
	public modelDB() {
	}
	
	public int authenticate(String host, String user, String password) {
		myClient = new DatabaseClient(host, 4545, user, password);
		return myClient.authenticate();	
	}

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

	/**
	 * Search for contacts within a group
	 * @param contactName
	 * @return
	 */
	public String[] search(String contactName, String groupName) {
		return myClient.search(contactName, groupName);
		
	}
	
	/**
	 * Get contacts by group. Returns all groups if string = "Alla" 
	 * @param string
	 * @return
	 */
	public String[] getByGroup(String groupName) {
		if(groupName.equals("Alla"))
			return getContacts();
		return myClient.getByGroup(groupName);
	}

	public void remove(String name) {
		myClient.removeContact(name);		
	}

	public void saveContact(String contactName, String phoneNumber, String email,
			String[] group) {
		myClient.saveContact(contactName, phoneNumber, email, group);		
	}

	public String[] getContactInfo(String contactName) {
		return myClient.getContactInfo(contactName);
	}
	public void deleteGroup(String group) {
		if(group.equals("Alla")) return;
		myClient.removeGroup(group);	
	}

	public void removeContactFromGroup(String name, String group) {
		myClient.addOrRemoveContactInGroup(name, group, true);
		
	}

	public void updateContact(String oldName, String contactName, String phoneNumber, String email,
			String[] addGroups, String[] removeGroups) {
		myClient.updateContact(oldName, contactName, phoneNumber, email, addGroups, removeGroups);
		
	}

}
