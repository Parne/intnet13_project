package intnet13.project.contacts;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ViewSwitcher;

/**
 * Flytta all controll?
 * @author Jesper
 *
 */
public class Control {
	private EditText userName;
	private Button loginButton;
	private ViewSwitcher vs;
	private EditText searchText;
	private Button searchButton;
	private Button addButton;
	private Spinner groupSpinner;	
	private ListView contactList;
	
	public Control(Intnet13_projectActivity This, EditText _userName,
			Button _loginButton, ViewSwitcher _vs, EditText _searchText,
			Button _searchButton, Button _addButton, Spinner _groupSpinner,
			ListView _contactList){
		
		userName = _userName;
		loginButton = _loginButton;
		vs = _vs;
		searchButton = _searchButton;
		addButton = _addButton;
		groupSpinner = _groupSpinner;
		contactList = _contactList;
	}

}
