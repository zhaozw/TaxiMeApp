package co.nz.ss.taxiMe.exceptions.connectionExceptions;

import co.nz.ss.taxiMe.R;
import android.content.Context;
import android.util.Log;

public class ConnectionURLException extends ConnectionException {

	public ConnectionURLException(Context context) {
		super(context.getString(R.string.URLExceptionString));
		Log.e("TEST", this.getClass().getName());
	}

}
