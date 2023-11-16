package monopolySimulation;

import enums.PropertiesType;

public class Properties {

	private String name;
	private PropertiesType propertyType;
	private int timesLandedOn;
	
	public int getTimesLandedOn() {
		return timesLandedOn;
	}
	public void setTimesLandedOn(int timesLandedOn) {
		this.timesLandedOn = timesLandedOn;
	}
	
}
