package co.nz.ss.taxiMe.CustomViews;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import co.nz.ss.taxiMe.R;
import co.nz.ss.taxiMe.businessObjects.ConnectionChecker;
import co.nz.ss.taxiMe.exceptions.LocationNotProvidedException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Provides the ability to enter your location, as well as use GPS/Netowrk to enter it for you
 * @author Niall
 *
 */
public class LocationSelecter extends BorderedLinearLayout{

	private static final int TTL = 2 * 60 * 1000; //The amount of time a lastKnowLocation is valid for
	
	private Resources res;
	private TextView title;
	private TextView insertLocationText;
	
	private EditText address;
	private EditText suburb;
	
	private Dialog dialog;
	
	private boolean hasLocation = false;
	private boolean hasGPSLocation = false;
	private boolean hasNetworkLocation = false;
	private boolean errorFlag = false;
	private boolean unableToLocateFlag = false;
	private boolean useGPS;
	private boolean useNetwork;
	
	private String addressString;
	private String suburbString;	
	
	private Location location;
	private Location currentGPSLocation;
	private Location currentNetworkLocation;
	private LocationManager manager;
	private LocationListener networkListener;
	private LocationListener GPSListener;
	
	private Timer timer;
	
	public LocationSelecter(Context context, AttributeSet attrs) {
		super(context, attrs);
		res = getResources();
		
		String GPSString = res.getString(R.string.GPSBooleanString);
		String NetworkString = res.getString(R.string.NetworkCheckBoxBooleanString);
		
		SharedPreferences prefs =  context.getSharedPreferences(res.getString(R.string.Preferences_File), context.MODE_PRIVATE);
		useGPS = prefs.getBoolean(GPSString, true);
		useNetwork = prefs.getBoolean(NetworkString, true);
		
		doUISetup(context);
	}
	
	
	
	public void doUISetup(Context context){
		
		int padding;
		
		//Title
		title = new TextView(this.getContext());
		title.setTextColor(res.getColor(R.color.ViewTextColor));
		title.setTextSize(res.getDimension(R.dimen.SubHeadderSize));
		padding = (int) res.getDimension(R.dimen.HeadderPadding);
		title.setPadding(padding, padding, padding, padding);
		this.addView(title);
		
		//Use My location Button
		Button useMyLocation = new Button(this.getContext());
		useMyLocation.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0){

				doGetLocation();

			}
		});
		
		useMyLocation.setText(res.getString(R.string.findLocationButtonString));
		padding = (int) res.getDimension(R.dimen.FindTaxiButtonPadding);
		useMyLocation.setPadding(padding, padding, padding, padding);
		this.addView(useMyLocation);
		
		//OR text view
		TextView or = new TextView(this.getContext());
		or.setText(res.getString(R.string.Or));
		or.setTextSize(res.getDimension(R.dimen.HeadderSize));
		or.setTypeface(null, Typeface.BOLD);
		or.setGravity(Gravity.CENTER_HORIZONTAL);
		padding = (int)res.getDimension(R.dimen.HeadderPadding);
		or.setPadding(padding, padding, padding, padding);
		this.addView(or);
		
		//View start Location label
		insertLocationText = new TextView(this.getContext());
		insertLocationText.setPadding(padding, padding, padding, padding);
		this.addView(insertLocationText);
		
		//Address Line
		LinearLayout addressLine = new LinearLayout(getContext());
		
		//Address text
		TextView addressTextViewer = new TextView(getContext());
		addressTextViewer.setText(res.getString(R.string.AddressLine));
		padding = (int)res.getDimension(R.dimen.FindTaxiButtonPadding);
		addressTextViewer.setPadding(padding, padding, padding, padding);
		addressLine.addView(addressTextViewer);
		
		//Address input
		address = new EditText(getContext());
		int layout = (int)res.getDimension(R.dimen.BoarderWidth);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(layout, layout, layout, layout);
		address.setLayoutParams(lp);
		addressLine.addView(address);
		
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(address.getWindowToken(), 0);
			
		this.addView(addressLine);
		
		//Suburb line
		LinearLayout suburbLine = new LinearLayout(getContext());
		
		//Suburb text
		TextView suburbTextViewer = new TextView(getContext());
		suburbTextViewer.setText(res.getString(R.string.SuburbLine));
		padding = (int)res.getDimension(R.dimen.FindTaxiButtonPadding);
		suburbTextViewer.setPadding(padding, padding, padding, padding);
		suburbLine.addView(suburbTextViewer);
		
		//Suburb input
		suburb = new EditText(getContext());
		suburb.setLayoutParams(lp);
		suburbLine.addView(suburb);
		
		this.addView(suburbLine);
		
		//Noting
		TextView note = new TextView(getContext());
		note.setText(res.getString(R.string.FindTaxiLocationNote));
		note.setPadding(padding, padding, padding, padding);
		this.addView(note);	
		
	}

	public void setText(TripNode node){
		
		String heading;
		String insert;
		
		if(node==TripNode.Start){
			heading=res.getString(R.string.FromString);
			insert=res.getString(R.string.writeFromString);
		}
		else{
			heading=res.getString(R.string.endString);
			insert=res.getString(R.string.writeDestinationString);
		}
		
		title.setText(heading);
		insertLocationText.setText(insert);
	}
	
	public String getLocation() throws LocationNotProvidedException{

		String addressString = address.getText().toString().trim();
		String suburbString = suburb.getText().toString().trim();
		if(addressString==null||addressString.equals("")){
			throw new LocationNotProvidedException();
		}
		if(suburbString==null||suburbString.equals("")){
			throw new LocationNotProvidedException();
		}
		
		return addressString + "," + suburbString;
	}
	
	
	private void doGetLocation(){
		
		Context context = getContext();
		hasLocation = false; // has found the current location
		errorFlag = false; //An error has occoured
		hasGPSLocation = false; //has old gps Location
		hasNetworkLocation = false; // has old network location
		unableToLocateFlag = false; 
		currentGPSLocation = null;
		currentNetworkLocation = null;
		
		//Show loading dialog
		dialog = ProgressDialog.show(context, context.getString(R.string.LOADING_TITLE),
				context.getText(R.string.LOADING_MESSAGE), true);
		dialog.show();
		
		ConnectionChecker cc = new ConnectionChecker(context);
		
		//Check for internet
		if(!cc.checkInternetEnabled()){
			showError(res.getString(R.string.NoInternetConnection));
			dialog.dismiss();
			return;
		}
		
		//If both useGPS and UseNetwork are flase, then we cannot locate
		if((useGPS || useNetwork)==false){
			showError(res.getString(R.string.NoConnectionAllowedString));
			dialog.dismiss();
			return;
		}
		
		//Use gps/network only if enabled and selcted to be used
		useGPS = useGPS && cc.checkGPSEnabled();
		useNetwork = useNetwork && cc.checkNetworkLocaitonEnabled();
		
		//Check if selected methods are enabled
		if((useGPS || useNetwork)==false){
			showError(res.getString(R.string.SelectedProvidersNotAvailable));
			return;
		}
		
		
		manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		//What to do when GPS is updated
		GPSListener = new LocationListener() {
			
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			
			public void onProviderEnabled(String provider) {}
			
			public void onProviderDisabled(String provider) {}
			
			public void onLocationChanged(Location location) {
				updateLocation(location, LocationManager.GPS_PROVIDER);
			}
		};
		
		//What to do when network location is updated
		networkListener = new LocationListener() {
			
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			
			public void onProviderEnabled(String provider) {}
			
			public void onProviderDisabled(String provider) {}
			
			public void onLocationChanged(Location location) {
				updateLocation(location, LocationManager.NETWORK_PROVIDER);
			}
		};
		
		//Request location from providers
		if(useGPS){
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, GPSListener);
		}
		if(useNetwork){
			manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
		}
	
		
		timer = new Timer();
		Timeout t = new Timeout();
		timer.schedule(t, 30000); //Schedule a time out to stop searching
		
		AddressLoader al = new AddressLoader();
		al.execute(); //Start searching
		
	}
	
	/**
	 * Updates the current location of the given provider
	 * @param location
	 * @param provider
	 */
	private void updateLocation(Location location, String provider){
		if(provider.equals(LocationManager.GPS_PROVIDER)){
			hasGPSLocation = true;
			currentGPSLocation = location;
		}
		else if(provider.equals(LocationManager.NETWORK_PROVIDER)){
			hasNetworkLocation = true;
			currentNetworkLocation = location;
		}
		
		//We have the required locations, stop looking!!!
		if(hasGPSLocation==useGPS && hasNetworkLocation==useNetwork){
			Location bestLocation = findBestLocation(currentGPSLocation, currentNetworkLocation);
			this.location = bestLocation;
			hasLocation = true;
			timer.cancel();
		}
		
	}
	
	/**
	 * Choose the best location fix from the two given
	 * @param l1
	 * @param l2
	 * @return
	 */
	private Location findBestLocation(Location l1, Location l2){
		//If both are null, ERROR
		if(l1==null && l2==null){
			throw new IllegalArgumentException();
		}
		
		//IF one is null, the other is best
		if(l1==null){
			return l2;
		}
		
		if(l2==null){
			return l1;
		}
		
		//Check accuracy
		if(l1.getAccuracy() < l2.getAccuracy() ){
			return l1;
		}
		
		return l2;
	}
	
	private void setAddressAndSuburbText(){
		address.setText(addressString);
		suburb.setText(suburbString);
	}
	
	private void showError(String error){
		
		Context context = getContext();
		errorFlag = true;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(error)
		       .setCancelable(false)
		       .setPositiveButton(context.getString(R.string.OK), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public enum TripNode{
		Start,End;
	}
	
	/**
	 * Blocks UI until address found, a timeout ocours, or an error ocours
	 * @author Niall
	 *
	 */
	public class AddressLoader extends AsyncTask<String, Integer, String>{

		@Override
		protected void onPostExecute(String result) {
			
			//If result is not null, and error occoured
			if(result!=null){
				showError(result);
			}
			else{
				//Suburb and address fileds are now set, so use them!!
				setAddressAndSuburbText();
			}
			
			if(dialog!=null){
				dialog.dismiss();
				dialog = null;
			}
			
			//Stop getting locations from providers
			if(useGPS){
				manager.removeUpdates(GPSListener);
			}
			if(useNetwork){
				manager.removeUpdates(networkListener);
			}
			
		}

		@Override
		protected String doInBackground(String... params) {
			
			//Block until we have a location or an error
			while(!(hasLocation || errorFlag)){
				
			}
			
			
			if(unableToLocateFlag){
				return res.getString(R.string.UnableToLocate);
			}
			
			if(hasLocation){
				try{
					Geocoder gc = new Geocoder(getContext());
					
					
					
					List<Address> adds = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
					
					addressString = adds.get(0).getAddressLine(0);
					suburbString = adds.get(0).getAddressLine(1);
					
				} catch (IOException e) {
					
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					Log.e("TEST", sw.toString());
					return res.getString(R.string.NetworkErrorString);
				}
			}
			return null;
		}
	}
	
	/**
	 * Stops locating after a timeout has occoured
	 * @author Niall
	 *
	 */
	public class Timeout extends TimerTask{
		
		@Override
		public void run() {
			
			if(currentGPSLocation != null && useGPS){
				location = currentGPSLocation;
				hasLocation = true;
				return;
			}
			
			else if(currentNetworkLocation != null && useNetwork){
				location = currentNetworkLocation;
				hasLocation = true;
				return;
			}
			
			
			// Attempt to use last known loactions
			Location lastKnownNetworkLocation = null;
			Location latKnownGPSLocation = null;
			
			if(useNetwork){
				lastKnownNetworkLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
			
			if(useGPS){
				latKnownGPSLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
			}
			
			if(lastKnownNetworkLocation!= null && isOlderThanTTL(lastKnownNetworkLocation.getTime())){
				lastKnownNetworkLocation = null;
			}
			
			//Check TTL for lastknown locations
			if(latKnownGPSLocation != null && isOlderThanTTL(latKnownGPSLocation.getTime())){
				latKnownGPSLocation = null;
			}
			
			//IF both are null, then we cannot locate...
			if(latKnownGPSLocation== null && lastKnownNetworkLocation ==null){
				errorFlag = true;
				unableToLocateFlag = true;
				return;
			}
			
			location = findBestLocation(lastKnownNetworkLocation, latKnownGPSLocation);
			hasLocation = true;
		}
		
	}
	
	private boolean isOlderThanTTL(long time){
		return (System.currentTimeMillis() - time) > TTL;
	}
	
}
