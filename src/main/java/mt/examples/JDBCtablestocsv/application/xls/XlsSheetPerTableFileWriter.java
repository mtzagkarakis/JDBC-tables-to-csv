package mt.examples.JDBCtablestocsv.application.xls;

import mt.examples.JDBCtablestocsv.application.FileWriter;
import mt.examples.JDBCtablestocsv.domain.TableRepresentation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class XlsSheetPerTableFileWriter implements FileWriter, Closeable {
    private final Workbook xls;
    private final String pathname;
    private final boolean shouldAutoResizeColumns;
    public XlsSheetPerTableFileWriter(String pathname, boolean shouldAutoResizeColumns) {
        Objects.requireNonNull(pathname);
        this.xls = new XSSFWorkbook();
        if (!pathname.endsWith(".xlsx")){
            pathname = pathname.concat(".xlsx");
        }
        this.pathname = pathname;
        this.shouldAutoResizeColumns = shouldAutoResizeColumns;
    }

    @Override
    public void writeToFile(TableRepresentation tableRepresentation) {
        Sheet currentSheet = xls.createSheet(tableRepresentation.getTableName());

        Row header = currentSheet.createRow(0);
        writeCells(header, tableRepresentation.getColumName());

        int rowCounter = 1;
        for(List<String> rowValues: tableRepresentation.getRows()){
            Row valueRow = currentSheet.createRow(rowCounter++);
            writeCells(valueRow, rowValues);
        }

        if (shouldAutoResizeColumns){
            IntStream.range(0, tableRepresentation.getColumName().size())
                    .forEach(currentSheet::autoSizeColumn);
        }
    }

    private void writeCells(Row row, List<String> values){
        int columnCounter = 0;
        for (String value: values){
            row.createCell(columnCounter++).setCellValue(value);
        }
    }

    @Override
    public void close() throws IOException {
        try(FileOutputStream fileOut = new FileOutputStream(pathname)){
            xls.write(fileOut);
        } finally {
            xls.close();
        }
    }
}
