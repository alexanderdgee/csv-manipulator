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
        String simple = String.format("\"%s\",\"%s\"\n\"%s\",\"%s\"", header1, header2, entry1, entry2);
        CSVParser parser = new CSVParser();
        CSV csv = parser.parse(simple);
        assertEquals(header1, csv.getHeaders().get(0));
        assertEquals(header2, csv.getHeaders().get(1));
        assertEquals(entry1, csv.getRows().get(0).get(0));
        assertEquals(entry2, csv.getRows().get(0).get(1));
    }

    @Test
    public void testQuotedCSVWithSpaces() {
        String simple = String.format("\"%s\", \"%s\"\n\"%s\", \"%s\"", header1, header2, entry1, entry2);
        CSVParser parser = new CSVParser();
        CSV csv = parser.parse(simple);
        assertEquals(header1, csv.getHeaders().get(0));
        assertEquals(header2, csv.getHeaders().get(1));
        assertEquals(entry1, csv.getRows().get(0).get(0));
        assertEquals(entry2, csv.getRows().get(0).get(1));
    }

    @Test
    public void testQuotedCSVWithCommas() {
        String simple = String.format("\"%s\",\"%s\"\n\"%s\",\"%s\"", header1, headerWithCommas, entry1, entryWithCommas);
        CSVParser parser = new CSVParser();
        CSV csv = parser.parse(simple);
        assertEquals(header1, csv.getHeaders().get(0));
        assertEquals(headerWithCommas, csv.getHeaders().get(1));
        assertEquals(entry1, csv.getRows().get(0).get(0));
        assertEquals(entryWithCommas, csv.getRows().get(0).get(1));
    }

    @Test
    public void testCSVWithQuotedDelimitersOnly() {
        String simple = String.format("%s,\"%s\"\n%s,\"%s\"", header1, headerWithCommas, entry1, entryWithCommas);
        CSVParser parser = new CSVParser();
        CSV csv = parser.parse(simple);
        assertEquals(header1, csv.getHeaders().get(0));
        assertEquals(headerWithCommas, csv.getHeaders().get(1));
        assertEquals(entry1, csv.getRows().get(0).get(0));
        assertEquals(entryWithCommas, csv.getRows().get(0).get(1));
    }
}
