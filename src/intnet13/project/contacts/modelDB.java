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

	public String[] getGroups() {
		// TODO Auto-generated method stub
		//(String[]);
		String[] temp = {"Mat", "Jobb","Skola"};
		return temp;
	}

	public String[] getContacts() {
		// TODO Auto-generated method stub
		return null;
	}

}
