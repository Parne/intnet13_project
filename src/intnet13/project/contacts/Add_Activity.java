package intnet13.project.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Add_Activity extends Activity {
	EditText name;
	EditText phoneNumber;
	EditText email;
	Spinner groupSpinner;
	EditText newGroup;
	Button editButton;
	Button saveButton;
	Button removeButton;
	
	modelDB mdb;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        
        mdb = (modelDB) getIntent().getSerializableExtra("mdb");        
        
        name = (EditText)this.findViewById(R.id.name);
        phoneNumber = (EditText)this.findViewById(R.id.phoneNumber);
        email = (EditText)this.findViewById(R.id.email);
        groupSpinner = (Spinner) this.findViewById(R.id.groupPickerSinner);
        
        String temp = getIntent().getStringExtra("contactName");
        if(temp != null){
        	name.setText(temp);
        	String[] cInfo = mdb.getContactInfo(temp);
        	phoneNumber.setText(cInfo[1]);
        	email.setText(cInfo[2]);        	
        }
        
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mdb.getGroups());
    	adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	groupSpinner.setAdapter(adapter1);
        newGroup = (EditText)this.findViewById(R.id.newGroupName);
        
        editButton = (Button)this.findViewById(R.id.editContact);        
        editButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editable(true);
			}
		});
        
        saveButton = (Button)this.findViewById(R.id.save);        
        saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String group = ((TextView)groupSpinner.getSelectedItem()).getText().toString();
				if(groupSpinner.getSelectedItemPosition() == 0)
					group = newGroup.getText().toString();
				mdb.saveContact(name.getText().toString(),
						phoneNumber.getText().toString(),
						email.getText().toString(),
						group);
				editable(false);
			}
		});
        
        removeButton = (Button)this.findViewById(R.id.remove);        
        removeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mdb.remove(name.getText().toString());
				setResult(RESULT_OK);
		        finish();
			}
		});
        //setResult(RESULT_OK);
        //finish();
	}
	
	private void editable(boolean edit){
		name.setEnabled(edit);
		phoneNumber.setEnabled(edit);
		email.setEnabled(edit);
		groupSpinner.setEnabled(edit);
		newGroup.setEnabled(edit);
	}
}
