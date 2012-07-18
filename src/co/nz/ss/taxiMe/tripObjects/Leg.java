package co.nz.ss.taxiMe.tripObjects;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.location.Location;

public class Leg extends Step{

	private ArrayList<Step> steps;
	private String startAddress;
	private String endAddress;
	
	public Leg() {
		steps = new ArrayList<Step>();
	}

	public ArrayList<Step> getSteps() {
		return steps;
	}

	public void addSteps(Step step) {
		if(!(step instanceof Leg)){
			steps.add(step);
		}
	}

	public String getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}

	public String getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}
	
	public String[] getLegInstructions() {
		ArrayList<String> instructions = new ArrayList<String>();
		for(Step s: steps){
			instructions.add(s.getInstructions());
		}
		
		String[] toReturn = new String[instructions.size()];
		toReturn = instructions.toArray(toReturn);
		return toReturn;
	}

	public ArrayList<GeoPoint> getPath(){	
		
		ArrayList<GeoPoint> path = new ArrayList<GeoPoint>();
		
		for(Step s: steps){
			path.add(convertToGeoPoint(s.getStartLocation()));
			path.add(convertToGeoPoint(s.getEndLocation()));
		}
		
		return path;
	}
	
	private GeoPoint convertToGeoPoint(Location location){
		return new GeoPoint((int)(location.getLatitude() * 1e6), (int)(location.getLongitude() * 1e6));
	}
	
}
