package intnet13.project.contacts;

import java.util.ArrayList;
import java.util.Collection;

public class modelDB {
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

	public ArrayList<String> getGroups() {
		// TODO Auto-generated method stub
		//(String[]);
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("Mat");
		temp.add("Jobb");
		temp.add("Skola");
		return temp;
	}

	public ArrayList<String> getContacts() {
		// TODO Auto-generated method stub
		return null;
	}

}
