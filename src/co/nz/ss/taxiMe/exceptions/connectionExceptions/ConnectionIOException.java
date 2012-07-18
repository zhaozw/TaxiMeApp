package co.nz.ss.taxiMe.exceptions.connectionExceptions;

import co.nz.ss.taxiMe.R;
import android.content.Context;
import android.util.Log;

public class ConnectionIOException extends ConnectionException{

	public ConnectionIOException(Context context) {
		super(context.getString(R.string.ConnectionIOExceptionString));
		Log.e("TEST", this.getClass().getName());
	}

}
