package co.nz.ss.taxiMe.exceptions.connectionExceptions;

import co.nz.ss.taxiMe.R;
import android.content.Context;
import android.util.Log;

public class ConnectionEncodingException extends ConnectionException {

	public ConnectionEncodingException(Context context) {
		super(context.getString(R.string.ConnectionEncodingExceptionString));
		Log.e("TEST", this.getClass().getName());
	}

}
