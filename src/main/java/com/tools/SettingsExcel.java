package com.tools;

import com.models.Predict;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

/**
 * Created by Admin on 22.02.2019.
 */
public class SettingsExcel {

    public SettingsExcel() {
    }

    public XSSFCellStyle createStyleForTitle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        return style;
    }

    public XSSFCellStyle createStyleForCellStandart(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }

    public void createCellForTitle(XSSFSheet sheet, String[] stringsName) {
        XSSFWorkbook workbook = sheet.getWorkbook();
        XSSFCellStyle style = createStyleForTitle(workbook);
        Cell cell;
        Row row = sheet.createRow(0);
        for (int i = 0; i < stringsName.length; i++) {
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(stringsName[i]);
            cell.setCellStyle(style);
        }
    }

    public void createCellRowMatrixRegionPrediction(XSSFSheet sheet, int[] matrix, double epsilon, int indentRow) {
        XSSFWorkbook workbook = sheet.getWorkbook();
        XSSFCellStyle styleStandart = createStyleForCellStandart(workbook);
        Cell cell;
        Row row;
        int matrixColumn = matrix.length;
        indentRow++;
        row = sheet.createRow(indentRow);
        cell = row.createCell(0, CellType.NUMERIC);
        cell.setCellValue(epsilon);
        cell.setCellStyle(styleStandart);
        for (int j = 1; j <= matrixColumn; j++) {
            cell = row.createCell(j, CellType.NUMERIC);
            cell.setCellValue(matrix[j - 1]);
            cell.setCellStyle(styleStandart);
        }
    }

    public void createMainCell(XSSFSheet sheet, ArrayList<Predict> predicts) {
        XSSFWorkbook workbook = sheet.getWorkbook();
        XSSFCellStyle styleStandart = createStyleForCellStandart(workbook);
        XSSFCellStyle styleDouble = createStyleForCellDouble(workbook);

        XSSFCellStyle styleAlphas = workbook.createCellStyle();
        styleAlphas.setBorderBottom(BorderStyle.THIN);
        styleAlphas.setBorderLeft(BorderStyle.THIN);
        styleAlphas.setBorderRight(BorderStyle.THIN);
        styleAlphas.setBorderTop(BorderStyle.THIN);
        styleAlphas.setWrapText(true);


        Cell cell;
        Row row;
        for (int i = 0; i < predicts.size(); i++) {

            row = sheet.createRow(i + 1);
            cell = row.createCell(0, CellType.NUMERIC);
            cell.setCellValue(predicts.get(i).getId());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellValue(predicts.get(i).getRealClass());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue(predicts.get(i).getPredictClass());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(3, CellType.NUMERIC);
            cell.setCellValue(predicts.get(i).getConfidence() * 100);
            cell.setCellStyle(styleDouble);

            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue(predicts.get(i).getCredibility() * 100);
            cell.setCellStyle(styleDouble);

            cell = row.createCell(5, CellType.NUMERIC);
            cell.setCellValue(predicts.get(i).getPPositive());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(6, CellType.NUMERIC);
            cell.setCellValue(predicts.get(i).getPNegative());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(7, CellType.NUMERIC);
            cell.setCellValue(predicts.get(i).getAlphaPositive());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(8, CellType.NUMERIC);
            cell.setCellValue(predicts.get(i).getAlphaNegative());
            cell.setCellStyle(styleStandart);

            cell = row.createCell(9, CellType.STRING);
            cell.setCellValue(predicts.get(i).getDataSetObjectsEntity().getParams());
            cell.setCellStyle(styleStandart);
        }
    }
    public XSSFCellStyle createStyleForCellDouble(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        DataFormat formatDouble = workbook.createDataFormat();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setDataFormat(formatDouble.getFormat("#0.00"));
        return style;
    }
}
