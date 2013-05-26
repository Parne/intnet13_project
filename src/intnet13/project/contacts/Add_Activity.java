package intnet13.project.contacts;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Add_Activity extends Activity {
	public EditText name;
	public EditText phoneNumber;
	public EditText email;
	public Spinner groupSpinner;
	public EditText newGroup;
	public Button addGroupButton;
	public Button editButton;
	public Button saveButton;
	public Button removeButton;
	public ListView currentGroups;
	
	public static modelDB mdb;
	public static Control c;
	public String oldName = null;
	private ArrayAdapter<String> glistAdapter;
	public ArrayList<String> groups = new ArrayList<String>();
	public String[] oldGroups = new String[]{};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        
        System.out.println("in:start");
        //mdb = new modelDB();//(modelDB) getIntent().getSerializableExtra("mdb");
         System.out.println("in:1");
        c.addActivity(this);
        System.out.println("in:2");
        
        name = (EditText)this.findViewById(R.id.name);
        phoneNumber = (EditText)this.findViewById(R.id.phoneNumber);
        email = (EditText)this.findViewById(R.id.email);
        groupSpinner = (Spinner) this.findViewById(R.id.groupPickerSinner);  
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mdb.getGroups());
    	adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	groupSpinner.setAdapter(adapter1);
        newGroup = (EditText)this.findViewById(R.id.newGroupName);

        addGroupButton = (Button)this.findViewById(R.id.addGroup);
        addGroupButton.setOnClickListener(c.getButtonListener());   
        currentGroups = (ListView)this.findViewById(R.id.currentGroups);
        currentGroups.setOnItemClickListener(c.getGroupListListener());
        glistAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, groups);
    	currentGroups.setAdapter(glistAdapter);
        editButton = (Button)this.findViewById(R.id.editContact);   
        editButton.setOnClickListener(c.getButtonListener());        
        saveButton = (Button)this.findViewById(R.id.save);        
        saveButton.setOnClickListener(c.getButtonListener());        
        removeButton = (Button)this.findViewById(R.id.remove);        
        removeButton.setOnClickListener(c.getButtonListener());
        
        
        String temp = getIntent().getStringExtra("contactName");
        System.out.println("in:3");
        if(temp != null){
        	oldName = temp;
        	name.setText(temp);
        	String[] cInfo = mdb.getContactInfo(temp);
        	
        	phoneNumber.setText(cInfo[1]);
        	email.setText(cInfo[2]);
        	
        	//fillGroupList(new String[]{"KTH", "SU"});
        	oldGroups = Arrays.copyOfRange(cInfo, 3, cInfo.length);
        	fillGroupList(oldGroups);
        	//groupSpinner.setSelection(adapter1.getPosition(cInfo[3]));

            editable(false);
        }
        else{
        	editButton.setEnabled(false);
        	removeButton.setEnabled(false);
        }
        System.out.println("in:4");
        
	}
	
	public void editable(boolean edit){
		name.setEnabled(edit);
		phoneNumber.setEnabled(edit);
		email.setEnabled(edit);
		groupSpinner.setEnabled(edit);
		newGroup.setEnabled(edit);
		saveButton.setEnabled(edit);
		removeButton.setEnabled(edit);
		currentGroups.setEnabled(edit);
		addGroupButton.setEnabled(edit);
	}
	
	public void fillGroupList(String[] list){
    	groups.addAll(Arrays.asList(list));
    	glistAdapter.notifyDataSetChanged();
    }
	
	public void removeGroup(int pos){		
		groups.remove(pos);
		glistAdapter.notifyDataSetChanged();
	}
	
	public void removeGroup(String oldGroup){
		groups.remove(oldGroup);
		glistAdapter.notifyDataSetChanged();
	}
	
	public void addGroup(String newGroup){
		groups.add(newGroup);
		glistAdapter.notifyDataSetChanged();
	}
}
