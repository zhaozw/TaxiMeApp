package co.nz.ss.taxiMe.exceptions.connectionExceptions;

import co.nz.ss.taxiMe.R;
import android.content.Context;
import android.util.Log;

public class LocationNotFoundException extends ConnectionException {

	public LocationNotFoundException(Context context) {
		super(context.getString(R.string.LocationNotFoundExceptionString));
		Log.e("TEST", this.getClass().getName());
	}

}
