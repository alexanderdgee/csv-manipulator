package helpers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {
    public static Stream<String> readLines(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        return Files.lines(path);
    }

    public static String relativePath(String pathString) {
        return "src/test/resources/" + pathString;
    }

    public static void writeLines(String pathString, List<String> lines) throws IOException {
        Path path = Paths.get(pathString);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (String line : lines) {
                writer.write(line);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
