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
        boolean separatorCharacterSeen = false;
        boolean escapeCharacterSeen = false;
        while (i < csvString.length()) {
            char c = csvString.charAt(i);
            if (c == quoteDelimiter) {
                if (!(insideQuotes && escapeCharacterSeen)) {
                    insideQuotes = !insideQuotes;
                }
            } else if (!insideQuotes && c == separator) {
                row.add(csvString.substring(startOfValue, i));
                i++;
                startOfValue = i;
                separatorCharacterSeen = true;
                continue;
            } else if (insideQuotes && c == escape) {
                escapeCharacterSeen = !escapeCharacterSeen;
                i++;
                continue;
            } else if (!insideQuotes && substringMatch(csvString, System.lineSeparator(), i)) {
                row.add(csvString.substring(startOfValue, i));
                i += System.lineSeparator().length();
                startOfValue = i;
                rows.add(row);
                row = new ArrayList<>();
                continue;
            }
            escapeCharacterSeen = false;
            separatorCharacterSeen = false;
            i++;
        }
        if (insideQuotes) {
            throw new InvalidCSVException("Unmatched quotes detected");
        }
        if (separatorCharacterSeen) {
            // last character of file was a comma: last row should have an empty string
            row.add("");
        } else if (startOfValue < csvString.length()) {
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
