package co.nz.ss.taxiMe.exceptions;

import android.util.Log;
import co.nz.ss.taxiMe.R;

public class LocationNotProvidedException extends Exception {

	public LocationNotProvidedException() {
		super();
		Log.e("TEST", this.getClass().getName());
	}


}
