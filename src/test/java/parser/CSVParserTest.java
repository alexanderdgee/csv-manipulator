package parser;

import csv.CSV;
import org.junit.Test;

import static org.junit.Assert.*;

public class CSVParserTest {
    private String header1 = "test header 1";
    private String header2 = "test header 2";
    private String entry1 = "test entry 1";
    private String entry2 = "test entry 2";
    private String headerWithCommas = "test, header,";
    private String entryWithCommas = "test, entry,";

    @Test
    public void testParseSimpleCSV() throws InvalidCSVException {
        String simple = String.format("%s,%s\n%s,%s", header1, header2, entry1, entry2);
        CSVParser parser = new CSVParser();
        CSV csv = parser.parse(simple);
        assertEquals(1, csv.getRowCount());
        assertEquals(header1, csv.getHeaders().get(0));
        assertEquals(header2, csv.getHeaders().get(1));
        assertEquals(entry1, csv.getRows().get(0).get(0));
        assertEquals(entry2, csv.getRows().get(0).get(1));
    }

    @Test
    public void testParseQuotedCSV() throws InvalidCSVException {
        String quotedHeader1 = "\"" + header1 + "\"";
        String quotedHeader2 = "\"" + header2 + "\"";
        String quotedEntry1 = "\"" + entry1 + "\"";
        String quotedEntry2 = "\"" + entry2 + "\"";
        String simple = String.format("%s,%s\n%s,%s", quotedHeader1, quotedHeader2, quotedEntry1, quotedEntry2);
        CSVParser parser = new CSVParser();
        CSV csv = parser.parse(simple);
        assertEquals(quotedHeader1, csv.getHeaders().get(0));
        assertEquals(quotedHeader2, csv.getHeaders().get(1));
        assertEquals(quotedEntry1, csv.getRows().get(0).get(0));
        assertEquals(quotedEntry2, csv.getRows().get(0).get(1));
    }

    @Test
    public void testParseQuotedCSVWithSpaces() throws InvalidCSVException {
        // TODO: resolve whether this is actually permitted behaviour, or if quotes must be the first and last characters of a value, if present at all
        String quotedHeader1 = "\"" + header1 + "\"";
        String quotedHeader2 = " \"" + header2 + "\"";
        String quotedEntry1 = "\"" + entry1 + "\"";
        String quotedEntry2 = " \"" + entry2 + "\"";
        String simple = String.format("%s,%s\n%s,%s", quotedHeader1, quotedHeader2, quotedEntry1, quotedEntry2);
        CSVParser parser = new CSVParser();
        CSV csv = parser.parse(simple);
        assertEquals(quotedHeader1, csv.getHeaders().get(0));
        assertEquals(quotedHeader2, csv.getHeaders().get(1));
        assertEquals(quotedEntry1, csv.getRows().get(0).get(0));
        assertEquals(quotedEntry2, csv.getRows().get(0).get(1));
    }

    @Test
    public void testParseQuotedCSVWithCommas() throws InvalidCSVException {
        String quotedHeader1 = "\"" + header1 + "\"";
        String quotedHeader2 = "\"" + headerWithCommas + "\"";
        String quotedEntry1 = "\"" + entry1 + "\"";
        String quotedEntry2 = "\"" + entryWithCommas + "\"";
        String simple = String.format("%s,%s\n%s,%s", quotedHeader1, quotedHeader2, quotedEntry1, quotedEntry2);
        CSVParser parser = new CSVParser();
        CSV csv = parser.parse(simple);
        assertEquals(quotedHeader1, csv.getHeaders().get(0));
        assertEquals(quotedHeader2, csv.getHeaders().get(1));
        assertEquals(quotedEntry1, csv.getRows().get(0).get(0));
        assertEquals(quotedEntry2, csv.getRows().get(0).get(1));
    }

    @Test
    public void testParseCSVWithQuotedDelimitersOnly() throws InvalidCSVException {
        String quotedHeader2 = "\"" + headerWithCommas + "\"";
        String quotedEntry2 = "\"" + entryWithCommas + "\"";
        String simple = String.format("%s,%s\n%s,%s", header1, quotedHeader2, entry1, quotedEntry2);
        CSVParser parser = new CSVParser();
        CSV csv = parser.parse(simple);
        assertEquals(header1, csv.getHeaders().get(0));
        assertEquals(quotedHeader2, csv.getHeaders().get(1));
        assertEquals(entry1, csv.getRows().get(0).get(0));
        assertEquals(quotedEntry2, csv.getRows().get(0).get(1));
    }

    @Test
    public void testConstrainRetainsMissingEntryIfCompleteRowsNotRequired() throws InvalidCSVException {
        String moreHeaders = String.format("%s,%s\n%s", header1, header2, entry1);
        CSVParserOptions options = new CSVParserOptions();
        // make default behaviour explicit for the test
        options.requireCompleteRows = false;
        CSVParser parser = new CSVParser(options);
        CSV csv = parser.parse(moreHeaders);
        assertEquals(1, csv.getRowCount());
        assertEquals(header1, csv.getHeaders().get(0));
        assertEquals(header2, csv.getHeaders().get(1));
        assertEquals(entry1, csv.getRows().get(0).get(0));
    }

    @Test(expected = InvalidCSVException.class)
    public void testConstrainThrowsOnMissingEntryIfCompleteRowsRequired() throws InvalidCSVException {
        String moreHeaders = String.format("%s,%s\n%s", header1, header2, entry1);
        CSVParserOptions options = new CSVParserOptions();
        options.requireCompleteRows = true;
        CSVParser parser = new CSVParser(options);
        parser.parse(moreHeaders);
    }

    @Test
    public void testConstrainRetainsExtraEntryIfNotConstrained() throws InvalidCSVException {
        String lessHeaders = String.format("%s\n%s,%s", header1, entry1, entry2);
        CSVParserOptions options = new CSVParserOptions();
        // make default behaviour explicit for the test
        options.constrainToHeaderSize = false;
        CSVParser parser = new CSVParser(options);
        CSV csv = parser.parse(lessHeaders);
        assertEquals(header1, csv.getHeaders().get(0));
        assertEquals(entry1, csv.getRows().get(0).get(0));
        assertEquals(entry2, csv.getRows().get(0).get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConstrainDropsExtraEntryIfConstrained() throws InvalidCSVException {
        String lessHeaders = String.format("%s\n%s,%s", header1, entry1, entry2);
        CSVParserOptions options = new CSVParserOptions();
        options.constrainToHeaderSize = true;
        CSVParser parser = new CSVParser(options);
        CSV csv = parser.parse(lessHeaders);
        assertEquals(header1, csv.getHeaders().get(0));
        assertEquals(entry1, csv.getRows().get(0).get(0));
        csv.getRows().get(0).get(1);
    }
}
