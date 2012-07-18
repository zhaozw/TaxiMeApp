package co.nz.ss.taxiMe.Activities;

import co.nz.ss.taxiMe.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Home, displays options menu
 * @author Niall
 *
 */
public class TaxiMeActivity extends Activity {


	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void findTaxi(final View view){
    	Intent i = new Intent(TaxiMeActivity.this, FindTaxiActivity.class);
    	startActivity(i);
    }
    
    public void rateTaxi(final View view){
    	Intent i = new Intent(TaxiMeActivity.this, RatingActivity.class);
    	startActivity(i);
    }
    
    public void showPreferences(final View view){
    	Intent i = new Intent(TaxiMeActivity.this, Preferences.class);
    	startActivity(i);
    }
    
    public void exit(final View view){
    	showExitDialog();
    }
    
private void showExitDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.ExitQueryString))
		       .setCancelable(false)
		       .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                System.exit(0);
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