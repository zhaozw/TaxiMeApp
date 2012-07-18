package co.nz.ss.taxiMe.exceptions.connectionExceptions;

import co.nz.ss.taxiMe.R;
import android.content.Context;
import android.util.Log;


public class InvalidXMLException extends ConnectionException {
	
	public InvalidXMLException(Context context)
	{
		super(context.getString(R.string.InvalidXMLExceptionString));
		Log.e("TEST", this.getClass().getName());
	}
}
