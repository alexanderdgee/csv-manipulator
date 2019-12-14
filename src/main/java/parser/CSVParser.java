package parser;

import csv.CSV;

public class CSVParser {
    private CSVParserOptions options;

    public CSVParser() {
        this.options = new CSVParserOptions();
    }

    public CSVParser(CSVParserOptions options) {
        this.options = options;
    }

    public CSV parse(String csvString) {
        return null;
    }
}
