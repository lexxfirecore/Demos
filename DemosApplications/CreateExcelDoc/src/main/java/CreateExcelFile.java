import java.io.FileOutputStream;

//Find jar from here "http://poi.apache.org/download.html"
import  java.io.*;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import  org.apache.poi.hssf.usermodel.HSSFSheet;
import  org.apache.poi.hssf.usermodel.HSSFWorkbook;
import  org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;

/**
 * Created by Alex Corghencea on 27 March 2018.
 */
public class CreateExcelFile {
    public static void main(String[] args) {
        try {
            String filename = "C:/NewExcelFile.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("FirstSheet");

            HSSFCellStyle style = workbook.createCellStyle();
         //   style.setFillForegroundColor(Short.valueOf(4 + ""));
            style.setFillBackgroundColor(Short.valueOf(1 + ""));
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);



            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell(0).setCellValue("No.");
            rowhead.createCell(1).setCellValue("Name");
            rowhead.createCell(2).setCellValue("Address");
            rowhead.createCell(3).setCellValue("Email");
            rowhead.setRowStyle(style);

            HSSFRow row = sheet.createRow((short) 1);
            row.createCell(0).setCellValue("1");

            row.createCell(1).setCellValue("Sankumarsingh");
            row.createCell(2).setCellValue("India");
            row.createCell(3).setCellValue("sankumarsingh@gmail.com");





            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            System.out.println("Your excel file has been generated!");

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}


