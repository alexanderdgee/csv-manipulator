package helpers;

import org.junit.Test;
import parser.InvalidCSVException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CSVUtilsTest {
    private String sample1 = "filename,origin,metadata,hash";
    private String sample2 = "file1,London,\"a file about London\",e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6";
    private String sample3 = "file2,Surrey,\"a file about The National Archives\",a4bf0d05d8805f8c35b633ee67dc10efd6efe1cb8dfc0ecdba1040b551564967";
    private String sample4 = "file55,Londom,\"London was initially incorrectly spelled as Londom\",e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6";
    private String sample4Corrected = "file55,London,\"London was initially incorrectly spelled as Londom\",e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6";
    private String sample5 = "file4,Penrith,\"Lake District National Park info\",a4bf0d05d8805f8c35b633ee67dc10efd6efe1cb8dfc0ecdba1040b551564968";
    private String sampleName = "sample.txt";

    @Test
    public void testUpdateRowValues() throws IOException, InvalidCSVException {
        createSample();
        CSVUtils.updateRowValues(sampleName, "origin", "Londom", "London");
        List<String> processedLines = FileUtils.readLines(sampleName).collect(Collectors.toList());
        assertEquals(5, processedLines.size());
        assertEquals(sample1, processedLines.get(0));
        assertEquals(sample2, processedLines.get(1));
        assertEquals(sample3, processedLines.get(2));
        assertEquals(sample4Corrected, processedLines.get(3));
        assertEquals(sample5, processedLines.get(4));
    }

    private void createSample() throws IOException {
        FileUtils.writeLines(sampleName, List.of(sample1, sample2, sample3, sample4, sample5));
    }
}
