package intnet13.project.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Add_Activity extends Activity {
	Button searchButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        
        
        searchButton = (Button)this.findViewById(R.id.searchButton);        
        searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
        
        //setResult(RESULT_OK);
        //finish();
	}
}
