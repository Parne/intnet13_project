package intnet13.project.contacts;

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
 * Flytta all controll?
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
	
	public Control(Intnet13_projectActivity _This){		
		This = _This;
		mdb = new modelDB();
	}
	
	public Control(Add_Activity _This, modelDB _mdb){		
		ThisAdd = _This;
		mdb = _mdb;
	}
	
	public ButtonListener getButtonListener(){return bl;}
	public class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()){
			case R.id.loginButton:
				if(mdb.authenticate(This.userName.getText().toString(), "password")){
					This.vs.showNext();
					This.fillGroup(mdb.getGroups());
					This.fillcontactList(mdb.getContacts());
				}
				else
					Toast.makeText(This,
							"Fel användarnamn/lösenord",
							Toast.LENGTH_SHORT).show();
				break;
			
			case R.id.addButton:
					Intent i = new Intent(This, Add_Activity.class);	
					i.putExtra("mdb", mdb);
					This.startActivityForResult(i, 0);	
				break;
				
			case R.id.searchButton:
				This.fillcontactList(mdb.search(This.searchText.getText().toString()));
				break;
			
			case R.id.editContact:
				ThisAdd.editable(true);
				break;
				
			case R.id.save:
				String group = ThisAdd.groupSpinner.getSelectedItem().toString();
				if(ThisAdd.groupSpinner.getSelectedItemPosition() == 0)
					group = ThisAdd.newGroup.getText().toString();
				System.out.println("Group: " + group);
				mdb.saveContact(ThisAdd.name.getText().toString(),
						ThisAdd.phoneNumber.getText().toString(),
						ThisAdd.email.getText().toString(),
						group);
				ThisAdd.editable(false);
				break;		
			
			case R.id.remove:
				mdb.remove(ThisAdd.name.getText().toString());
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
			Intent i = new Intent(This, Add_Activity.class);	
	    	i.putExtra("mdb", mdb);
	    	i.putExtra("contactName", (String) ((TextView) arg1).getText());
			This.startActivityForResult(i, 0);
		}
	}
	
	public SpinnerListener getSpinnerListener(){return sl;}
	public class SpinnerListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			This.fillcontactList(mdb.getByGroup((String) ((TextView) arg1).getText()));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}		
	}

}
