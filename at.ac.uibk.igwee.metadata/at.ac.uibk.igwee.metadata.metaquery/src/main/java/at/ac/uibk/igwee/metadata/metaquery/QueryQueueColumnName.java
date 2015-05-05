package at.ac.uibk.igwee.metadata.metaquery;

/**
 * Name of each column in a CSV QueryQueue file
 * @author joseph
 *
 */
public enum QueryQueueColumnName {
	
	/**
	 * Key
	 */
	KEY("Key"),
	/**
	 * Name
	 */
	NAME("Name"),
	
	/**
	 * Query
	 */
	QUERY("Query"),
	
	/**
	 * Info
	 */
	INFO("Info"),
	
	/**
	 * Type
	 */
	TYPE("Type"),
	
	/**
	 * Authorities
	 */
	AUTHORITY("Authorities");
	
	private String columnTitle;
	QueryQueueColumnName(String ct) {
		this.columnTitle = ct;
	}
	/**
	 * The title of the column
	 * @return
	 */
	public String getColumnTitle() {
		return this.columnTitle;
	}
}