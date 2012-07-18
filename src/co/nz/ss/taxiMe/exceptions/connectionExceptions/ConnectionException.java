package co.nz.ss.taxiMe.exceptions.connectionExceptions;

import android.util.Log;

public abstract class ConnectionException extends Exception {

	public ConnectionException(String detailMessage) {
		super(detailMessage);
		Log.e("TEST", this.getClass().getName());
	}

}
