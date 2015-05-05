package at.ac.uibk.igwee.metadata.viaf.impl;

/**
 * DataObject class for a ViafQueryParameter-Pair.
 * 
 * NB: Viaf uses one query parameter in HTTP that contains a 
 * number of actual query request ("?query='local.personalNames = "Jane Austen" and local.name = dnb')
 * while everything between '' should be URLEncoded.
 * This class contains only one query (local.personalNames="Jane Austen").
 * 
 * NB: This class does not model the modifier (=, any, not, >= etc.). This kind of queries
 * seems to take a longer time for VIAF, and should therefore avoided.
 * 
 * @author joseph
 *
 */
public final class ViafQueryParameter {
	
	/**
	 * The key, there is only a limited number of that.
	 */
	private final ViafQueryKey key;
	/**
	 * The value
	 */
	private final String value;
	public ViafQueryParameter(ViafQueryKey key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	/**
	 * @return the key
	 */
	public ViafQueryKey getKey() {
		return key;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	
	
	

}
