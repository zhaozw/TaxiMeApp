package co.nz.ss.taxiMe.exceptions.connectionExceptions;

import co.nz.ss.taxiMe.R;
import android.content.Context;
import android.util.Log;


public class InternalServerException extends ConnectionException {
	
	public InternalServerException(String message, Context context)
	{
		super(context.getString(R.string.InternalServerErrorString) + message);
		Log.e("TEST", this.getClass().getName());
	}

}
