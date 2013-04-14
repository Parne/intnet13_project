package intnet13.project.contacts;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class DatabaseClient {
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket s;
	private String host, user, password;
	private int port;
	private ArrayList<ResultData> groups;
	private Map<Integer, ResultData> contacts;
	private Set<Integer> distinct_contacts;
	private ArrayList<Integer> user_groups;
	private ArrayList<Contact_Group_Element> contact_groups;
	
	public DatabaseClient(String host, int port, String user, String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		//if(authenticate()) {
			//saveContact("Pan", "Peter", "Champagne1", "sakah@kth.se", "SU");
			//removeContact("Peter Pan");
			//getByGroup("SU");
		//}
	}
	
	public boolean authenticate() {
		System.out.println("Yolo Auth");
		String auth = "where username='" + user + "' AND password='" + password + "'";
		final SQLStatement sql = new SQLStatement("SELECT", "* FROM", "c_user", auth);
		
		/*(new Thread(new Runnable(){

			@Override
			public void run() {
				query(sql);
			}
			
		})).start();*/
		if(query(sql) == null) {
			System.out.println("Access denied!");
			return false;			
		}
		init();
		return true;
	}
	
	private void init() {
		user_groups = new ArrayList<Integer>();
		contact_groups = new ArrayList<Contact_Group_Element>();
		groups = new ArrayList<ResultData>();
		contacts = new HashMap<Integer, ResultData>();
		distinct_contacts = new TreeSet<Integer>();
		setUserGroups();
		setContactGroups();
		setContacts();	
	}
	
	private void setContacts() {
		int numberOfContacts = distinct_contacts.size();
		if(numberOfContacts == 0) {
			System.out.println("You lack contact_ids");
			return;			
		}
		Iterator<Integer> it=distinct_contacts.iterator();
		int current;
		while(it.hasNext()) {
			current = (Integer) it.next();
			String res = "where contact_id='" + current + "'";
			SQLStatement sql = new SQLStatement("SELECT", "* FROM", "contact", res);
			ResultData rs = query(sql);
			if(rs == null) {
				System.out.println("No match in table:contact for contactID:" + current);
				return;
			}			
			contacts.put(current, rs);
		}
	}
	private void setUserGroups() {
		String res = "where username='" + user + "'";
		SQLStatement sql1 = new SQLStatement("SELECT", "* FROM", "user_groups", res);
		ResultData rs = query(sql1);
		if(rs == null) {
			System.out.println("You have no groups!");
			return;
		}
		ArrayList<ArrayList<ResultObject>> dataCollection = rs.getCollection();
		ArrayList<ResultObject> curr;
		int length = dataCollection.size();
		if(length > 0) {
			for (int i = 0; i<length; i++) {
				curr = dataCollection.get(i);
				user_groups.add((Integer) curr.get(1).getValue());
				String req = "where group_id=" + user_groups.get(i);
				SQLStatement sql2 = new SQLStatement("SELECT", "* FROM", "groups", req);
				groups.add(query(sql2));
			}
		}
	}
	private void setContactGroups() {
		int numberOfGroups = user_groups.size();
		if(numberOfGroups == 0) {
			System.out.println("You lack group_ids");
			return;			
		}
		int group_id, contact_id;
		for (int j = 0; j<numberOfGroups; j++) {
			group_id = user_groups.get(j);
			String res = "where group_id='" + group_id + "'";
			SQLStatement sql = new SQLStatement("SELECT", "* FROM", "contact_groups", res);
			ResultData rs = query(sql);
			if(rs == null) {
				System.out.println("No match in table:contact_groups for groupID:" + group_id);
				return;
			}
			ArrayList<ArrayList<ResultObject>> dataCollection = rs.getCollection();
			ArrayList<ResultObject> curr;
			int length = dataCollection.size();
			for (int i = 0; i<length; i++) {
				curr = dataCollection.get(i);
				contact_id = (Integer) curr.get(1).getValue();
				contact_groups.add(new Contact_Group_Element(group_id, contact_id));
				distinct_contacts.add(contact_id);
			}
		}
	}
	
	private void openConnection() {
		try {
	    	s = new Socket(host, port);
	    	PrintWriter pw = new PrintWriter(s.getOutputStream());
	    	pw.write("TEST");
	    	pw.close();
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void closeConnection() {
		try {
			out.close();
			in.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ResultData query(SQLStatement statement) {
		System.out.println("1");
		openConnection();
		System.out.println("2");
		try {
			out.writeObject(statement);
			out.flush();
			System.out.println("3");
			return receiveQuery();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}
	
	// Return the resultdata if query was successful
	// else return null
	private ResultData receiveQuery() {
		ResultData rs = null;
		try {
			System.out.println("4");
			rs = (ResultData) in.readObject();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("5");
		closeConnection();
		System.out.println("6");
		if (rs.wasSuccessful()) {
			System.out.println("7");
			return rs;
		}
		return null;		
	}
	
	// get contact info, return String[] with info
	// if found, else null
	public String[] getContactInfo(String contactName) {
			int contactID = getContactID(contactName);
			if(contactID == -1) {
				System.out.println("Couldnt find contact ID");
				return null;
			}			
			ResultData contact = contacts.get(contactID);
			ArrayList<ResultObject> currentContact = contact.getRow(0);
			String phone = (String) currentContact.get(3).getValue();
			String email = (String) currentContact.get(4).getValue();
			return new String[]{contactName, phone, email};
	}
	
	// Return id for a contact, if not found -1 is returned
	private int getContactID(String contactName) {
		String currentName;
		int currentID = -1;
		ArrayList<ResultObject> currentContact = null;
		Iterator it = contacts.entrySet().iterator();
		Map.Entry<Integer, ResultData> entry;
		boolean found = false;
	    while(it.hasNext() && !found) {
	    	entry = (Entry<Integer, ResultData>) it.next();
		    ResultData value = entry.getValue();
		    currentContact = value.getRow(0);
		    currentName = (String) currentContact.get(2).getValue() + " " + (String) currentContact.get(1).getValue();
		    if(currentName.equals(contactName)) {
			    currentID = entry.getKey();
		    	found = true;		    	
		    }
	    }
		return currentID;		
	}
	
	// Get all contacts
	public String[] getContacts() {
		String[] result = new String[contacts.size()];
		ArrayList<ResultObject> currentContact = null;
		int i = 0;
		for (Map.Entry<Integer, ResultData> entry : contacts.entrySet()) {
		    ResultData value = entry.getValue();currentContact = value.getRow(0);
		    String currentName = (String) currentContact.get(2).getValue() + " " + 
		    		(String) currentContact.get(1).getValue();
		    result[i] = currentName;
		    i++;
		    System.out.println(currentName);
			}
		return result;
	}
	
	public void removeContact(String contactName) {
		int contactID = getContactID(contactName);
		if(contactID == -1) {
			System.out.println("Couldnt remove contact: couldnt find it");
			return;
		}
		SQLStatement sql = new SQLStatement("DELETE FROM", "contact", contactID);
		ResultData newContact = query(sql);
		if(newContact == null) {
			System.out.println("Something fishy happened when trying to delete a contact");
			return;
		}
		contacts.remove(contactID);
		distinct_contacts.remove(contactID);
		removeContactGroup(contactID);			
	}
	private void removeContactGroup(int id) {
		Contact_Group_Element curr;
		for (int i = 0; i<contact_groups.size(); i++) {
			curr = contact_groups.get(i);
			if(curr.contact_id == id) {
				contact_groups.remove(i);
			}
		}
	}
	
	public void saveContact(String surname, String firstname, String phoneNumber, String email,
			String group)  {
		int groupID = getGroupIDByName(group);
		if(groupID == -1) {
			System.out.println("Couldnt find group when saving contact!");
			return;
		}
		String[] values = new String[]{surname, firstname, phoneNumber, email};
		SQLStatement sql = new SQLStatement("INSERT INTO", "contact", values);
		ResultData newContact = query(sql);
		if(newContact == null) {
			System.out.println("Something fishy happened when trying to save a contact into contact");
			return;
		}
		int contactID = Integer.parseInt((String) newContact.getRow(0).get(0).getValue());
		String[] values1 = new String[]{Integer.toString(groupID), Integer.toString(contactID)};
		SQLStatement sql1 = new SQLStatement("INSERT INTO", "contact_groups", values1);
		ResultData addedContactGroup = query(sql1);
		if(addedContactGroup == null) {
			System.out.println("Something fishy happened when trying to save into contact_group");
			return;
		}
		contacts.put(contactID, newContact);
		distinct_contacts.add(contactID);
		contact_groups.add(new Contact_Group_Element(groupID, contactID));
	}
	
	class Contact_Group_Element {
		public int group_id, contact_id;
		Contact_Group_Element(int a, int b) {
			group_id = a;
			contact_id = b;
		}
		public String toString() {
			return "group_id: " + group_id + " contact_id: " + contact_id; 
		}
	}
	private void debugPrint() {
		System.out.println("\nUserGroups");
		for (int i = 0; i<user_groups.size(); i++)
			System.out.println(user_groups.get(i));
		System.out.println("\nContactGroups");
		for (int i = 0; i<contact_groups.size(); i++)
			System.out.println(contact_groups.get(i));
		
		Iterator it = distinct_contacts.iterator();
		System.out.println("\nDistinct contacts");
        while(it.hasNext())
        {
          System.out.println("Contact: "+ it.next());
        }

		System.out.println("\nGroups");
		for (int i = 0; i<groups.size(); i++)
			System.out.println(groups.get(i));
		
		System.out.println("\nContacts");
		for (Map.Entry<Integer, ResultData> entry : contacts.entrySet()) {
		    int key = entry.getKey();
		    ResultData value = entry.getValue();
		    System.out.println("Key: " + key);
		    System.out.println(value);
		}
	}
	
	public String[] getGroups() {
		String[] result = new String[groups.size()+1];
		result[0] = "Alla";
		ArrayList<ResultObject> currentGroup;
		for (int i = 0; i < groups.size(); i++) {
			currentGroup = groups.get(i).getRow(0);
			String name = (String) currentGroup.get(1).getValue();
			result[i+1] = name;
			System.out.println(name);
		}
		return result;
	}
	
	private int getGroupIDByName(String name) {
		String currentName;
		ResultData currentRow;
		for (int i = 0; i< groups.size(); i++) {
			currentRow = groups.get(i);
			currentName = (String) currentRow.getRow(0).get(1).getValue();
			if (name.equals(currentName))
				return (Integer) currentRow.getRow(0).get(0).getValue();
		}
		return -1;
	}
	public String[] getByGroup(String groupName) {
		ArrayList<String> result = new ArrayList<String>();
		int group_id = getGroupIDByName(groupName);
		Contact_Group_Element curr;
		for (int i = 0; i<contact_groups.size(); i++) {
			curr = contact_groups.get(i);
			if(curr.group_id == group_id) {
				result.add(getContactByID(curr.contact_id));
			}
		}
		String[] temp = {groupName+"1", groupName+"2", groupName+"3"};
		return temp;		
	}
	
	private String getContactByID(int id) {
		ResultData currentContact = contacts.get(id);
		String  currentName = (String) currentContact.getRow(0).get(2).getValue() + " " 
		+ (String) currentContact.getRow(0).get(1).getValue();
		return currentName;
	}

}
