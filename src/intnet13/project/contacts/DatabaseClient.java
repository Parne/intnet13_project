package intnet13.project.contacts;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class DatabaseClient {
	private PrintWriter output;
	private BufferedReader input;
	private Socket s;
	private String host, user, password;
	private int port;
	private HashMap<String, String[]> contacts;
	private HashMap<String, String[]> groups;
	private HashMap<String, ArrayList<String>> contacts_in_group;
	
	public DatabaseClient(String host, int port, String user, String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	private void init() {
		contacts = new HashMap<String, String[]>();
		groups = new HashMap<String, String[]>();
		contacts_in_group = new HashMap<String, ArrayList<String>>();
		loadContacts();
	}
	
	public int authenticate() {
		int[] response;
		response = query("1", null);
		if (response[0]== 1) {
			System.out.println("Authenticated!");
			init();
			return 1;
		}
		else if (response[0] == -1) {
			System.out.println("Connection to db failed");
			return -1;
		}
		System.out.println("Access denied!");
		return 0;
	}
	
	private boolean loadGroups() {	
		int[] response;
		response = query("6", null);
		if(response[0] != 1) {
			System.out.println("Data request failed");
			if(response[0] == 3)
				System.out.println("Access denied: Invalid user and/or password!");
			return false;
		}		
		for (int i = 0; i<response[1]/3; i++) {
			try {
				String g_name = input.readLine();
				String g_desc = input.readLine();
				String g_id = input.readLine();
				addGroup(g_name, g_desc, g_id);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private void loadContacts() {
		if(!loadGroups()) //Reqest to get groups failed
			return;
		int[] response;
		response = query("2", null);
		if(response[0] != 1) {
			System.out.println("Data request failed");
			if(response[0] == 3)
				System.out.println("Access denied: Invalid user and/or password!");
			return;
		}		
		for (int i = 0; i<response[1]/5; i++) {
			try {
				String c_name = input.readLine();
				String c_phone = input.readLine();
				String c_email = input.readLine();
				String c_id = input.readLine();
				String g_name = input.readLine();
				addContact(c_name, c_phone, c_email, c_id);
				addContactInGroup(g_name, c_name);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addContact(String name, String phone, String email, String id) {
		// If contact is new
		if(!contacts.containsKey(name)) {
			String[] arr = new String[3];
			arr[0] = phone;
			arr[1] = email;
			arr[2] = id;
			contacts.put(name, arr);
		}
	}
	
	private void addGroup(String name, String desc, String id) {
		// If group is new
		if(!groups.containsKey(name)) {
			String[] arr = new String[2];
			arr[0] = desc;
			arr[1] = id;
			groups.put(name, arr);
		}
	}
	
	private void addContactInGroup(String g_name, String c_name) {
		ArrayList<String> arr;
		// If group is new
		if(!contacts_in_group.containsKey(g_name)) {
			arr = new ArrayList<String>();
			arr.add(c_name);
			contacts_in_group.put(g_name, arr);
		}
		else {
			arr = contacts_in_group.get(g_name);
			arr.add(c_name);
		}
	}
	
	public boolean removeContact(String name) {
		if(!contacts.containsKey(name)) {
			System.out.println("Contact doesn't exist!");
			return false;
		}
		int[] response;
		String[] options = new String[1];
		options[0] = contacts.get(name)[2];
		response = query("3",  options);
		if(response[0] == 1) {
			contacts.remove(name);
			// Remove contact's membership in all groups
			Iterator at = contacts_in_group.entrySet().iterator();
			ArrayList<String> contact_in_group;
			String current;
		    while (at.hasNext()) {
		        Map.Entry pairs = (Map.Entry)at.next();
		        contact_in_group = (ArrayList<String>) pairs.getValue();
		        for(int i = 0; i<contact_in_group.size(); i++) {
		        	current = contact_in_group.get(i);
		        	if(current.equals(name))
		        		contact_in_group.remove(i);
		        }
		    }
		}
		else {
			System.out.println("Connection to db failed");
			return false;
		}
		return true;
	}
	
	public void removeGroup(String name) {
		if(!groups.containsKey(name)) {
			System.out.println("Error tried to remove group: Group does not exist");
			return;
		}
		String[] options = new String[1];
		options[0] = groups.get(name)[1];
		int[] response = query("7", options);
		if (response[0] != 1) {
			System.out.println("Failed to remove group in external db");
			return;
		}
		groups.remove(name);
		contacts_in_group.remove(name);
	}
	
	private boolean saveGroup(String[] options) {
		if(groups.containsKey(options[0])) {
			System.out.println("Failed to save group: Group already exists");
			return false;
		}
		int[] response;
		response = query("5", options);
		if (response[0] != 1) {
			System.out.println("Failed to save group to external db");
			return false;
		}
		addGroup(options[0], options[1], Integer.toString(response[1]));
		return true;
	}
	
	public boolean saveContact(String contactName, String phoneNumber, String email,
			String group) {
		if(contacts.containsKey(contactName))
			return false;
		if(!groups.containsKey(group)) {
			String[] options = new String[2];
			options[0] = group;
			options[1] = group + " gruppen";
			if(!saveGroup(options))
				return false;
		}
		String g_id = groups.get(group)[1];
		String[] options = new String[5];
		options[0] = contactName;
		options[1] = phoneNumber;
		options[2] = email;
		options[3] = g_id;
		options[4] = groups.get("Alla")[1]; 
		int[] response;
		response = query("4",  options);
		if(response[0] != 1) {
			System.out.println("Failed to save contact to external db");
			return false;
		}
		addContact(contactName, phoneNumber, email, Integer.toString(response[1]));
		addContactInGroup(group, contactName);
		if(!group.equals("Alla")) 
			addContactInGroup("Alla", contactName);
		return true;
	}
	
	private boolean openConnection() {
		try {
	    	s = new Socket(host, port);
			output = new PrintWriter(s.getOutputStream());
			input = new BufferedReader(new InputStreamReader(s.getInputStream()));
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int[] query(String type, String[] options) {
		if(!openConnection())
			return new int[]{-1};
		output.println(type);
		output.println(user);
		output.println(password);
		if(options!= null) {
			for(String out : options)
				output.println(out);
		}
		output.flush();
		return receiveMessage();
	}
	
	private int[] receiveMessage() {
		int[] response = new int[2];
		response[0] = -1;
		response[1] = -1;
		try {
			response[0] = Integer.parseInt(input.readLine());
			response[1] = Integer.parseInt(input.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public String[] getGroups() {
		String[] res = new String[groups.size()];
		Iterator it = groups.entrySet().iterator();
		List<String> groupList = new ArrayList<String>();
		res[0] = "Alla";
		String currentGroup;
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        currentGroup = (String) pairs.getKey();
	        if(!currentGroup.equals("Alla"))
	        	groupList.add(currentGroup);
	    }
	    sortList(groupList);
	    for(int i = 0; i< groupList.size(); i++) 
	    	res[i+1] = groupList.get(i);
		return res;
	}
	
	public String[] getContacts() {
		return search("", "Alla");
	}
	
	public String[] search(String contactName, String groupName) {
		ArrayList<String> contacts = contacts_in_group.get(groupName);
		if(contacts == null)
			return new String[]{""};
		List<String> found = new ArrayList<String>();
		contactName = contactName.toLowerCase();
		for (String a : contacts) {
			if(a.toLowerCase().contains(contactName))
				found.add(a);
		}
		sortList(found);
		String[] res = new String[found.size()];
		for (int i = 0; i<found.size(); i++) {
			res[i] = found.get(i);
		}
		return res;
	}
	public String[] getByGroup(String groupName) {
		if(groupName.equals("Alla"))
			return getContacts();
		if(!groups.containsKey(groupName))
			return new String[]{""};
		List<String> groupMembers = contacts_in_group.get(groupName);
		if(groupMembers == null)
			return new String[]{""};
		String[] res = new String[groupMembers.size()];
		sortList(groupMembers);
		for (int i = 0; i<groupMembers.size(); i++) {
			res[i] = groupMembers.get(i);
		}
		return res;
	}
	

	public String[] getContactInfo(String contactName) {
		if(!contacts.containsKey(contactName))
			return new String[]{""};
		String[] info = contacts.get(contactName);
		String[] res = new String[info.length+1];
		res[0] = contactName;
		res[1] = info[0];
		res[2] = info[1];
		res[3] = getMembership(contactName);
		return res;
	}
	
	// Return all groups a contact belongs to
	private String getMembership(String name) {
		String currentGroup;
		Iterator it = contacts_in_group.entrySet().iterator();
		ArrayList<String> currentMembers;
		List<String> groupList = new ArrayList<String>();		
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        currentGroup = (String) pairs.getKey();
	        currentMembers = contacts_in_group.get(currentGroup);
	        for (String a : currentMembers) {
	        	if(a.equals(name))
	        		groupList.add(currentGroup);
	        }
	    }
	    sortList(groupList);
	    String[] res = new String[groupList.size()];
	    for(int i = 0; i< groupList.size(); i++) 
	    	res[i] = groupList.get(i);
	    if(groupList.size() == 1)
	    	return res[0];
		return res[1];
	}
	
	private void sortList(List<String> list) {
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				o1 = o1.toLowerCase();
				o2 = o2.toLowerCase();
				int length = o1.length() > o2.length() ? o2.length() : o1.length();
				for (int i = 0; i<length; i++) {
					if (o1.charAt(i) < o2.charAt(i))
						return -1;
					if (o1.charAt(i) > o2.charAt(i))
						return 1;
				}
				return 0; }
			});
	}
}
