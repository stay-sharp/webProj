package com.ruiyang.du.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ExcelOperation {

    private static final String FROMAT_EXCEL = ".xlsx";
    private static final String SOURCE_EXCEL_PATH = "/Users/yp-tc-m-7122/Desktop/source2" + FROMAT_EXCEL;
    private static final String TARGET_EXCEL_PATH = "/Users/yp-tc-m-7122/Desktop/export/result-";
    private static final String DECRYPT_KEY = "817ABBC975B0626E";
    private static final int SIZE_OF_RESULT_EXCEL = 20;

    private static List<Integer> columnNeedDecrypt;
    static {
        columnNeedDecrypt = Arrays.asList(1,2,3,4,6,7,9);
    }



    public static void main(String[] args) {
        String sourceFilePath = SOURCE_EXCEL_PATH;
        try {
            XSSFWorkbook sourceWorkBook = readSourceFileToWorkBook(sourceFilePath);
            for (int i = 0; i < sourceWorkBook.getNumberOfSheets(); i++) {
                XSSFSheet sourceSheet = sourceWorkBook.getSheetAt(i);
                loopForRows(sourceSheet, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取源文档到workbook
     *
     * @param filePath
     */
    public static XSSFWorkbook readSourceFileToWorkBook(String filePath) throws IOException, InvalidFormatException {
        File sourceFile = new File(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(sourceFile);
        System.out.println("读取源文件到工作簿完毕");
        return workbook;
    }

//    /**
//     * 处理sheet,循环行
//     *
//     * @param sheet
//     * @param sheetIndex
//     */
//    private static void loopForRows(XSSFSheet sheet, int sheetIndex) throws Exception {
//        int totalRowNum = sheet.getLastRowNum();
//        if (totalRowNum < 1) {
//            return;
//        }
//        //按每SIZE_OF_RESULT_EXCEL行一个，构造多个子sheet
//        int sheetAmount = totalRowNum / SIZE_OF_RESULT_EXCEL + 1;
//        for (int s = 0; s < sheetAmount; s++) {
//            XSSFWorkbook targetWorkBook = new XSSFWorkbook();
//            XSSFSheet targetSheet = targetWorkBook.createSheet();
//            //处理行
//            int loopFrom = 0 + s * SIZE_OF_RESULT_EXCEL;
//            int loopTo = (s + 1) * SIZE_OF_RESULT_EXCEL > totalRowNum ? totalRowNum : (s + 1) * SIZE_OF_RESULT_EXCEL;
//            int newRowNum = 0;
//            for (int i = loopFrom; i < loopTo; i++) {
//                XSSFRow row = sheet.getRow(i);
//                XSSFRow targetRow = targetSheet.createRow(newRowNum);
//                System.out.println("开始循环总表行数据，当前第" + i + "行");
//                loopForCells(row, targetRow);
//                newRowNum++;
//            }
//            exportTargetToFile(targetWorkBook, TARGET_EXCEL_PATH + sheetIndex + "-" + s + FROMAT_EXCEL);
//            targetWorkBook.close();
//        }
//    }

    /**
     * 处理sheet,循环行
     *
     * @param sheet
     * @param sheetIndex
     */
    private static void loopForRows(XSSFSheet sheet, int sheetIndex) throws Exception {
        int totalRowNum = sheet.getLastRowNum();
        if (totalRowNum < 1) {
            return;
        }
        XSSFWorkbook targetWorkBook = new XSSFWorkbook();
        XSSFSheet targetSheet = targetWorkBook.createSheet();
        //处理行
        for (int i = 0; i < totalRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            XSSFRow targetRow = targetSheet.createRow(i);
            System.out.println("开始循环总表行数据，当前第" + i + "行");
            loopForCells(row, targetRow);
        }
        exportTargetToFile(targetWorkBook, TARGET_EXCEL_PATH + sheetIndex + FROMAT_EXCEL);
        targetWorkBook.close();

    }

    /**
     * 循环行内单元格
     *
     * @param sourceRow
     * @param targetRow
     * @return
     */
    private static void loopForCells(XSSFRow sourceRow, XSSFRow targetRow) {
        if (sourceRow == null || sourceRow.getPhysicalNumberOfCells() < 1) {
            return;
        }
        for (int i = 0; i < sourceRow.getPhysicalNumberOfCells(); i++) {
            XSSFCell sourceCell = sourceRow.getCell(i);
            XSSFCell targetCell = targetRow.createCell(i * 2);
            handlerCellContent(sourceCell, targetCell, i);
            XSSFCell targetSplitCell = targetRow.createCell(i * 2 + 1);
            targetSplitCell.setCellValue(",");
        }
    }


    /**
     * 处理单元格内容，包含敏感信息解密、特殊属性的判断
     *
     * @param sourceCell
     * @param targetCell
     * @param cellIndex
     */
    private static void handlerCellContent(XSSFCell sourceCell, XSSFCell targetCell, int cellIndex) {
        String sourceCellValue = sourceCell.getStringCellValue();
        boolean needDecrypt = columnNeedDecrypt.contains(cellIndex);
        if (needDecrypt) {
            //需解密的字段
            try {
                sourceCellValue = decrypt(sourceCellValue);
            } catch (Exception e) {
            }
        }
        targetCell.setCellValue(sourceCellValue);
    }

    private static void exportTargetToFile(XSSFWorkbook targetWorkBook, String targetFilePath) throws IOException {
        System.out.println("数据处理完毕，生成结果文件");
        File targetFile = new File(targetFilePath);
        FileOutputStream outputStream = new FileOutputStream(targetFile);
        targetWorkBook.write(outputStream);
        outputStream.close();
    }

    /**
     * AES解密
     *
     * @param source
     * @return
     */
    private static String decrypt(String source) {
        //return AES.decryptFromBase64(source, DECRYPT_KEY);
        return source;
    }


}
