package intnet13.project.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Add_Activity extends Activity {
	public EditText name;
	public EditText phoneNumber;
	public EditText email;
	public Spinner groupSpinner;
	public EditText newGroup;
	public Button editButton;
	public Button saveButton;
	public Button removeButton;
	
	modelDB mdb;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        
        mdb = (modelDB) getIntent().getSerializableExtra("mdb");
        Control c = new Control(this, mdb);
        
        name = (EditText)this.findViewById(R.id.name);
        phoneNumber = (EditText)this.findViewById(R.id.phoneNumber);
        email = (EditText)this.findViewById(R.id.email);
        groupSpinner = (Spinner) this.findViewById(R.id.groupPickerSinner);  
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mdb.getGroups());
    	adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	groupSpinner.setAdapter(adapter1);
        newGroup = (EditText)this.findViewById(R.id.newGroupName);
        
        String temp = getIntent().getStringExtra("contactName");
        if(temp != null){
        	name.setText(temp);
        	String[] cInfo = mdb.getContactInfo(temp);
        	phoneNumber.setText(cInfo[1]);
        	email.setText(cInfo[2]);
        	
        	editable(false);
        }
        
        editButton = (Button)this.findViewById(R.id.editContact);        
        editButton.setOnClickListener(c.getButtonListener());/*new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editable(true);
			}
		});*/
        
        saveButton = (Button)this.findViewById(R.id.save);        
        saveButton.setOnClickListener(c.getButtonListener());/*new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String group = groupSpinner.getSelectedItem().toString();
				if(groupSpinner.getSelectedItemPosition() == 0)
					group = newGroup.getText().toString();
<<<<<<< HEAD
				mdb.saveContact(name.getText().toString(), "challe",
=======
				System.out.println("Group: " + group);
				mdb.saveContact(name.getText().toString(),
>>>>>>> 58f5758fc5d0b9f94fde234a771838ba0d2993fe
						phoneNumber.getText().toString(),
						email.getText().toString(),
						group);
				editable(false);
			}
		});*/
        
        removeButton = (Button)this.findViewById(R.id.remove);        
        removeButton.setOnClickListener(c.getButtonListener());/*new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mdb.remove(name.getText().toString());
				setResult(RESULT_OK);
		        finish();
			}
		});
        //setResult(RESULT_OK);
        //finish();*/
	}
	
	public void editable(boolean edit){
		name.setEnabled(edit);
		phoneNumber.setEnabled(edit);
		email.setEnabled(edit);
		groupSpinner.setEnabled(edit);
		newGroup.setEnabled(edit);
	}
}
