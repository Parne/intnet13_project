package intnet13.project.contacts;

import java.util.ArrayList;
import java.util.Currency;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class Intnet13_projectActivity extends Activity {
	private EditText userName;
	private Button loginButton;
	private ViewSwitcher vs;
	private EditText searchText;
	private Button searchButton;
	private Button addButton;
	private Spinner groupSpinner;	
	
	private  modelDB mdb;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mdb = new modelDB();
        
        vs = (ViewSwitcher)findViewById(R.id.viewSwitcher1);
        userName = (EditText)findViewById(R.id.username);
        loginButton = (Button)this.findViewById(R.id.loginButton);        
        loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(mdb.authenticate(userName.getText().toString(), "password")){
					/*Intent i = new Intent(Intnet13_projectActivity.this, Add_Activity.class);	
					startActivityForResult(i, 0);*/
					vs.showNext();
					fillGroup(mdb.getGroups());
					//fillTable(mdb.getContacts());
				}
				else
					Toast.makeText(Intnet13_projectActivity.this,
							"Fel användarnamn/lösenord",
							Toast.LENGTH_SHORT).show();
				
			}
			
		});
        
        searchText = (EditText)findViewById(R.id.searchText);
        searchButton = (Button)this.findViewById(R.id.searchButton);        
        searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				/*Intent i = new Intent(Intnet13_projectActivity.this, Add_Activity.class);	
				startActivityForResult(i, 0);*/				
				
			}
		});
        
        
        addButton = (Button)this.findViewById(R.id.addButton);        
        addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent i = new Intent(Intnet13_projectActivity.this, Add_Activity.class);	
				startActivityForResult(i, 0);				
				
			}
		});
        
        groupSpinner = (Spinner)this.findViewById(R.id.groupPicker);
        
    }
    
    private void fillTable(ArrayList<String> list){
    	
    }
    
    private void fillGroup(String[] groups) {
    	//TableRow row = new TableRow(this.getApplicationContext());
    	//groupSpinner.addView(child)    	
    	
    	ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, groups);
    	adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	groupSpinner.setAdapter(adapter1);

	}
}

