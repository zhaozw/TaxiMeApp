package co.nz.ss.taxiMe.Activities;

import co.nz.ss.taxiMe.R;
import co.nz.ss.taxiMe.CustomViews.LocationSelecter;
import co.nz.ss.taxiMe.CustomViews.LocationSelecter.TripNode;
import co.nz.ss.taxiMe.businessObjects.ConnectionChecker;
import co.nz.ss.taxiMe.exceptions.LocationNotProvidedException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * A FindTaxiActivity displays inputs to assist in location a taxi 
 * @author Niall
 *
 */
public class FindTaxiActivity extends Activity {

	private boolean changed = false;  //Tracks input
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Ensure keypad does not start open
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		setContentView(R.layout.findtaxiview);
		
		//Create location selectors, and set text
		LocationSelecter ls = (LocationSelecter) findViewById(R.id.startLocationSelecter);
		ls.setText(TripNode.Start);
		
		LocationSelecter le = (LocationSelecter) findViewById(R.id.endLocationSelecter);
		le.setText(TripNode.End);
		
	}
	
	/**
	 * Called when go button is pressed.  Checks for valid input and internet connection
	 * and then attempts to open the results in a new activity
	 */
	public void doGo(){
		
		Resources res = getResources();
		
		ConnectionChecker cc = new ConnectionChecker(FindTaxiActivity.this);
		
		//Check for an internet connetion
		if(!cc.checkInternetEnabled()){
			showErrorDialog(res.getString(R.string.NoInternetConnection));
			return;
		}
		
		//Get desired lcoation
		LocationSelecter start = (LocationSelecter) findViewById(R.id.startLocationSelecter);
		LocationSelecter end = (LocationSelecter) findViewById(R.id.endLocationSelecter);
		
		//Start taxi info activity, passing the locations
		try{
			String startString = start.getLocation();
			String endString = end.getLocation();
			
			Intent i = new Intent(FindTaxiActivity.this, TaxiListActivity.class);
			Bundle b = new Bundle();
			b.putString(res.getString(R.string.FromString), startString);
			b.putString(res.getString(R.string.endString), endString);
			i.putExtras(b);
			
			startActivity(i);
		}
		//If either location is empty, show error
		catch(LocationNotProvidedException lnpe){
			showErrorDialog(res.getString(R.string.EmptyStartOrEnd));
		}
		
	}
	
	/**
	 * Called whenback button is pressed
	 */
	public void doBack(){
		//if user has made input, check if they want to quit
		if(changed){
			showBackDialog();
		}
		else{
			FindTaxiActivity.this.finish();
		}
	}

	/**
	 * Check if a user wishes to quit
	 */
	private void showBackDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.ChangedReturnMessage))
		       .setCancelable(false)
		       .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   //Quit
		                FindTaxiActivity.this.finish();
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
	
	/**
	 * Displays an error essage with a single button
	 * @param message
	 */
	private void showErrorDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
		       .setCancelable(false)
		       .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}

}
