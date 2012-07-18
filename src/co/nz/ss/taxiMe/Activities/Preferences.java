package co.nz.ss.taxiMe.Activities;

import co.nz.ss.taxiMe.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Displays and allows user to edit preferences
 * @author Niall
 *
 */
public class Preferences extends Activity {

	
	private boolean saved = true; //Tracks if current settings has been saved
	private SharedPreferences settings; 
	
	private String tripCheckBoxString;
	private CheckBox tripCheckBox;
	
	private String GPSCheckBoxString;
	private CheckBox GPSCheckBox;
	
	private String NetworkCheckBoxString;
	private CheckBox NetworkCheckBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferencesview);
		
		Resources res = getResources();
		
		settings = getSharedPreferences(res.getString(R.string.Preferences_File), MODE_PRIVATE);
		
		tripCheckBoxString = res.getString(R.string.TripUpdatesBooleanString);
		tripCheckBox = (CheckBox) findViewById(R.id.TripCheckBox);
		tripCheckBox.setChecked(settings.getBoolean(tripCheckBoxString, true));
		
		GPSCheckBoxString = res.getString(R.string.GPSBooleanString);
		GPSCheckBox = (CheckBox) findViewById(R.id.GPSCheckBox);
		GPSCheckBox.setChecked(settings.getBoolean(GPSCheckBoxString, true));
		
		NetworkCheckBoxString = res.getString(R.string.NetworkCheckBoxBooleanString);
		NetworkCheckBox = (CheckBox) findViewById(R.id.NetworkCheckBox);
		NetworkCheckBox.setChecked(settings.getBoolean(NetworkCheckBoxString, true));
		
	}
	
	//Call any time a checkbox is clicked.
	public void checkBoxClicked(View v){
		saved = false;
	}

	
	//Called when back is clicked
	public void doBack(){
		//Check if saved, if not, prompt to save
		if(!saved){
			showNotSavedDialog();
		}
		else{
			//quit
			Preferences.this.finish();
		}
	}
	
	/**
	 * prechecks for saving
	 */
	public void submit(){
		//If already saved, inform user
		if(saved){
		   showToast(getString(R.string.ToastNotSettingsToSaveMessage), Toast.LENGTH_SHORT);
			
		}
		
		boolean useGPS =  GPSCheckBox.isChecked();
		boolean useNetwork = NetworkCheckBox.isChecked();
		
		//Advise user if they have disabled any form of location
		if(!(useGPS || useNetwork)){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.NoConnectionsSet))
			       .setCancelable(false)
			       .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   saveChanges();
			           }
			       })
			       .setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			
			AlertDialog alert = builder.create();
			alert.show();
		}
		else{
			saveChanges();
		}
		
	}
	
	/**
	 * saves settings
	 */
	private void saveChanges(){
		
		//Shows that settings have been saved
		showToast(getString(R.string.SettingsSavedToastString), Toast.LENGTH_SHORT);
		
		SharedPreferences.Editor editor = settings.edit();
		
		//save settings
		editor.putBoolean(GPSCheckBoxString, GPSCheckBox.isChecked());
		editor.putBoolean(tripCheckBoxString, tripCheckBox.isChecked());
		editor.putBoolean(NetworkCheckBoxString, NetworkCheckBox.isChecked());
		editor.commit();
		
		saved = true; //reset flag
	}
	
	/**
	 * Shows a toast with the given message
	 * @param message
	 * @param length
	 */
	private void showToast(String message, int length){
		Toast t = Toast.makeText(getApplicationContext(), message, length);
		t.show();
	}
	
	/**
	 * Shows that the settings have not been saved
	 */
	private void showNotSavedDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.NotSavedReturnMessage))
		       .setCancelable(false)
		       .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                Preferences.this.finish();
		           }
		       })
		       .setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();

	}

}
