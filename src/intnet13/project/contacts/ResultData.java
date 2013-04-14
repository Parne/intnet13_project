package intnet13.project.contacts;

import java.io.Serializable;
import java.util.ArrayList;


public class ResultData implements Serializable{
	
	private ArrayList<ArrayList<ResultObject>> collection;
	private String tableName;
	private boolean succeeded;
	
	public ResultData(String tableName) {
		collection = null;
		this.tableName = tableName;
	}
	public void succeesfulQuery() {
		succeeded = true;
	}
	
	public void notSucceesfulQuery() {
		succeeded = false;
	}
	
	public boolean wasSuccessful() {
		return succeeded;
	}
	
	public void addRow() {
		if(collection == null)
			collection = new ArrayList<ArrayList<ResultObject>>();
		ArrayList<ResultObject> arr = new ArrayList<ResultObject>();
		collection.add(arr);
	}
	
	public void addObject(int row, String column, Object value) {
		ArrayList<ResultObject> arr = collection.get(row);
		arr.add(new ResultObject(column, value));
	}
	
	public ArrayList<ArrayList<ResultObject>> getCollection() {
		return collection;
	}
	
	public ArrayList<ResultObject> getRow(int i) {
		return collection.get(i);
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String table) {
		tableName = table;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("*** " + tableName + " ***");
		for (ArrayList<ResultObject> a : collection) {
			sb.append("\n");
			for (int i = 0; i<a.size(); i++) {
				sb.append(a.get(i).getColumn() + ": ");
				sb.append(a.get(i).getValue() + "\n");
			}
		}	
		sb.append("*** End of " + tableName + " *** \n");
		return sb.toString();
	}


}
