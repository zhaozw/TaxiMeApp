package co.nz.ss.taxiMe.parsers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import co.nz.ss.taxiMe.businessObjects.URLFactory;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.ConnectionException;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.ConnectionIOException;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.InvalidXMLException;

import android.content.Context;
import android.util.Xml;

public class PricesParser{

	public static double[] parsePrices(int distance, Context context) throws ConnectionException{
		
		ArrayList<Double> returnedValues = new ArrayList<Double>();
		
		XmlPullParser parser = Xml.newPullParser();
		URL priceURL = URLFactory.getPricesURL(distance, context);
		
		try {
			parser.setInput(new InputStreamReader(priceURL.openStream()));
			
			int eventType = parser.getEventType();
			String tagName;
			
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch(eventType){
					case XmlPullParser.START_TAG:
						tagName = parser.getName();
						
						if(tagName.equals("return")){
							returnedValues.add(Double.parseDouble(parser.nextText()));
						}
						break;
				}
				
				eventType = parser.next();
			}
			
		} catch (XmlPullParserException e) {
			throw new InvalidXMLException(context);
		} catch (IOException e) {
			throw new ConnectionIOException(context);
		}
		catch(NumberFormatException e){
			throw new InvalidXMLException(context);
		}
		
		double[] toReturn = new double[returnedValues.size()];
		
		for(int i=0; i<returnedValues.size(); i++){
			toReturn[i] = returnedValues.get(i);
		}
		
		return toReturn;
		
	}
	
	
}
