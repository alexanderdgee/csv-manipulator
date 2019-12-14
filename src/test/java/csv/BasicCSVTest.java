package csv;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BasicCSVTest {
    @Test
    public void testRowCount() {
        List<String> empty = new ArrayList<>();
        BasicCSV csv = new BasicCSV(null, List.of(empty, empty));
        assertEquals(2, csv.getRowCount());
    }

    @Test(expected = NullPointerException.class)
    public void testRowCountWhenNullThrows() {
        BasicCSV csv = new BasicCSV(null, null);
        csv.getRowCount();
    }
}
