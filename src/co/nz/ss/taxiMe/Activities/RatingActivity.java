package co.nz.ss.taxiMe.Activities;

import co.nz.ss.taxiMe.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * A rating activity is not currently used
 * 
 * Displays as such
 * @author Niall
 *
 */
public class RatingActivity extends Activity {

	private boolean modified = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ratingsview);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	public void doBack(){
		if(modified){
			showBackDialog();
		}
		else{
			RatingActivity.this.finish();	
		}
	}
	
	public void submit(){
		
	}
	
private void showBackDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.ChangedReturnMessage))
		       .setCancelable(false)
		       .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                RatingActivity.this.finish();
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


