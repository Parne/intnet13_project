package intnet13.project.contacts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import android.widget.EditText;

public class modelDB implements Serializable {
	private String user;
	
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
		// TODO Auto-generated method stub
		String[] temp = {"Alla", "Mat", "Jobb","Skola"};
		return temp;
	}

	public String[] getContacts() {
		// TODO Auto-generated method stub
		String[] temp = {"Pålsson", "Tard","Noob"};
		return temp;
	}

	public String[] search(String contactName) {
		// TODO Auto-generated method stub
		return new String[]{contactName};
	}
	
	/**
	 * Get contacts by group. Returns all groups if string = "Alla" 
	 * @param string
	 * @return
	 */
	public String[] getByGroup(String groupName) {
		if(groupName.equals("Alla"))
			return getContacts();
		String[] temp = {groupName+"1", groupName+"2", groupName+"3"};
		return temp;		
	}

	public void remove(String name) {
		// TODO Auto-generated method stub
		
	}

	public void saveContact(String name, String phoneNumber, String email,
			String group) {
		// TODO Auto-generated method stub
		
	}

	public String[] getContactInfo(String contactName) {
		// TODO Auto-generated method stub
		return new String[]{contactName, "070-0707070", "test@noob.com"};
	}

}
