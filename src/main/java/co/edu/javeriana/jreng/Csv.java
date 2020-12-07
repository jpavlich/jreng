package co.edu.javeriana.jreng;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Csv {
    private Collection<String> columns = new ArrayList<>();
    private Collection<Collection<String>> data = new ArrayList<>();

    public void setColumns(Collection<String> columns) {
        this.columns.clear();
        this.columns.addAll(columns);
    }
    public void setColumns(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
    }
    
    public void addRow(Collection<String> row) {
        data.add(row);
    }

    public void addRow(String... cells) {
        data.add(Arrays.asList(cells));
    }

    public void save(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        if (!columns.isEmpty()) {
            writer.write(String.join("\t", columns));
            writer.write('\n');
        }
        for (Collection<String> row : data) {
            writer.write(String.join("\t", row));
            writer.write('\n');
        }
        writer.close();

    }

}
