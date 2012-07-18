package co.nz.ss.taxiMe.businessObjects;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * Checks for GPS, Network and internet connections
 * @author Niall
 *
 */
public class ConnectionChecker{
	
	private ConnectivityManager connectionManager;
	private LocationManager locationManager;
	
	public ConnectionChecker(Context context){
		connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}
	
	
	public boolean checkWiFiEnabled(){
		return checkForState(ConnectivityManager.TYPE_WIFI);
	}
	
	public boolean check3GEnabled(){
		return checkForState(ConnectivityManager.TYPE_MOBILE);
	}
	
	public boolean checkInternetEnabled(){
		NetworkInfo network = (NetworkInfo) connectionManager.getActiveNetworkInfo();
		
		if(network==null || !network.isConnected()){
			return false;
		}
		
		return true;
	}
	
	public boolean checkGPSEnabled(){
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	public boolean checkNetworkLocaitonEnabled(){
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
	
	private boolean checkForState(int type){
		State connected = connectionManager.getNetworkInfo(type).getState();
		if(connected!=null && connected == NetworkInfo.State.CONNECTED){
			return true;
		}
		return false;
	}
}
