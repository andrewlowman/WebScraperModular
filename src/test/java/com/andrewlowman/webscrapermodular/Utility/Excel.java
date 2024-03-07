package com.andrewlowman.webscrapermodular.Utility;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Excel {
    File excelFile;

    public void setExcelFile(File excelFile) {
        this.excelFile = excelFile;
    }

    public void creatSheet(String dept) throws IOException{
        //for error about zip bombs
        ZipSecureFile.setMinInflateRatio(0);

        //open line to book
        FileInputStream fileInputStream = new FileInputStream(excelFile);
        XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);

        XSSFSheet sheet = workBook.createSheet(dept);

        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("Name");

        cell = row.createCell(1);
        cell.setCellValue("Title");


        cell = row.createCell(2);
        cell.setCellValue("Location");


        cell = row.createCell(3);
        cell.setCellValue("Mailcode");

        fileInputStream.close();

        FileOutputStream fileOutputStream = new FileOutputStream(excelFile);
        workBook.write(fileOutputStream);
        workBook.close();
        fileOutputStream.close();
    }

    public void writeToBook(String name, String title, String dept, String location, String mailcode) throws IOException {
        //for error about zip bombs
        ZipSecureFile.setMinInflateRatio(0);

        //open line to book
        FileInputStream fileInputStream = new FileInputStream(excelFile);
        XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);

        XSSFSheet sheet = workBook.getSheet(dept);

        int lastRow = sheet.getLastRowNum();

        Row row = sheet.createRow(lastRow + 1);

        //name
        Cell cell = row.createCell(0);
        cell.setCellValue(name);

        //title
        cell = row.createCell(1);
        cell.setCellValue(title);

        //location
        cell = row.createCell(2);
        cell.setCellValue(location);

        //mailcode
        cell = row.createCell(3);
        cell.setCellValue(mailcode);

        fileInputStream.close();

        FileOutputStream fileOutputStream = new FileOutputStream(excelFile);
        workBook.write(fileOutputStream);
        workBook.close();
        fileOutputStream.close();

    }

    public boolean checkIfSheetCreatedAlready(String dept) throws IOException {
        //for error about zip bombs
        ZipSecureFile.setMinInflateRatio(0);

        //open line to book
        FileInputStream fileInputStream = new FileInputStream(excelFile);
        XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);

        //System.out.println("Department name in check if sheet created: " + dept);

        //create a new sheet if sheet with dept name doesn't exist
        if(workBook.getSheetIndex(dept) == -1){
            return false;
        }

        return true;
    }

    public void createPivotTable(String dept) throws IOException {
        //for error about zip bombs
        ZipSecureFile.setMinInflateRatio(0);

        //open line to book
        FileInputStream fileInputStream = new FileInputStream(excelFile);
        XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);

        //System.out.println("Department name in create pivot table: " + dept);

        XSSFSheet sheet = workBook.getSheet(dept);

        //pivot sheet
        XSSFSheet pivotSheet = workBook.createSheet("Pivot" + dept);
        AreaReference areaReference = new AreaReference("B:D", SpreadsheetVersion.EXCEL2007);
        XSSFPivotTable pivotTable = pivotSheet.createPivotTable(areaReference, new CellReference("A1"), sheet);

        pivotTable.addRowLabel(0);
        pivotTable.addRowLabel(1);
        pivotTable.addRowLabel(2);

        fileInputStream.close();

        FileOutputStream fileOutputStream = new FileOutputStream(excelFile);
        workBook.write(fileOutputStream);
        workBook.close();
        fileOutputStream.close();
    }
}
