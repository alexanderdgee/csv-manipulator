package helpers;

import csv.CSV;
import parser.CSVParser;
import parser.CSVParserOptions;
import parser.InvalidCSVException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CSVUtils {
    public static void updateRowValues(String csvFileName, String column, String oldValue, String newValue)
            throws IOException, InvalidCSVException {
        if (csvFileName == null || column == null || oldValue == null || newValue == null) {
            throw new RuntimeException("String parameters must be non-null to update values successfully.");
        }
        if (csvFileName.isEmpty()) {
            throw new RuntimeException("Must specify a non-empty filename to update values successfully.");
        }
        CSVParserOptions options = new CSVParserOptions();
        CSV csv = readCSV(csvFileName, options);
        for (int columnIndex : columnsWithTitle(column, csv)) {
            updateColumn(columnIndex, csv, oldValue, newValue);
        }
        writeCSV(csvFileName, csv, options);
    }

    public static CSV readCSV(String fileName, CSVParserOptions options) throws InvalidCSVException, IOException {
        // TODO: read data at once and cope with different line separators
        String csvString = FileUtils.readLines(fileName).collect(Collectors.joining(System.lineSeparator()));
        CSVParser parser = new CSVParser();
        return parser.parse(csvString);
    }

    public static Collection<Integer> columnsWithTitle(String header, CSV csv) {
        return IntStream.range(0, csv.getHeaders().size())
                .filter((int i) -> header.equals(csv.getHeaders().get(i))).boxed().collect(Collectors.toSet());
    }

    public static void updateColumn(int columnIndex, CSV csv, String oldValue, String newValue) {
        for (List<String> row : csv.getRows()) {
            if (row.size() > columnIndex && oldValue.equals(row.get(columnIndex))) {
                row.set(columnIndex, newValue);
            }
        }
    }

    public static void writeCSV(String fileName, CSV csv, CSVParserOptions options) throws IOException {
        FileUtils.writeLines(fileName, csv.getSerialisedLines(String.valueOf(options.separator)));
    }
}
