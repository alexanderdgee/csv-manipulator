package helpers;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class FileUtilsTest {
    private String sample1 = "This is a string.";
    private String sample2 = "This is a different string.";
    private String path = FileUtils.relativePath("example.txt");

    @Test
    public void testWriteRead() throws IOException {
        FileUtils.writeLines(path, List.of(sample1, sample2));
        List<String> strings = FileUtils.readLines(path).collect(Collectors.toList());
        assertEquals(2, strings.size());
        assertEquals(sample1, strings.get(0));
        assertEquals(sample2, strings.get(1));
    }
}
