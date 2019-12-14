package parser;

import csv.BasicCSV;
import csv.CSV;
import helpers.CSVSplitter;

import java.util.List;

public class CSVParser {
    private CSVParserOptions options;

    public CSVParser() {
        this.options = new CSVParserOptions();
    }

    public CSVParser(CSVParserOptions options) {
        this.options = options;
    }

    public CSV parse(String csvString) {
        List<List<String>> values = null;
        try {
            values = CSVSplitter.split(csvString, options.quoteDelimiter, options.separator);
        } catch (InvalidCSVException e) {
            throw new RuntimeException(e);
        }
        List<String> headers = values.remove(0);
        return new BasicCSV(headers, values);
    }
}
