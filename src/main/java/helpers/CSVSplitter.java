package helpers;

import parser.InvalidCSVException;

import java.util.ArrayList;
import java.util.List;

public class CSVSplitter {
    public static final char escape = '\\';

    public static List<List<String>> split(String csvString, char quoteDelimiter, char separator) throws InvalidCSVException {
        // assumes quotes have highest priority
        // TODO: cope with all UTF8 characters, not just those representable by single chars
        // TODO: manage line separators from different systems
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        int startOfValue = 0;
        int i = 0;
        boolean insideQuotes = false;
        boolean encounteredSeparator = false;
        boolean escapeCharacter = false;
        while (i < csvString.length()) {
            char c = csvString.charAt(i);
            if (c == quoteDelimiter) {
                if (!(insideQuotes && escapeCharacter)) {
                    // quotation marks have been escaped: do not regard them as closing the quotation
                    insideQuotes = !insideQuotes;
                }
            }
            if (!insideQuotes && c == separator) {
                encounteredSeparator = true;
                row.add(csvString.substring(startOfValue, i));
                startOfValue = i + 1;
            } else {
                encounteredSeparator = false;
            }
            if (insideQuotes && c == escape) {
                escapeCharacter = !escapeCharacter;
            } else {
                escapeCharacter = false;
            }
            if (!insideQuotes && substringMatch(csvString, System.lineSeparator(), i)) {
                row.add(csvString.substring(startOfValue, i));
                i += System.lineSeparator().length();
                startOfValue = i;
                rows.add(row);
                row = new ArrayList<>();
            } else {
                i++;
            }
        }
        if (insideQuotes) {
            throw new InvalidCSVException("Unmatched quotes detected");
        }
        if (encounteredSeparator) {
            // last character of file was a comma: last row should have an empty string
            row.add("");
        } else if (startOfValue < csvString.length()) {
            row.add(csvString.substring(startOfValue));
        }
        // ignore a final line separator, but include any final content
        if (row.size() > 0) {
            rows.add(row);
        }
        return rows;
    }

    private static boolean substringMatch(String input, String toCheckFor, int startIndex) {
        return toCheckFor.equals(input.substring(startIndex, startIndex + toCheckFor.length()));
    }
}
