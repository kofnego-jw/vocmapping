package at.ac.uibk.igwee.metadata.gnd.impl;

public enum GndIndex {
	
	/**
	 * All indices
	 */
	WOE,
	
	/**
	 * Person
	 */
	PER,
	/**
	 * Institutions (Körperschaft)
	 */
	KOE,
	/**
	 * Geolocation
	 */
	GEO,
	
	/**
	 * Bibliographical group. Possible values:
	 * <p> 
	 * Entitäten	Bibliografische Gattung
	 * Geografikum	Tg*
	 * Kongress	Tf*
	 * Körperschaft	Tb*
	 * Name	Tn*
	 * Person	Tp*
	 * Sachbegriff	Ts*
	 * Werk	Tu*
	 * <p>
	 * Taken from: http://www.dnb.de/DE/Service/DigitaleDienste/SRU/sru_node.html#doc43186bodyText9
	 */
	BBG,
	
	/**
	 * Number
	 */
	NUM,
	
	/**
	 * DNB Internal Number
	 */
	IDN

}
