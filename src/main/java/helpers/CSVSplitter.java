package helpers;

import parser.InvalidCSVException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CSVSplitter {
    public static final char escape = '\\';

    public static List<List<String>> split(String csvString, char quoteDelimiter, char separator, boolean strictQuotePositions) throws InvalidCSVException {
        // assumes quotes have highest priority
        // TODO: cope with all UTF8 characters, not just those representable by single chars
        // TODO: manage line separators from different systems
        Set<Character> separators = new HashSet<>();
        for (char c : System.lineSeparator().toCharArray()) {
            separators.add(c);
        }
        separators.add(separator);
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        int startOfValue = 0;
        int i = 0;
        boolean insideQuotes = false;
        boolean encounteredSeparator = false;
        boolean encounteredLineSeparator = false;
        boolean escapeCharacter = false;
        while (i < csvString.length()) {
            char c = csvString.charAt(i);
            if (c == quoteDelimiter) {
                if (!insideQuotes) {
                    if (strictQuotePositions && !(i == 0 || encounteredSeparator || encounteredLineSeparator)) {
                        throwAt("Values must be either quoted or unquoted. Mixed value detected.", rows, row);
                    }
                    insideQuotes = true;
                } else if (!escapeCharacter) {
                    // quotation marks can be escaped: do not regard them as closing the quotation
                    if (strictQuotePositions && i + 1 < csvString.length()
                            && !separators.contains(csvString.charAt(i + 1))) {
                        // were inside quotes, but reached the end: but the next character is not a separator
                        throwAt("Values must be either quoted or unquoted. Mixed value detected.", rows, row);
                    }
                    insideQuotes = false;
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
                encounteredLineSeparator = true;
                row.add(csvString.substring(startOfValue, i));
                i += System.lineSeparator().length();
                startOfValue = i;
                rows.add(row);
                row = new ArrayList<>();
            } else {
                encounteredLineSeparator = false;
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

    private static void throwAt(String message, List<List<String>> rows, List<String> row) throws InvalidCSVException {
        throw new InvalidCSVException(String.format("%s Row %s, value %s.", message, rows.size() + 1, row.size() + 1));
    }
}
