package helpers;

import org.junit.Test;
import parser.CSVParserOptions;
import parser.InvalidCSVException;

import java.util.List;

import static org.junit.Assert.*;

public class CSVSplitterTest {
    CSVParserOptions defaultOptions = new CSVParserOptions();

    @Test
    public void testRecognisesSeparator() throws InvalidCSVException {
        String input = "thing,else";
        List<List<String>> split = CSVSplitter.split(input, defaultOptions.quoteDelimiter, defaultOptions.separator);
        assertLineCount(1, split);
        assertValueCountInRow(2, 0, split);
        assertValueAtPositionInRow("thing", 0, 0, split);
        assertValueAtPositionInRow("else", 0, 1, split);
    }

    @Test
    public void testRecognisesLineSeparator() throws InvalidCSVException {
        String input = "thing" + System.lineSeparator() + "else";
        List<List<String>> split = CSVSplitter.split(input, defaultOptions.quoteDelimiter, defaultOptions.separator);
        assertLineCount(2, split);
        assertValueCountInRow(1, 0, split);
        assertValueAtPositionInRow("thing", 0, 0, split);
        assertValueAtPositionInRow("else", 1, 0, split);
    }

    @Test
    public void testIgnoresQuotedSeparator() throws InvalidCSVException {
        String input = "\"thing,else\"";
        List<List<String>> split = CSVSplitter.split(input, defaultOptions.quoteDelimiter, defaultOptions.separator);
        assertLineCount(1, split);
        assertValueCountInRow(1, 0, split);
        assertValueAtPositionInRow(input, 0, 0, split);
    }

    @Test
    public void testIgnoresQuotedLineSeparator() throws InvalidCSVException {
        String input = "\"thing" + System.lineSeparator() + "else\"";
        List<List<String>> split = CSVSplitter.split(input, defaultOptions.quoteDelimiter, defaultOptions.separator);
        assertLineCount(1, split);
        assertValueCountInRow(1, 0, split);
        assertValueAtPositionInRow(input, 0, 0, split);
    }

    @Test
    public void testIgnoresEscapedQuotedQuote() throws InvalidCSVException {
        String input = "\"thing\\\"else\"";
        List<List<String>> split = CSVSplitter.split(input, defaultOptions.quoteDelimiter, defaultOptions.separator);
        assertLineCount(1, split);
        assertValueCountInRow(1, 0, split);
        assertValueAtPositionInRow(input, 0, 0, split);
    }

    @Test
    public void testDoesNotIgnoreQuotePrecededByEscapedEscape() throws InvalidCSVException {
        // The escape character is itself escaped
        String input = "\"thing\\\\\",\"else\"";
        List<List<String>> split = CSVSplitter.split(input, defaultOptions.quoteDelimiter, defaultOptions.separator);
        assertLineCount(1, split);
        assertValueCountInRow(2, 0, split);
        assertValueAtPositionInRow("\"thing\\\\\"", 0, 0, split);
        assertValueAtPositionInRow("\"else\"", 0, 0, split);
    }

    @Test
    public void testFinalCommaCreatesEmptyString() throws InvalidCSVException {
        String input = "thing,";
        List<List<String>> split = CSVSplitter.split(input, defaultOptions.quoteDelimiter, defaultOptions.separator);
        assertLineCount(1, split);
        assertValueCountInRow(2, 0, split);
        assertValueAtPositionInRow("thing", 0, 0, split);
        assertValueAtPositionInRow("", 0, 1, split);
    }

    @Test
    public void testFinalLineSeparatorIgnored() throws InvalidCSVException {
        String input = "thing" + System.lineSeparator();
        List<List<String>> split = CSVSplitter.split(input, defaultOptions.quoteDelimiter, defaultOptions.separator);
        assertLineCount(1, split);
        assertValueCountInRow(1, 0, split);
        assertValueAtPositionInRow("thing", 0, 0, split);
    }

    @Test(expected = InvalidCSVException.class)
    public void testUnmatchedQuotesThrows() throws InvalidCSVException {
        String input = "\"";
        CSVSplitter.split(input, defaultOptions.quoteDelimiter, defaultOptions.separator);
    }

    public void assertLineCount(int expected, List<List<String>> split) {
        assertEquals(expected, split.size());
    }

    public void assertValueCountInRow(int expected, int row, List<List<String>> split) {
        assertEquals(expected, split.get(row).size());
    }

    public void assertValueAtPositionInRow(String expected, int row, int position, List<List<String>> split) {
        assertEquals(expected, split.get(row).get(position));
    }
}
