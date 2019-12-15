package parser;

public class CSVParserOptions {
    public char quoteDelimiter = '\"';
    public char separator = ',';
    public boolean constrainToHeaderSize = false;
    public boolean requireCompleteRows = false;
    public boolean strictQuotePositions = true;
}
