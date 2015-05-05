package at.ac.uibk.igwee.metadata.gnd.impl;

public final class GndQueryParameter {
	
	private final GndIndex index;
	private final String queryString;
	
	public GndQueryParameter(GndIndex index, String queryString) {
		super();
		this.index = index;
		this.queryString = queryString;
	}
	/**
	 * @return the index
	 */
	public GndIndex getIndex() {
		return index;
	}
	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}
	

}
