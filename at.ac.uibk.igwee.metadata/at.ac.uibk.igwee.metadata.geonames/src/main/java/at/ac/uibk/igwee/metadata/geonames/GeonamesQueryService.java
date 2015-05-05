package at.ac.uibk.igwee.metadata.geonames;

import at.ac.uibk.igwee.metadata.query.Query;
import at.ac.uibk.igwee.metadata.query.QueryService;


/**
 * Abstracted Service Interface for GeonamesQueryService
 * @author joseph
 *
 */
public interface GeonamesQueryService extends QueryService {

	/**
	 * Default start row = 0;
	 */
	public static final int START_ROW_OFFSET = -1; 
	
	/**
	 * Should return the search result, never null, but the result can contain an empty list
	 * as results. 
	 * @param name
	 * @return
	 */
	default public GeonamesQueryResult queryLocation(String name) throws GeonamesQueryException {
		return queryLocationInCountry(name, null);
	}
	/**
	 * Should return the search result within a country, never null, but the result can contain an empty list
	 * as results. 
	 * @param name the name of the location
	 * @param countryCode a country code ("AT")
	 * @return
	 */
	default public GeonamesQueryResult queryLocationInCountry(String name, String countryCode)
		throws GeonamesQueryException {
		return queryLocationInCountry(name, countryCode, Query.DEFAULT_START_ROW, 
				Query.DEFAULT_MAX_ROW);
	}
	
	/**
	 * 
	 * @param name
	 * @param startRow
	 * @param maxRows
	 * @return
	 * @throws GeonamesQueryException
	 */
	default public GeonamesQueryResult queryLocation(String name, int startRow, int maxRows) 
			throws GeonamesQueryException {
		return queryLocationInCountry(name, null, startRow, maxRows);
	}
	
	/**
	 * Should return the search result within a country, never null, but the result can contain an empty list
	 * as results. 
	 * @param name
	 * @param countryCode
	 * @param startRow
	 * @param maxRow
	 * @return
	 * @throws GeonamesQueryException
	 */
	public GeonamesQueryResult queryLocationInCountry(String name, String countryCode, 
			int startRow, int maxRow) throws GeonamesQueryException;
	
	/**
	 * 
	 * @param startRow startRow, starting with 1
	 * @return adjusted startRow for usage with Geonames.WebService
	 */
	default public int adjustStartRowOffset(int startRow) {
		return startRow + START_ROW_OFFSET;
	}
	
}
