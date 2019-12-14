package csv;

import java.util.List;

public class BasicCSV implements CSV {
    private List<String> headers;
    private List<List<String>> rows;

    public BasicCSV(List<String> headers, List<List<String>> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    @Override
    public List<String> getHeaders() {
        return headers;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public List<List<String>> getRows() {
        return rows;
    }
}
