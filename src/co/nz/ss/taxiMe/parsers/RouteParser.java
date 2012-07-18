package co.nz.ss.taxiMe.parsers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import org.xmlpull.v1.XmlPullParserException;

import co.nz.ss.taxiMe.R;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.ConnectionException;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.ConnectionIOException;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.InternalServerException;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.InvalidXMLException;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.LocationNotFoundException;
import co.nz.ss.taxiMe.tripObjects.Leg;
import co.nz.ss.taxiMe.tripObjects.Route;
import co.nz.ss.taxiMe.tripObjects.Step;

import android.content.Context;
import android.location.Location;
import android.util.Xml;

public class RouteParser {

	//TODO: Impliment super exception class.  catch default exceptions and rethrow same exception but of subtype of new class
	public static Route parse(URL input, Context context) throws ConnectionException{
		XmlPullParser parser = Xml.newPullParser();
		try{
			parser.setInput(new InputStreamReader(input.openStream()));
			return parse(parser, context);
		}
		catch (XmlPullParserException e) {
			throw new InvalidXMLException(context);
		} catch (IOException e) {
			throw new ConnectionIOException(context);
		}
		catch(NumberFormatException e){
			throw new InvalidXMLException(context);
		}
	}
	
	public static Route parse(String input, Context context) throws ConnectionException{
		XmlPullParser parser = Xml.newPullParser();
		try{
		parser.setInput(new StringReader(input));
		return parse(parser, context);
	}
		catch (XmlPullParserException e) {
			throw new InvalidXMLException(context);
		} catch (IOException e) {
			throw new ConnectionIOException(context);
		}
		catch(NumberFormatException e){
			throw new InvalidXMLException(context);
		}
	}
	
	private static Route parse(XmlPullParser parser, Context context) throws XmlPullParserException, IOException, ConnectionException{

		int eventType = parser.getEventType();
		String tagName;
		
		while(eventType != XmlPullParser.END_DOCUMENT){
			switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
					
					if(tagName.equals("Reason")){
						throw new InternalServerException(parser.nextText(), context);
					}
					else if(tagName.equals("return")){
						return RouteParser.parse(parser.nextText(), context);
					}
					else if(tagName.equals("status")){
						parseStatus(parser, context);
					}
					else if(tagName.equals("route")){
						return parseRoute(parser, context);
					}
	
					break;

			}
			
			eventType = parser.next();
		}
			
		return null;
		
	}
	
	private static void parseStatus(XmlPullParser parser, Context context) throws XmlPullParserException, IOException, InternalServerException, LocationNotFoundException{
		String status = parser.nextText();
		if(status.equalsIgnoreCase("NOT_FOUND")){
			throw new LocationNotFoundException(context);
		}
		else if(!status.equalsIgnoreCase("OK")){
			throw new InternalServerException(status, context);
		}
	}
	
	private static Route parseRoute(XmlPullParser parser, Context context) throws XmlPullParserException, IOException, InternalServerException, InvalidXMLException{
		Route toReturn = new Route();

		int eventType = parser.next();
		boolean done = false;
		String tagName;
		
		while(!done){
			
			if(eventType==XmlPullParser.START_TAG){
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase("summary")){
					toReturn.setOverView(parser.nextText());
				}
				else if(tagName.equalsIgnoreCase("leg")){
					toReturn.addLeg(legParser(parser, context));
				}
				else if(tagName.equalsIgnoreCase("copyrights")){
					toReturn.setCopyrightString(parser.nextText());
				}
				else if(tagName.equalsIgnoreCase("bounds")){
					toReturn.setSouthWestBound(locationParsing(parser, context, "southwest"));
					toReturn.setNorthEastBound(locationParsing(parser, context, "northeast"));
				}
			}
			else if(eventType==XmlPullParser.END_TAG){
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase("route")){
					return toReturn;
				}
			}
			

			eventType = parser.next();
		}
			
		throw new InvalidXMLException(context);

	}
	
	private static Leg legParser(XmlPullParser parser, Context context) throws XmlPullParserException, IOException{
		Leg toReturn = new Leg();

		int eventType = parser.next();
		boolean done = false;
		String tagName;
		
		while(!done){
			
			if(eventType==XmlPullParser.START_TAG){
				tagName = parser.getName();
				
				if(tagName.equalsIgnoreCase("start_location")){
					toReturn.setStartLocation(locationParsing(parser, context, tagName));
				}
				else if(tagName.equalsIgnoreCase("end_location")){
					toReturn.setEndLocation(locationParsing(parser, context, tagName));
				}
				else if(tagName.equalsIgnoreCase("start_address")){
					toReturn.setStartAddress(parser.nextText());
				}
				else if(tagName.equalsIgnoreCase("end_address")){
					toReturn.setEndAddress(parser.nextText());
				}
				else if(tagName.equalsIgnoreCase("duration")){
					toReturn.setDuration(getValue(parser, context));
				}
				else if(tagName.equalsIgnoreCase("distance")){
					toReturn.setDistance(getValue(parser, context));
				}
				else if(tagName.equalsIgnoreCase("step")){
					toReturn.addSteps(stepParsing(parser, context));
				}
			}
			else if(eventType==XmlPullParser.END_TAG){
				if(parser.getName().equalsIgnoreCase("leg")){
					return toReturn;
				}
			}
			

			eventType = parser.next();
		}
			
		return null;
	}
	
	private static Step stepParsing(XmlPullParser parser, Context context)throws XmlPullParserException, IOException{
		Step toReturn = new Step();

		int eventType = parser.next();
		boolean done = false;
		String tagName;
		
		while(!done){
			
			if(eventType==XmlPullParser.START_TAG){
				tagName = parser.getName();
				
				if(tagName.equalsIgnoreCase("start_location")){
					toReturn.setStartLocation(locationParsing(parser, context, tagName));
				}
				else if(tagName.equalsIgnoreCase("end_location")){
					toReturn.setEndLocation(locationParsing(parser, context, tagName));
				}
				else if(tagName.equalsIgnoreCase("duration")){
					toReturn.setDuration(getValue(parser, context));
				}
				else if(tagName.equalsIgnoreCase("distance")){
					toReturn.setDistance(getValue(parser, context));
				}
				else if(tagName.equalsIgnoreCase("html_instructions")){
					toReturn.setInstructions(parser.nextText());
				}
			}
			else if(eventType==XmlPullParser.END_TAG){
				if(parser.getName().equalsIgnoreCase("step")){
					return toReturn;
				}
			}
			

			eventType = parser.next();
		}
			
		return null;
	}
	
	private static Location locationParsing(XmlPullParser parser, Context context, String endTag) throws XmlPullParserException, IOException, NumberFormatException{
		Location toReturn = new Location(context.getString(R.string.GOOGLE));
		
		int eventType = parser.next();
		String tagName;
		boolean done = false;
		double lat = 0;
		double lng = 0;
		
		while(!done){
			
			if(eventType==XmlPullParser.START_TAG){
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase("lat")){
					lat = Double.parseDouble(parser.nextText());
				}
				else if(tagName.equalsIgnoreCase("lng")){
					lng = Double.parseDouble(parser.nextText());
				}
			}
			else if(eventType==XmlPullParser.END_TAG){
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase(endTag)){
					return toReturn;
				}
				else if(tagName.equalsIgnoreCase("lat")){
					toReturn.setLatitude(lat);
				}
				else if(tagName.equalsIgnoreCase("lng")){
					toReturn.setLongitude(lng);
				}
			}
			

			eventType = parser.next();
		}
			
		return null;
	}
	
	private static int getValue(XmlPullParser parser, Context context) throws XmlPullParserException, IOException, NumberFormatException{
		
		int eventType = parser.next();
		String tagName;
		boolean done = false;
		
		while(!done){
			
			if(eventType==XmlPullParser.START_TAG){
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase("value")){
					return Integer.parseInt(parser.nextText());
				}
				
			}
			
			eventType = parser.next();
		}
			
		return -1;
	}
			
}
