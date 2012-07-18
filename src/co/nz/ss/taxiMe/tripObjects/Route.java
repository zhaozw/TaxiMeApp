package co.nz.ss.taxiMe.tripObjects;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

import co.nz.ss.taxiMe.R;

import android.content.Context;
import android.location.Location;

public class Route implements Serializable{

	private ArrayList<Leg> legs;
	private String overView;
	private String copyrightString;
	private Location southWestBound;
	private Location northEastBound;
	
	public Route(){
		legs = new ArrayList<Leg>();
	}

	public String getOverView() {
		return overView;
	}

	public void setOverView(String overView) {
		this.overView = overView;
	}

	public String getCopyrightString() {
		return copyrightString;
	}

	public void setCopyrightString(String copyrightString) {
		this.copyrightString = copyrightString;
	}
	
	public void setSouthWestBound(Location location){
		southWestBound = location;
	}
	
	public void setNorthEastBound(Location location){
		northEastBound = location;
	}
	
	public Location getSouthWestBound(){
		return southWestBound;
	}
	
	public Location getNorthEastBound(){
		return northEastBound;
	}
	
	public void addLeg(Leg l){
		legs.add(l);
	}
	
	public ArrayList<Leg> getLegs(){
		return legs;
	}
	
	public int getDistance(){
		int distance = 0;
		
		for(Leg l: legs){
			distance += l.getDistance();
		}
		
		return distance;
	}
	
	public String[] getInstructions(){
		
		ArrayList<String> insructions = new ArrayList<String>();
		
		for(Leg l: legs){
			String[] legInstructions = l.getLegInstructions();
			
			for(int i = 0; i< legInstructions.length; i++){
				insructions.add(legInstructions[i]);
			}
		}
		
		String[] toReturn = new String[insructions.size()];
		toReturn = insructions.toArray(toReturn);
		return toReturn;
	}
	
	public ArrayList<GeoPoint> getPath(){
		
		ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
		
		for(Leg l: legs){
			
			for(GeoPoint gp: l.getPath()){
				points.add(gp);
			}
		}
		
		return points;
	}
	
}
