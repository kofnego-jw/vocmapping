package trial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

public class CVSFormatTrial {

	@Test
	public void test() throws Exception {
		CSVFormat format = CSVFormat.DEFAULT
//				.withDelimiter(';')
				;
		CSVPrinter printer = new CSVPrinter(System.out, format);
		
		List<List<String>> table = new ArrayList<>();
		table.add(Arrays.asList("one", "two", "fünf, und weiter"));
		table.add(Arrays.asList("zwei", "vier", "\"\"seven\""));
		printer.printRecords(table);
		
		printer.close();
	}
	
}
