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
    public void testSimpleCSV() {
        String simple = String.format("%s,%s\n%s,%s", header1, header2, entry1, entry2);
        CSVParser parser = new CSVParser();
        CSV csv = parser.parse(simple);
        assertEquals(1, csv.getRowCount());
        assertEquals(header1, csv.getHeaders().get(0));
        assertEquals(header2, csv.getHeaders().get(1));
        assertEquals(entry1, csv.getRows().get(0).get(0));
        assertEquals(entry2, csv.getRows().get(0).get(1));
    }

    @Test(expected = InvalidCSVException.class)
    public void testMissingEntryThrows() {
        String moreHeaders = String.format("%s,%s\n%s", header1, header2, entry1);
        CSVParser parser = new CSVParser();
        parser.parse(moreHeaders);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testExtraEntryIgnored() {
        String lessHeaders = String.format("%s\n%s,%s", header1, entry1, entry2);
        CSVParser parser = new CSVParser();
        CSV csv = parser.parse(lessHeaders);
        assertEquals(header1, csv.getHeaders().get(0));
        assertEquals(entry1, csv.getRows().get(0).get(0));
        csv.getRows().get(0).get(1);
    }

    @Test
    public void testQuotedCSV() {
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
    public void testQuotedCSVWithSpaces() {
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
    public void testQuotedCSVWithCommas() {
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
    public void testCSVWithQuotedDelimitersOnly() {
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
}
