package ips.android;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class SettingsActivity extends Activity implements OnItemSelectedListener {
	
	String mapId;
	
	int gridSize;
	
	EditText textField;
	
	Intent newIntent;

	String algoType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.Maps,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);	
		
		Bundle extras = getIntent().getExtras();
		
		//textField = (EditText) findViewById(R.id.editText1);
		
		//textField.setText(gridSize+"", TextView.BufferType.EDITABLE);
		
		newIntent = new Intent();
		
		algoType = "Nearest neighbours";
		
		//textField.setText(6);
	}
	
	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.activity_settings, menu); return true; }
	 */

	@Override
	public void onBackPressed() {		
		saveSettings();
		
		super.onBackPressed();
	}
	
	
	
	@Override
	protected void onStop() {	
		
		saveSettings();
		
		super.onStop();
	}

	private void saveSettings() {
		
		
		newIntent.putExtra("algoType", algoType);

		setResult(RESULT_OK, newIntent);
		
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		algoType = (String) parent.getItemAtPosition(pos);
		
		newIntent.putExtra("algoType", algoType);
		setResult(RESULT_OK, newIntent);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

}
