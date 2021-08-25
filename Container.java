package core;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Container {
    private final String name;
    private XWPFDocument xwpfDocument = null;
    private HWPFDocument hwpfDocument = null;
    private final XSSFWorkbook xssfWorkbook = new XSSFWorkbook();

    public Container(String name, XWPFDocument xwpfDocument) {
        this.name = name;
        this.xwpfDocument = xwpfDocument;
    }

    public Container(String name, HWPFDocument hwpfDocument) {
        this.name = name;
        this.hwpfDocument = hwpfDocument;
    }

    public String getName() {
        return name;
    }

    public XWPFDocument getXwpfDocument() {
        return xwpfDocument;
    }

    public HWPFDocument getHwpfDocument() {
        return hwpfDocument;
    }

    public XSSFWorkbook getXssfWorkbook() {
        return xssfWorkbook;
    }
}
