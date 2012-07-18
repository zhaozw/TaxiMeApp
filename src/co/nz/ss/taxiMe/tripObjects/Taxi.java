package co.nz.ss.taxiMe.tripObjects;

public class Taxi implements Comparable<Taxi>{

	private String name;
	private String phoneNuber;
	private double currentPrice;
	
	public Taxi(String name, String phoneNumber){
		this.name = name;
		this.phoneNuber = phoneNumber;
	}
	
	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNuber() {
		return phoneNuber;
	}

	public void setPhoneNuber(String phoneNuber) {
		this.phoneNuber = phoneNuber;
	}

	public int compareTo(Taxi other) {
		return (int) (currentPrice*100 - other.getCurrentPrice()*100);
	}

	
}
