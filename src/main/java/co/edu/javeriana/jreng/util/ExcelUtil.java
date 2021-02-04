package co.edu.javeriana.jreng.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtil {

    public Sheet createSheet(Workbook wb, String sheetName, Iterable<List<String>> data, String... columns) { // https://mkyong.com/java/apache-poi-reading-and-writing-excel-file-in-java/
        
        Sheet sh = wb.createSheet(sheetName);

        int rowNum = 0;
        // header
        Row header = sh.createRow(rowNum++);
        for (int i = 0; i < columns.length; i++) {
            header.createCell(i).setCellValue(columns[i]);
        }

        for (List<String> tuple : data) {
            Row row = sh.createRow(rowNum++);
            int colNum = 0;
            for (String val : tuple) {
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(val);
            }
        }

        return sh;

    }

    public void save(Workbook wb, String filename) {
        try {
            FileOutputStream outputStream = new FileOutputStream(filename);
            wb.write(outputStream);
            wb.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
