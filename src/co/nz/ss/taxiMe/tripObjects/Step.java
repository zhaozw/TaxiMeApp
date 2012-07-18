package co.nz.ss.taxiMe.tripObjects;

import android.location.Location;
import android.text.Html;

public class Step {

	private Location startLocation;
	private Location endLocation;
	private int duration;
	private int distance;
	private String instructions;

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getInstructions() {
		return Html.fromHtml(instructions).toString();
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public Location getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Location location) {
		endLocation = location;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Location location) {
		startLocation = location;
	}
}
