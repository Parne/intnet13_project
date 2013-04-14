package intnet13.project.contacts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class test_db {
	
	
	public void testConnection() throws ClassNotFoundException {
		System.out.println("DB connection trying!");
		Connection con = null;
	    Statement st = null;
	    ResultSet rs = null;
	
	    Class.forName ("org.postgresql.Driver");
	    String url = "jdbc:postgresql://psql-vt2013.csc.kth.se";
	    String user = "hsoh";
	    String password = "dsq59VfTcp";
	
	    try {
	        con = DriverManager.getConnection(url, user, password);
	        st = con.createStatement();
	        rs = st.executeQuery("SELECT * FROM contact");
	
	        if (rs.next()) {
	            System.out.println(rs.getString(1));
	        	}
	    	} 
	    catch (SQLException ex) {
	    		// TODO
	    	System.out.println("DB ERROR");
	    }	
	}
}
