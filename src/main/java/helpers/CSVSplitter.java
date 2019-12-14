package helpers;

import parser.InvalidCSVException;

import java.util.ArrayList;
import java.util.List;

public class CSVSplitter {
    public static List<List<String>> split(String csvString, char quoteDelimiter, char separator) throws InvalidCSVException {
        // assumes quotes have highest priority
        // TODO: manage line separators from different systems
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        int startOfValue = 0;
        int i = 0;
        boolean insideQuotes = false;
        while (i < csvString.length()) {
            char c = csvString.charAt(i);
            if (c == quoteDelimiter) {
                // TODO: manage escaped quotes
                insideQuotes = !insideQuotes;
            } else if (!insideQuotes && c == separator) {
                row.add(csvString.substring(startOfValue, i));
                startOfValue = i + 1;
            } else if (!insideQuotes && substringMatch(csvString, System.lineSeparator(), i)) {
                row.add(csvString.substring(startOfValue, i));
                i += System.lineSeparator().length();
                startOfValue = i;
                rows.add(row);
                row = new ArrayList<>();
                continue;
            }
            i++;
        }
        if (insideQuotes) {
            throw new InvalidCSVException("Unmatched quotes detected");
        }
        if (startOfValue < csvString.length()) {
            row.add(csvString.substring(startOfValue));
        }
        if (row.size() > 0) {
            rows.add(row);
        }
        return rows;
    }

    private static boolean substringMatch(String input, String toCheckFor, int startIndex) {
        return toCheckFor.equals(input.substring(startIndex, startIndex + toCheckFor.length()));
    }

}
