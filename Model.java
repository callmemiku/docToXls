package core;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Model {

    private final List<Container> containers = new ArrayList<>();

    Logger logger = Logger.getLogger("Model logger");

    private List<Path> DOCX_FILES;
    private List<Path> DOC_FILES;

    public void setDOCX_FILES(List<Path> DOCX_FILES) {
        this.DOCX_FILES = DOCX_FILES;
    }

    public void setDOC_FILES(List<Path> DOC_FILES) {
        this.DOC_FILES = DOC_FILES;
    }

    public void gatherData() throws IOException {
        for(Path path:DOCX_FILES){
            logger.info(path.getFileName().toString().substring(0, path.getFileName().toString().length() - 5));
            containers.add(new Container(path.getFileName().toString(), new XWPFDocument(new FileInputStream(new File(path.toString())))));
        }
        for(Path path:DOC_FILES){
            logger.info(path.getFileName().toString().substring(0, path.getFileName().toString().length() - 4));
            containers.add(new Container(path.getFileName().toString().substring(0, path.getFileName().toString().length() - 4), new HWPFDocument(new POIFSFileSystem(new FileInputStream(new File(path.toString()))))));
        }
    }

    private CellStyle createCellStyle(CellStyle cellStyle){
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }


    // '\u0007' is the specific blank symbol that has to be removed or checked
    public void createXSSFs() {
        for (Container c : containers) {
            XSSFWorkbook current = c.getXssfWorkbook();
            CellStyle cellStyle = createCellStyle(current.createCellStyle());
            XSSFSheet currentSheet = current.createSheet();
            if (c.getXwpfDocument() == null) {
                Range range = c.getHwpfDocument().getRange();
                TableIterator tableIterator = new TableIterator(range);
                while (tableIterator.hasNext()) {
                    Table table = tableIterator.next();
                    for (int rowIndex = 0; rowIndex < table.numRows(); rowIndex++) {
                        TableRow tableRow = table.getRow(rowIndex);
                        if (tableRow.numCells() > 0) {
                            if (!tableRow.getCell(0).text().equals("\u0007") || !tableRow.getCell(0).text().equals("")) {
                                XSSFRow row = currentSheet.createRow(rowIndex);
                                for (int cellIndex = 0; cellIndex < tableRow.numCells(); cellIndex++) {
                                    TableCell tableCell = tableRow.getCell(cellIndex);
                                    if (!tableCell.text().equals("\u0007")) {
                                        XSSFCell cell = row.createCell(cellIndex, CellType.STRING);
                                        cell.setCellStyle(cellStyle);
                                        cell.setCellValue(tableCell.text());
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                for (XWPFTable table : c.getXwpfDocument().getTables()) {
                    for (int rowIndex = 0; rowIndex < table.getNumberOfRows(); rowIndex++) {
                        XWPFTableRow tableRow = table.getRows().get(rowIndex);
                        if (tableRow.getTableCells().size() > 0) {
                            if (!tableRow.getCell(0).getText().equals("\u0007") || !tableRow.getCell(0).getText().equals("")) {
                                XSSFRow row = currentSheet.createRow(rowIndex);
                                for (int cellIndex = 0; cellIndex < tableRow.getTableCells().size(); cellIndex++) {
                                    XWPFTableCell xwpfTableCell = tableRow.getTableCells().get(cellIndex);
                                    if (!xwpfTableCell.getText().equals("\u0007")) {
                                        XSSFCell cell = row.createCell(cellIndex, CellType.STRING);
                                        cell.setCellStyle(cellStyle);
                                        cell.setCellValue(xwpfTableCell.getText());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void cleanXSSFs() {
        for (Container c : containers) {
            XSSFSheet xssfSheet = c.getXssfWorkbook().getSheetAt(0);
            for (int rowIndex = 0; rowIndex < xssfSheet.getPhysicalNumberOfRows() + 100; rowIndex++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
                if (xssfRow != null) {
                    for (int cellIndex = 0; cellIndex < xssfRow.getLastCellNum() + 10; cellIndex++) {
                        XSSFCell xssfCell = xssfRow.getCell(cellIndex);
                        if (xssfCell != null) {
                            String cellValue = xssfCell.getStringCellValue();
                            cellValue = cellValue.replaceAll("\\u0007", "");
                            xssfCell.setCellValue(cellValue);
                        }
                    }
                }
            }
        }

        for (Container c: containers){
            XSSFSheet xssfSheet = c.getXssfWorkbook().getSheetAt(0);
            for (int i = 0; i < 10; i++) {
                xssfSheet.autoSizeColumn(i);
            }
            for (int rowIndex = 0; rowIndex < xssfSheet.getPhysicalNumberOfRows(); rowIndex++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
                if (xssfRow != null){
                    if (xssfRow.getCell(0) != null){
                        if (xssfRow.getCell(0).getStringCellValue().equals("")){
                            xssfSheet.removeRow(xssfRow);
                        }
                    }
                }
            }
        }
    }

    public void writeXSSFs(String path) throws IOException {
        for (Container c : containers) {
            String pathNew = String.format("%s\\%s.xlsx", path, c.getName());
            logger.info("File written: " + pathNew);
            FileOutputStream fileOutputStream = new FileOutputStream(pathNew);
            c.getXssfWorkbook().write(fileOutputStream);
            fileOutputStream.close();
        }
    }
}
