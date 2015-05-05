package at.ac.uibk.igwee.metadata.geonames;

import at.ac.uibk.igwee.metadata.vocabulary.AbstractVocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

/**
 * Modell class for Geoname Data
 * @author joseph
 *
 */
public class GeonameData extends AbstractVocabulary {
	
	public static final String GEONAMES_PREFIX = "geonames:";
	
	public static final String DEFAULT_UNKNOWN_STRING = "<<UNKNOWN>>";
	
	/**
	 * name (e.g. Innsbruck)
	 */
	private String name;
	
	/**
	 * country (e.g. Autria)
	 */
	private String country;
	
	/**
	 * country code (e.g. AT, DE)
	 */
	private String countryCode;
	
	/**
	 * continent code (e.g. EU, NA)
	 */
	private String continentCode;
	
	/**
	 * longitude
	 */
	private double longitude;
	
	/**
	 * latitude
	 */
	private double latitude;

	/**
	 * Geonames ID
	 */
	private int geonameId;

	/**
	 * Please do not use the default constructor. all fields should be final, but cannot be
	 * due to restrictions of Webservice
	 */
	public GeonameData() {
		super();
	}
	
	public GeonameData(String name, String country, String countryCode, String continentCode,
			double longitude, double latitude, int geonameId) {
		super(GEONAMES_PREFIX + Integer.toString(geonameId),
				OrgGeonames.getInstance().createURI(geonameId), 
				OrgGeonames.getInstance(), VocabularyType.PLACE_NAME, null);
		this.name = name;
		this.country = country;
		this.countryCode = countryCode;
		this.continentCode = continentCode;
		this.longitude = longitude;
		this.latitude = latitude;
		this.geonameId = geonameId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @return the continentCode
	 */
	public String getContinentCode() {
		return continentCode;
	}

	/**
	 * @param continentCode the continentCode to set
	 */
	public void setContinentCode(String continentCode) {
		this.continentCode = continentCode;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @param geonameId the geonameId to set
	 */
	public void setGeonameId(int geonameId) {
		this.geonameId = geonameId;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	
	/**
	 * 
	 * @return the geoname Id
	 */
	public int getGeonameId() {
		return this.geonameId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GeonameData [name=" + name + ", country=" + country
				+ ", countryCode=" + countryCode + ", continentCode="
				+ continentCode + ", longitude=" + longitude + ", latitude="
				+ latitude + ", geonameId=" + geonameId + "]";
	}
	
	private Object readResolve() {
		if (getAuthority()==null)
			setAuthority(OrgGeonames.getInstance());
		
		return this;
	}
	

}
