package csv;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<String> getSerialisedLines(String separator) {
        List<String> output = new ArrayList<>();
        output.add(String.join(separator, getHeaders()));
        output.addAll(getRows().stream().map((List<String> row) -> String.join(separator, row))
                .collect(Collectors.toList()));
        return output;
    }
}
