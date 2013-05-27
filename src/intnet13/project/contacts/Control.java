package intnet13.project.contacts;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

/**
 * Flytta all control?
 * @author Jesper
 *
 */
public class Control {
	private Intnet13_projectActivity This;
	private Add_Activity ThisAdd;
	private modelDB mdb;
	
	private ButtonListener bl = new ButtonListener();
	private ListListener ll = new ListListener();
	private SpinnerListener sl = new SpinnerListener();
	private GroupListListener gll = new GroupListListener();
	
	private Control c = this;
	
	public Control(Intnet13_projectActivity _This, modelDB _mdb){		
		This = _This;
		mdb = _mdb;
	}
	
	public void addActivity(Add_Activity _This){		
		ThisAdd = _This;
	}
	
	public ButtonListener getButtonListener(){return bl;}
	public class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()){
			case R.id.loginButton:
				int authenticate = mdb.authenticate(This.ipText.getText().toString(),
						This.userName.getText().toString(), 
						This.password.getText().toString());
				if(authenticate == 1){
					This.vs.showNext();
					This.fillGroup(mdb.getGroups());
					This.fillcontactList(mdb.getContacts());
				}
				else if (authenticate == -1)
					Toast.makeText(This,
							"Kunde inte ansluta till servern",
							Toast.LENGTH_SHORT).show();
				else 
					Toast.makeText(This,
							"Fel anvandarnamn/losenord",
							Toast.LENGTH_SHORT).show();
				break;
			
			case R.id.deleteButton:
				mdb.deleteGroup((String)((TextView) This.groupSpinner.getSelectedView()).getText());
				This.fillGroup(mdb.getGroups());
				This.fillcontactList(mdb.getContacts());
				break;
				
			case R.id.addButton:
				System.out.println("start");
					Add_Activity.mdb = mdb;
					Add_Activity.c = c;
					Intent i = new Intent(This, Add_Activity.class);	
					//i.putExtra("mdb", mdb);
					System.out.println("middle");
					This.startActivityForResult(i, 0);
					System.out.println("done");
					This.fillcontactList(mdb.getContacts());
					This.fillGroup(mdb.getGroups());
				break;
				
			case R.id.searchButton:
				This.fillcontactList(mdb.search(This.searchText.getText().toString(),
						(String)((TextView) This.groupSpinner.getSelectedView()).getText()));
				break;
			
			case R.id.addGroup:
				String group = ThisAdd.groupSpinner.getSelectedItem().toString();
				System.out.println("At pos: " + ThisAdd.groupSpinner.getSelectedItemPosition() + "\nGroup name: "+group);
				if(ThisAdd.groupSpinner.getSelectedItemPosition() == 0 &&
						!ThisAdd.newGroup.getText().toString().equals(""))
					group = ThisAdd.newGroup.getText().toString();
				System.out.println("Group: " + group);				
				ThisAdd.addGroup(group);
				break;
				
			case R.id.editContact:
				ThisAdd.editable(true);
				break;
				
			case R.id.save:				
				ArrayList<String> add = new ArrayList<String>(ThisAdd.groups);
				ArrayList<String> remove = 
						new ArrayList<String>(Arrays.asList(ThisAdd.oldGroups));
				//String[] sortedCurrG = (String[]) ThisAdd.groups.toArray();
				for(String s : ThisAdd.oldGroups)
					add.remove(s);
				for(String s : ThisAdd.groups)
					remove.remove(s);
				
				System.out.println("Tabort: ");
				for(String s: remove)
					System.out.println(s);
				System.out.println("Lägg till:");
				for(String s: add)
					System.out.println(s);
				
				//prep for groupsave;
				if(ThisAdd.oldName == null)
					mdb.saveContact(ThisAdd.name.getText().toString(),
							ThisAdd.phoneNumber.getText().toString(),
							ThisAdd.email.getText().toString(),
							toStringArray(ThisAdd.groups));
				else
					mdb.updateContact(ThisAdd.oldName, ThisAdd.name.getText().toString(),
							ThisAdd.phoneNumber.getText().toString(),
							ThisAdd.email.getText().toString(),
							toStringArray(add),
							toStringArray(remove));
				
				ThisAdd.editable(false);
				This.fillcontactList(mdb.getContacts());
				This.fillGroup(mdb.getGroups());
				ThisAdd.setResult(ThisAdd.RESULT_OK);
				ThisAdd.finish();
				break;		
			
			case R.id.remove:
				mdb.remove(ThisAdd.name.getText().toString());				
				This.fillcontactList(mdb.getContacts());
				This.fillGroup(mdb.getGroups());
				ThisAdd.setResult(ThisAdd.RESULT_OK);
				ThisAdd.finish();
		        break;
			}
		}
		
	}
	
	public ListListener getListListener(){return ll;}
	public class ListListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Add_Activity.mdb = mdb;
			Add_Activity.c = c;
			Intent i = new Intent(This, Add_Activity.class);	
	    	i.putExtra("contactName", (String) ((TextView) arg1).getText());
			This.startActivityForResult(i, 0);
			This.fillcontactList(mdb.getContacts());
		}
	}
	
	public GroupListListener getGroupListListener(){return gll;}
	public class GroupListListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			//ThisAdd.removeGroup(arg2);
			String groupName = ((TextView) arg1).getText().toString();
			ThisAdd.removeGroup(groupName);
		}
	}
	
	public SpinnerListener getSpinnerListener(){return sl;}
	public class SpinnerListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			This.fillcontactList(mdb.search(This.searchText.getText().toString(), 
					((TextView) arg1).getText().toString()));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}		
	}
	
	public String[] toStringArray(ArrayList<String> temp){
		String[] addArray = new String[temp.size()];
		for(int i = 0; i < temp.size();i++)
			addArray[i] = temp.get(i);
		return addArray;
	}

}
