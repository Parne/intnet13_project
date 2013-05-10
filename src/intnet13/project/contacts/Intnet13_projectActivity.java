package intnet13.project.contacts;

import java.util.ArrayList;
import java.util.Currency;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class Intnet13_projectActivity extends Activity {
	public EditText userName;
	public EditText password;
	public Button loginButton;
	public ViewSwitcher vs;
	public EditText searchText;
	public Button searchButton;
	public Button addButton;
	public Button deleteButton;
	public Spinner groupSpinner;	
	public ListView contactList;	
	
	private  modelDB mdb;
	
	//TODO: save state, add
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	System.out.println("Yolo Parne!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mdb = new modelDB();
        Control c = new Control(this, mdb);
        
        vs = (ViewSwitcher)findViewById(R.id.viewSwitcher1);
        userName = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)this.findViewById(R.id.loginButton);        
        loginButton.setOnClickListener(c.getButtonListener());        
        searchText = (EditText)findViewById(R.id.searchText);
        searchButton = (Button)this.findViewById(R.id.searchButton);        
        searchButton.setOnClickListener(c.getButtonListener());         
        deleteButton = (Button)this.findViewById(R.id.deleteButton);        
        deleteButton.setOnClickListener(c.getButtonListener());    
        addButton = (Button)this.findViewById(R.id.addButton);        
        addButton.setOnClickListener(c.getButtonListener());        
        groupSpinner = (Spinner)this.findViewById(R.id.groupPicker);
        groupSpinner.setOnItemSelectedListener(c.getSpinnerListener());        
        contactList = (ListView) this.findViewById(R.id.contactList);
        contactList.setOnItemClickListener(c.getListListener());
      
    }
    
    public void fillcontactList(String[] list){
    	ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
    				android.R.layout.simple_list_item_1,
    				list);
    	contactList.setAdapter(arrayAdapter); 
    }
    
    public void fillGroup(String[] groups) {    	
    	ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, groups);
    	adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	groupSpinner.setAdapter(adapter1);	

	}
}

