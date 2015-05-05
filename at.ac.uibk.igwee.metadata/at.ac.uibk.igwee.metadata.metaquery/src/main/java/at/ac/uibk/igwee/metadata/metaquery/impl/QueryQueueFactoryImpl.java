package at.ac.uibk.igwee.metadata.metaquery.impl;

import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import at.ac.uibk.igwee.metadata.geonames.GeonameData;
import at.ac.uibk.igwee.metadata.geonames.OrgGeonames;
import at.ac.uibk.igwee.metadata.gnd.DeDnb;
import at.ac.uibk.igwee.metadata.gnd.GndVocabulary;
import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;
import at.ac.uibk.igwee.metadata.metaquery.QueryQueueColumnName;
import at.ac.uibk.igwee.metadata.metaquery.QueryQueueFactory;
import at.ac.uibk.igwee.metadata.metaquery.QueryQueueFormat;
import at.ac.uibk.igwee.metadata.metaquery.QueryServiceException;
import at.ac.uibk.igwee.metadata.metaquery.VocabularyQuery;
import at.ac.uibk.igwee.metadata.metaquery.VocabularyQueryResult;
import at.ac.uibk.igwee.metadata.viaf.OrgViaf;
import at.ac.uibk.igwee.metadata.viaf.ViafVocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.AbstractVocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;
import at.ac.uibk.igwee.metadata.wikidata.OrgWikidata;
import at.ac.uibk.igwee.metadata.wikidata.WikidataVocabulary;
import at.ac.uibk.igwee.xslt.XsltService;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

/**
 * Implementation class
 * @author joseph
 *
 */
@Component(provide=QueryQueueFactory.class)
public class QueryQueueFactoryImpl implements QueryQueueFactory {
	
	protected static final List<String> HEADER_COLUMNS = Arrays.asList(
			"Name", "Info", "Type", "URL"
			);
	
	/**
	 * XML-Prologue
	 */
	protected static final String XML_PROLOGUE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	/**
	 * Stylesheet location: /xsl/excelToQueryQueue.xsl
	 */
	protected static final String EXCEL_XSLT = "/xsl/excelToQueryQueue.xsl";
	
	/**
	 * Default Encoding of Excel: ISO 8859-1
	 */
	protected static final String EXCEL_DEFAULT_ENCODING = "ISO8859-1";
	
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryQueueFactoryImpl.class);
	
	/**
	 * XStream for serialization
	 */
	protected static final XStream XSTREAM;
	static {
		XSTREAM = new XStream();
		XSTREAM.setClassLoader(QueryQueue.class.getClassLoader());
		// TODO Configuration
		
		XSTREAM.addPermission(NoTypePermission.NONE);
		XSTREAM.addPermission(NullPermission.NULL);
		XSTREAM.addPermission(PrimitiveTypePermission.PRIMITIVES);
		XSTREAM.allowTypeHierarchy(Collection.class);
		XSTREAM.allowTypesByRegExp(new String[]{"java\\.lang\\.String",
				"at\\.ac\\.uibk\\.igwee\\.metadata\\..*"});
		
		XSTREAM.alias("queryQueue", QueryQueue.class);
		XSTREAM.alias("vocabularyQuery", VocabularyQuery.class);
		XSTREAM.addImplicitCollection(VocabularyQuery.class, "includedAuthority", Authority.class);
		XSTREAM.alias("vocabularyType", VocabularyType.class);
		XSTREAM.alias("vocabularyQueryResult", VocabularyQueryResult.class);
		XSTREAM.alias("geonameData", GeonameData.class);
		XSTREAM.omitField(AbstractVocabulary.class, "authority");
		XSTREAM.alias("viafVocabulary", ViafVocabulary.class);
		XSTREAM.alias("wikidataVocabulary", WikidataVocabulary.class);
		XSTREAM.alias("gndVocabulary", GndVocabulary.class);
		
		XSTREAM.alias("wikidata", OrgWikidata.class);
		XSTREAM.alias("geonames", OrgGeonames.class);
		XSTREAM.alias("viaf", OrgViaf.class);
		XSTREAM.alias("gnd", DeDnb.class);
	}
	
	
	private XsltService xsltService;
	@Reference
	public void setXsltService(XsltService s) {
		this.xsltService = s;
	}
	
	
	public QueryQueueFactoryImpl() {
		super();
	}
	
	@Override
	public String fromQueryQueueToXML(QueryQueue qq)
			throws QueryServiceException {
		
		String xml;
		try {
			xml = XSTREAM.toXML(qq);
		} catch (Exception e) {
			LOGGER.error("Cannot serialize QueryQueue to XML.", e);
			throw new QueryServiceException("Cannot serialize QueryQueue to XML: " + e.getMessage());
		}
		
		return XML_PROLOGUE + xml;
	}
	
	@Override
	public void writeQueryQueue(QueryQueue qq, OutputStream os)
			throws QueryServiceException {
		String content = fromQueryQueueToXML(qq);
		try {
			IOUtils.write(content, os, DEFAULT_ENCODING);
		} catch (Exception e) {
			LOGGER.error("Cannot write QueryQueueXML to OutputStream.", e);
			throw new QueryServiceException("Cannot write QueryQueueXML to OutputStream: " + e.getMessage());
		}
		
		
	}
	
	@Override
	public QueryQueue loadQueryQueue(InputStream in, QueryQueueFormat type,
			String encoding) throws QueryServiceException {
		
		if (type==QueryQueueFormat.EXCEL_XML)
			return fromExcelXML(in);
				
		String content;
		try {
			content = IOUtils.toString(in, encoding);
		} catch (Exception e) {
			LOGGER.error("Cannot read the InputStream as String.", e);
			throw new QueryServiceException("Cannot read the InputStream as String: " + e.getMessage());
		}
		
		switch(type) {
		case XML_QUERYQUEUE: 
			return (QueryQueue) XSTREAM.fromXML(content);
			
		case TEXT:
			return fromText(content);
			
		case CSV:
			return fromCSV(content, CSVFormat.RFC4180);
			
		case EXCEL_CSV:
			return fromCSV(content, CSVFormat.EXCEL.withDelimiter(';'));
			
		default:
			throw new QueryServiceException("This should never happen. The Excel clause "
					+ "should be caught earlier.");
		}
	}
		
	protected QueryQueue fromExcelXML(InputStream in) throws QueryServiceException {
		InputStream qqXml;
		try {
			qqXml = xsltService.doXslt(in, getClass().getResourceAsStream(EXCEL_XSLT), null);
		} catch (Exception e) {
			LOGGER.error("Cannot perform the excel to queryQueue XSLT.", e);
			throw new QueryServiceException("Cannot perform the Excel to QueryQueue XSLT: " 
					+ e.getMessage());
		}
		return (QueryQueue) XSTREAM.fromXML(qqXml);
	}
	
	/**
	 * Converts the text first to CSV then parse the CSV
	 * @param text
	 * @return
	 * @throws QueryServiceException
	 */
	protected QueryQueue fromText(String text) throws QueryServiceException {
		
		String[] ls = text.split("\\r?\\n");
		List<String> lines = 
				Stream.of(ls).filter(l -> !l.isEmpty()).collect(Collectors.toList());
		
		String csv = lines.stream().map(l -> "\"" + l.replaceAll("\"", "\\\"").replaceAll("\\t", "\",\""))
			.collect(Collectors.joining("\n"));
		
		return fromCSV(csv, CSVFormat.RFC4180);
	}
	
	/**
	 * Convert CSV to QueryQueue. Use QueryQueueColumnName 
	 * @param csv
	 * @return
	 * @throws QueryServiceException
	 */
	protected QueryQueue fromCSV(String csv, CSVFormat format) throws QueryServiceException {
		StringReader sr = new StringReader(csv);
		CSVParser parser;
		try {
			parser = format.withHeader().parse(sr);
		} catch (Exception e) {
			LOGGER.error("Cannot parse CSV.", e);
			throw new QueryServiceException("Cannot parse CSV: " + e.getMessage());
		}
		
		List<VocabularyQuery> qList;
		
		try { qList = parser.getRecords()
				.stream()
				.map(QueryQueueFactoryImpl::convertToVocabularyQuery)
				.collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("Cannot parse the CSV file.", e);
			throw new QueryServiceException("Cannot parse the CSV file: " + e.getMessage());
		}
		
		return new QueryQueue("queryqueue", "queryqueue from csv", qList);
		
	}
	
	@Override
	public String exportResult(QueryQueue qq, char deliminator) throws QueryServiceException {
		
		if (qq.getResults()==null || qq.getResults().isEmpty())
			throw new QueryServiceException("No results found, cannot export mapping.");
		
		List<VocabularyQueryResult> fixed =
				qq.getResults().stream().filter(vqr -> vqr.getFixedResult()!=null)
				.collect(Collectors.toList());
		
		if (fixed.isEmpty())
			throw new QueryServiceException("No fixed results found, cannot export mapping.");
		
		CSVPrinter printer = null;
		CharArrayWriter writer = new CharArrayWriter();
		
		try {
			
			CSVFormat format = CSVFormat.DEFAULT.withDelimiter(deliminator);
			
			printer = new CSVPrinter(writer, format);
			
			printer.printRecord(HEADER_COLUMNS);
			
			for (VocabularyQueryResult vqr: fixed) {
				List<String> list = exportToStringList(vqr);
				printer.printRecord(list);
			}			
		} catch (Exception e) {
			LOGGER.error("Cannot export to CSV.", e);
			throw new QueryServiceException("Cannot export CSV data: " + e.getMessage());
		} finally {
			if (printer!=null) {
				try {
					printer.close();
				} catch (Exception ignored) {
					LOGGER.warn("Error while closing the CSVPrinter.", ignored);
				}
			}
		}
		
		return writer.toString();
	}
	
	/**
	 * List: Name, Info, Type, URL
	 * @param r
	 * @return
	 */
	protected static List<String> exportToStringList(VocabularyQueryResult r) {
		Vocabulary voc = r.getFixedResult();
		assert(voc!=null);
		return Arrays.asList(
				r.getVocabularyQuery().getName(),
				r.getVocabularyQuery().getAdditionalInfo(),
				voc.getVocabularyType().toString(),
				voc.getURI().toString()
				);
	}
	
	/**
	 * 
	 * @param rec
	 * @return
	 */
	protected static VocabularyQuery convertToVocabularyQuery(CSVRecord rec) {
		String name;
		try {
			name = rec.get(QueryQueueColumnName.KEY.getColumnTitle());
		} catch (Exception e) {
			try {
				name = rec.get(QueryQueueColumnName.NAME.getColumnTitle());
			} catch (Exception e2) {
				name = "";
			}
		}
		
		String query;
		try {
			query = rec.get(QueryQueueColumnName.QUERY.getColumnTitle());
		} catch (Exception e) {
			if (!name.isEmpty()) 
				query = name;
			else 
				query = "";
		}
		
		String info;
		
		try {
			info = rec.get(QueryQueueColumnName.INFO.getColumnTitle());
		} catch (Exception e) {
			if (!query.isEmpty())
				info = query;
			else if (!name.isEmpty())
				info = name;
			else
				info = "";
		}
		
		VocabularyType type = null;
		
		try {
			String typeString = rec.get(QueryQueueColumnName.TYPE.getColumnTitle()).toLowerCase();
			
			if (typeString.contains("pers"))
				type = VocabularyType.PERSONAL_NAME;
			else if (typeString.contains("place") || typeString.contains("geo") 
					|| typeString.contains("loc"))
				type = VocabularyType.PLACE_NAME;
			else if (typeString.contains("corp") || typeString.contains("inst"))
				type = VocabularyType.INSTITUTION_NAME;
			
		} catch (Exception e) {
			type = VocabularyType.UNKNOWN;
		}
		
		String auths = null;
		try {
			auths = rec.get(QueryQueueColumnName.AUTHORITY.getColumnTitle());
		} catch (Exception e) {
			auths = null;
		}
		
		
		return new VocabularyQuery(name, query, info, type, parseAuthorities(auths));
	}
	
	/**
	 * TODO: Parse Authority Strings
	 * 
	 * 
	 * @param auth
	 * @return
	 */
	protected static Set<Authority> parseAuthorities(String auth) {
		if (auth==null || auth.isEmpty()) return null;
		String[] auths = auth.trim().toLowerCase()
				.split("[^a-z]+");
		
		return Stream.of(auths).map(s -> {
			if (s.contains("geonames"))
				return OrgGeonames.getInstance();
			else if (s.contains("viaf"))
				return OrgViaf.getInstance();
			else if (s.contains("wikidata"))
				return OrgWikidata.getInstance();
			else if (s.contains("gnd"))
				return DeDnb.getInstance();
			else
				return null;
		}).filter(a -> a!=null)
		.collect(Collectors.toSet());
	}
	

}
