package at.ac.uibk.igwee.metadata.gnd.impl;

public enum GndBbgValue {
	
	PERSON("Tp*"),
	PLACE("Tg*"),
	INSTITUTION("Tb*");
	
	private String gndValue;
	
	GndBbgValue(String val) {
		this.gndValue = val;
	}
	
	public String getGndValue() {
		return this.gndValue;
	}

}
