package co.nz.ss.taxiMe.businessObjects;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import co.nz.ss.taxiMe.R;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.ConnectionEncodingException;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.ConnectionException;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.ConnectionURLException;
import android.content.Context;

/**
 * Creates urls for the service requested methods with given paramaters
 * @author Niall
 *
 */
public class URLFactory {
	
	/**
	 * Get the url for prices given a distance
	 * @throws A Subclass of ConnectionException - Allows for easier exception handling
	 */
	public static URL getPricesURL(int distance, Context context) throws ConnectionException{
		
		try {
			return new URL(context.getString(R.string.URL_BASE) + context.getString(R.string.GET_PRICES_URL) 
					+ distance);
		} catch (MalformedURLException e) {
			throw new ConnectionURLException(context);
		}
	}
	
	/**
	 * Get the url for a route given start and end locations
	 * @throws A Subclass of ConnectionException - Allows for easier exception handling
	 */
	public static URL getRouteURL(String start, String end, boolean usesSensor, Context context) throws ConnectionException{
		try {
			return new URL(context.getString(R.string.URL_BASE) + context.getString(R.string.GET_ROUTE_URL) + 
					URLEncoder.encode(start, context.getString(R.string.ENCODING)) + 
					context.getString(R.string.END_URL) + URLEncoder.encode(end, context.getString(R.string.ENCODING)) +
					context.getString(R.string.SENSOR_URL) + usesSensor);
		} catch (MalformedURLException e) {
			throw new ConnectionURLException(context);
		} catch (UnsupportedEncodingException e) {
			throw new ConnectionEncodingException(context);
		}
	}
	
	/**
	 * Get the url for info about a  given taxi id
	 * @return
	 * @throws A Subclass of ConnectionException - Allows for easier exception handling
	 */
	public static URL getInfoURL(int id, Context context) throws ConnectionException{
		
		try {
			return new URL(context.getString(R.string.URL_BASE) + context.getString(R.string.GET_INFO_URL) 
					+ id);
		} catch (MalformedURLException e) {
			throw new ConnectionURLException(context);
		}

	}
}
