package at.ac.uibk.igwee.metadata.viaf.impl;

/**
 * DataObject for ViafQueryKey
 * @author Joseph
 *
 */
public enum ViafQueryKey {
	
	/**
	 * Query in any place = cql.any
	 */
	ANY("cql.any"),
	
	/**
	 * Query in Authority = local.sources
	 */
	AUTHORITY("local.sources"),
	
	/**
	 * Query in Personal Names = local.personalNames
	 */
	PERSONAL_NAME("local.personalNames"),
	
	/**
	 * Query in corporate names = local.corporateNames
	 */
	CORPORATE_NAME("local.corporateNames"),
	
	/**
	 * Query in place names = local.geographicNames
	 */
	PLACE_NAME("local.geographicNames"),
	
	/**
	 * Query in main heading. added for 
	 */
	MAIN_HEADING("local.mainHeadingEl");

	/**
	 * Key for a ViafQuery 
	 */
	private String viafKey;
	
	ViafQueryKey(String key) {
		this.viafKey = key;
	}
	
	/**
	 * 
	 * @return the viafKey
	 */
	public String getViafKey() {
		return this.viafKey;
	}
	
}
