package intnet13.project.contacts;

import java.io.Serializable;


public class SQLStatement implements Serializable {
	
	private String type;
	private String query;
	private String table;
	private String restriction;
	private int id;
	private String[] values;
	
	public SQLStatement (String a, String b, String c, String d) {
		type = a;
		query = b;
		table = c;
		restriction = d;
		values = null;
	}
	
	public SQLStatement (String a, String b, int id) {
		type = a;
		table = b;
		this.id = id;
		restriction = null;
		values = null;
	}
	
	public SQLStatement (String a, String b, String[] newValues) {
		type = a;
		table = b;
		restriction = null;
		int offset;
		if(newValues.length == 2) {
			values = new String[2];
			offset = 0;
		}
		else {			
			values = new String[newValues.length+1];
			offset = 1;
		}
		for (int i = 0; i<newValues.length; i++) {
			values[i+offset] = newValues[i];
		}
	}
	
	
	public String getType() {
		return type;
	}
	public String getQuery() {
		return query;
	}
	public String getTable() {
		return table;
	}
	public String getRestriction() {
		return restriction;
	}
	
	public void setID(int id) {
		values[0] = Integer.toString(id);
	}
	public String[] getValues() {
		return values;
	}
	public String getValueString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i< values.length; i++) {
			if(i != 0)
				sb.append(",");
			sb.append("'");
			sb.append(values[i]);
			sb.append("'");
		}
		sb.append(")");
		return sb.toString();
	}
	public String getStatement() {
		String res;
		if (restriction != null)
			res = type + " " + query + " " + table + " " + restriction;
		else if(values != null)
			res = type + " " + table + " VALUES " + getValueString();
		else if(type.equals("DELETE FROM"))
			return getDeleteStatement();
		else
			res = type + " " + query + " " + table;
		return res;
	}
	
	private String getDeleteStatement() {
		String res;
		if (table.equals("contact"))
			res = "DELETE FROM contact where contact_id=" + id;
		else
			res = "DELETE FROM groups where group_id=" + id;
		return res;
	}
	
	public String toString() {
		return getStatement();
	}
}
