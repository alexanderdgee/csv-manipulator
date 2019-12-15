package parser;

import csv.BasicCSV;
import csv.CSV;
import helpers.CSVSplitter;

import java.util.ArrayList;
import java.util.List;

public class CSVParser {
    private CSVParserOptions options;

    public CSVParser() {
        this.options = new CSVParserOptions();
    }

    public CSVParser(CSVParserOptions options) {
        this.options = options;
    }

    public CSV parse(String csvString) throws InvalidCSVException {
        List<List<String>> values = CSVSplitter.split(csvString, options.quoteDelimiter, options.separator);
        List<String> headers = values.remove(0);
        constrainToHeaders(headers, values);
        return new BasicCSV(headers, values);
    }

    public void constrainToHeaders(List<String> headers, List<List<String>> rows)
            throws InvalidCSVException {
        if (!options.requireCompleteRows && !options.constrainToHeaderSize) {
            return;
        }
        for (List<String> row : rows) {
            if (options.requireCompleteRows && row.size() < headers.size()) {
                throw new InvalidCSVException(String.format("Invalid number of columns on row %s",
                        rows.indexOf(row) + 1));
            }
            if (options.constrainToHeaderSize && row.size() > headers.size()) {
                row.subList(headers.size(), row.size()).clear();
            }
        }
    }
}
