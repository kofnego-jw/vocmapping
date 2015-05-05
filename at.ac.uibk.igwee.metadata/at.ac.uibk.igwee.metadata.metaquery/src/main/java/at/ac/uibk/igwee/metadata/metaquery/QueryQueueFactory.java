package at.ac.uibk.igwee.metadata.metaquery;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * QueryQueue Serialization methods
 * @author joseph
 *
 */
public interface QueryQueueFactory {
	
	/**
	 * Default Encoding for XML
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * Loads and creates a QueryQueue
	 * @param in InputStream
	 * @param format the Format
	 * @param encoding Character encoding
	 * @return a QueryQueue
	 * @throws QueryServiceException
	 */
	public QueryQueue loadQueryQueue(InputStream in, QueryQueueFormat format, String encoding)
			throws QueryServiceException;
	
	/**
	 * Loads an XML-Stream
	 * @param xml
	 * @return a QueryQueue
	 * @throws QueryServiceException
	 */
	default public QueryQueue loadQueryQueue(InputStream xml) throws QueryServiceException {
		return loadQueryQueue(xml, QueryQueueFormat.XML_QUERYQUEUE, DEFAULT_ENCODING);
	}
	
	/**
	 * 
	 * @param qq
	 * @return an XML-String of queryQueue
	 * @throws QueryServiceException
	 */
	public String fromQueryQueueToXML(QueryQueue qq) throws QueryServiceException;
	
	/**
	 * Saves the QueryQueue to an OutputStream
	 * @param qq
	 * @param os
	 * @throws QueryServiceException
	 */
	public void writeQueryQueue(QueryQueue qq, OutputStream os) throws QueryServiceException;
	
	/**
	 * 
	 * @param queryQueue
	 * @return A string, formatted as CSV, having the following columns "Name" (=key), 
	 * 			"Info", "Type", and "URL", using the deliminator ","
	 * @throws QueryServiceException if no fixed results are found
	 */
	default public String exportResult(QueryQueue queryQueue) throws QueryServiceException {
		return exportResult(queryQueue, ',');
	}
	
	
	/**
	 * 
	 * @param queryQueue
	 * @param deliminator.
	 * @return A string, formatted as CSV, having the following columns "Name" (=key), 
	 * 			"Info", "Type", and "URL", using the deliminator
	 * @throws QueryServiceException
	 */
	public String exportResult(QueryQueue queryQueue, char deliminator) 
			throws QueryServiceException;

}
