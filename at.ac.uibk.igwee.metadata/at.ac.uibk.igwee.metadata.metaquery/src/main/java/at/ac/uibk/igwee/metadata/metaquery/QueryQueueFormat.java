package at.ac.uibk.igwee.metadata.metaquery;

/**
 * InputType of the queryqueue
 * @author joseph
 *
 */
public enum QueryQueueFormat {
	
	/**
	 * The file is saved as QueryQueueXML
	 */
	XML_QUERYQUEUE,
	
	/**
	 * A Comma separated value file
	 */
	CSV,
	
	/**
	 * A Text file, using TAB and \n as deliminator
	 */
	TEXT,
	
	/**
	 * XML by MS Excel
	 */
	EXCEL_XML,
	
	/**
	 * CSV by Excel
	 */
	EXCEL_CSV;

}
