package csv;

import java.util.List;

public interface CSV {
    public List<String> getHeaders();
    public int getRowCount();
    public List<List<String>> getRows();
}
