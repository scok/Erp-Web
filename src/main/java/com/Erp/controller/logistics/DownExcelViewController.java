package com.Erp.controller.logistics;

import com.Erp.entity.logistics.Account;
import com.Erp.entity.logistics.Estimate;
import com.Erp.entity.logistics.Product;
import com.Erp.service.logistics.AccountService;
import com.Erp.service.logistics.EstimateService;
import com.Erp.service.logistics.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class DownExcelViewController{

    private final AccountService accountService;
    private final ProductService productService;
    private final EstimateService estimateService;

    @PostMapping(value = "/excel/download")
    public void toExcel(@RequestBody Map<String,Object> data, HttpServletResponse res) throws Exception {
        String mode = data.keySet().iterator().next();
        String values = data.get(mode).toString();

       /* excel sheet 생성 */
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet1"); // 엑셀 sheet 이름
        sheet.setDefaultColumnWidth(28); // 디폴트 너비 설정

        /* header font style*/
        XSSFFont headerXSSFFont = (XSSFFont) workbook.createFont();
        headerXSSFFont.setColor(new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}));

        /* header cell style */
        XSSFCellStyle headerXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();

        // 테두리 설정
        headerXssfCellStyle.setBorderLeft(BorderStyle.THIN);
        headerXssfCellStyle.setBorderRight(BorderStyle.THIN);
        headerXssfCellStyle.setBorderTop(BorderStyle.THIN);
        headerXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        // 배경 설정
        headerXssfCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 34, (byte) 37, (byte) 41}));
        headerXssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerXssfCellStyle.setFont(headerXSSFFont);

        /* body cell style */
        XSSFCellStyle bodyXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();

        // 테두리 설정
        bodyXssfCellStyle.setBorderLeft(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderRight(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderTop(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        /* body data*/

        if(mode.equals("account")){
            AccountExcel(values,sheet,headerXssfCellStyle,bodyXssfCellStyle);
        }else if(mode.equals("product")){
            ProductExcel(values,sheet,headerXssfCellStyle,bodyXssfCellStyle);
        }

        /* download*/
        res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        ServletOutputStream servletOutputStream = res.getOutputStream();

        workbook.write(servletOutputStream);
        workbook.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }

    public void AccountExcel(String values,Sheet sheet,XSSFCellStyle headerXssfCellStyle,XSSFCellStyle bodyXssfCellStyle) throws IllegalAccessException {
        List<Account> accountList = new ArrayList<Account>();

        String filter = values.substring(1,values.length()-1);
        for (String keyWord  : filter.split(",")){
            System.out.println(keyWord.trim());
            accountList.add(accountService.findByAcCode(keyWord.trim()));
        }

        /* header data */
        String headerNames[] = new String[]{"거래 구분","거래처 코드", "거래처 명", "거래처 대표자 명","거래처 업태","거래처 종목","사업자 등록 번호","거래처 주소","거래처 번호","거래처 팩스번호","거래처 사이트"};

        int rowCount = 0; // 데이터가 저장될 행

        Row headerRow = null;
        Cell headerCell = null;

        headerRow = sheet.createRow(rowCount++);
        for(int i=0; i<headerNames.length; i++) {
            headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headerNames[i]); // 데이터 추가
            headerCell.setCellStyle(headerXssfCellStyle); // 스타일 추가
        }

        Row bodyRow = null;
        Cell bodyCell = null;

        for(Account items : accountList) {
            bodyRow = sheet.createRow(rowCount++);
            int i = 0;

            // 객체의 필드 값을 String으로 분리
            Field[] fields = items.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 접근 가능하도록 설정
                Object value = field.get(items); // 필드 값 가져오기
                String stringValue = String.valueOf(value); // String으로 변환
                System.out.println(field.getName() + ": " + stringValue);

                bodyCell = bodyRow.createCell(i);
                bodyCell.setCellValue(stringValue); // 데이터 추가
                bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가

                if(i == 10){
                    break;
                }
                i++;
            }
        }
    }

    public void ProductExcel(String values,Sheet sheet,XSSFCellStyle headerXssfCellStyle,XSSFCellStyle bodyXssfCellStyle) throws IllegalAccessException {
        List<Product> productList = new ArrayList<Product>();

        String filter = values.substring(1,values.length()-1);
        for (String keyWord  : filter.split(",")){
            System.out.println(keyWord.trim());
            productList.add(productService.findByCode(keyWord.trim()));
        }

        String headerNames[] = new String[]{"상품 코드","상품 명", "단가", "제품 규격","상품 구분","상품 상세 구분"};

        int rowCount = 0; // 데이터가 저장될 행

        Row headerRow = null;
        Cell headerCell = null;

        headerRow = sheet.createRow(rowCount++);
        for(int i=0; i<headerNames.length; i++) {
            headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headerNames[i]); // 데이터 추가
            headerCell.setCellStyle(headerXssfCellStyle); // 스타일 추가
        }

        Row bodyRow = null;
        Cell bodyCell = null;

        for(Product items : productList) {
            bodyRow = sheet.createRow(rowCount++);
            int i = 0;

            // 객체의 필드 값을 String으로 분리
            Field[] fields = items.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 접근 가능하도록 설정
                Object value = field.get(items); // 필드 값 가져오기
                String stringValue = String.valueOf(value); // String으로 변환
                System.out.println(field.getName() + ": " + stringValue);

                bodyCell = bodyRow.createCell(i);
                bodyCell.setCellValue(stringValue); // 데이터 추가
                bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가
                if(i == 5){
                    break;
                }
                i++;
            }
        }
    }
}
