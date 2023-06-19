package com.Erp.controller.logistics;

import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.InventoryFormDto;
import com.Erp.dto.logistics.MaterialDeliveryFormDto;
import com.Erp.dto.logistics.ProductionFormDto;
import com.Erp.dto.logistics.WarehousingInAndOutFormDto;
import com.Erp.entity.logistics.*;
import com.Erp.service.logistics.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class DownExcelViewController{

    private final AccountService accountService;
    private final ProductService productService;
    private final MaterialDeliveryService materialDeliveryService;
    private final ProductionService productionService;
    private final LogisticsService logisticsService;

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
        }else if(mode.equals("materialDelivery")){
            MaterialDeliveryExcel(values,sheet,headerXssfCellStyle,bodyXssfCellStyle);
        }else if(mode.equals("production")){
            ProductionExcel(values,sheet,headerXssfCellStyle,bodyXssfCellStyle);
        }else if(mode.equals("warehousing")){
            WarehousingExcel(values,sheet,headerXssfCellStyle,bodyXssfCellStyle);
        }else if(mode.equals("inventory")){
            InventoryExcel(values,sheet,headerXssfCellStyle,bodyXssfCellStyle);
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

    public void MaterialDeliveryExcel(String values,Sheet sheet,XSSFCellStyle headerXssfCellStyle,XSSFCellStyle bodyXssfCellStyle) throws IllegalAccessException {
        List<MaterialDeliveryFormDto> materialDeliveries = new ArrayList<MaterialDeliveryFormDto>();

        String filter = values.substring(1,values.length()-1);
        for (String keyWord  : filter.split(",")){

            MaterialDeliveryFormDto materialDeliveryFormDto = MaterialDeliveryFormDto.of(materialDeliveryService.findById(keyWord.trim()));

            materialDeliveries.add(materialDeliveryFormDto);
        }

        String headerNames[] = new String[]{"자재 불출 ID","불출 일자", "자재 명","불출 수량","창고","구역", "불출 라인","등록인"};

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

        for(MaterialDeliveryFormDto items : materialDeliveries) {
            bodyRow = sheet.createRow(rowCount++);
            int i = 0;

            // 객체의 필드 값을 String으로 분리
            Field[] fields = items.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 접근 가능하도록 설정
                Object value = field.get(items); // 필드 값 가져오기
                String stringValue = String.valueOf(value); // String으로 변환

                bodyCell = bodyRow.createCell(i);
                bodyCell.setCellValue(stringValue); // 데이터 추가
                bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가

                i++;
            }
        }
    }

    public void ProductionExcel(String values,Sheet sheet,XSSFCellStyle headerXssfCellStyle,XSSFCellStyle bodyXssfCellStyle) throws IllegalAccessException {
        List<ProductionFormDto> productionFormDtos = new ArrayList<ProductionFormDto>();

        String filter = values.substring(1,values.length()-1);
        for (String keyWord  : filter.split(",")){

            ProductionFormDto productionFormDto = ProductionFormDto.of(productionService.findById(keyWord.trim()));

            productionFormDtos.add(productionFormDto);
        }

        String headerNames[] = new String[]{"생산 실적 ID","조립 라인", "등록자 명","수량","제품 명","창고 명", "구역 명","등록 일자"};

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

        for(ProductionFormDto items : productionFormDtos) {
            bodyRow = sheet.createRow(rowCount++);
            int i = 0;

            // 객체의 필드 값을 String으로 분리
            Field[] fields = items.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 접근 가능하도록 설정
                Object value = field.get(items); // 필드 값 가져오기
                String stringValue = String.valueOf(value); // String으로 변환

                bodyCell = bodyRow.createCell(i);
                bodyCell.setCellValue(stringValue); // 데이터 추가
                bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가
                if(i == 7){
                    break;
                }
                i++;
            }
        }
    }
    public void WarehousingExcel(String values,Sheet sheet,XSSFCellStyle headerXssfCellStyle,XSSFCellStyle bodyXssfCellStyle) throws IllegalAccessException {
        List<WarehousingInAndOutFormDto> warehousingInAndOutFormDtos = new ArrayList<WarehousingInAndOutFormDto>();

        String filter = values.substring(1,values.length()-1);
        for (String keyWord  : filter.split(",")){
            WarehousingInAndOutFormDto warehousingInAndOutFormDto = WarehousingInAndOutFormDto.of(logisticsService.widFindById(keyWord.trim()));
            warehousingInAndOutFormDtos.add(warehousingInAndOutFormDto);
        }

        /* header data */
        String headerNames[] = new String[]{"입고,출고 코드", "거래처 명", "창고 명","구역","상품 명","수량","입고, 출고 일자","상태"};

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

        for(WarehousingInAndOutFormDto items : warehousingInAndOutFormDtos) {
            bodyRow = sheet.createRow(rowCount++);
            int i = 0;

            // 객체의 필드 값을 String으로 분리
            Field[] fields = items.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 접근 가능하도록 설정
                Object value = field.get(items); // 필드 값 가져오기
                String stringValue = String.valueOf(value); // String으로 변환

                bodyCell = bodyRow.createCell(i);
                bodyCell.setCellValue(stringValue); // 데이터 추가
                bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가

                if(i == 7){
                    break;
                }
                i++;
            }
        }
    }

    public void InventoryExcel(String values,Sheet sheet,XSSFCellStyle headerXssfCellStyle,XSSFCellStyle bodyXssfCellStyle) throws IllegalAccessException {
        List<InventoryFormDto> inventoryFormDtos = new ArrayList<InventoryFormDto>();

        String filter = values.substring(1,values.length()-1);
        for (String keyWord  : filter.split(",")){
            InventoryFormDto inventoryFormDto = InventoryFormDto.of(logisticsService.inFindById(keyWord.trim()));
            inventoryFormDtos.add(inventoryFormDto);
        }

        /* header data */
        String headerNames[] = new String[]{"재고 코드", "상품 코드", "상품 명","규격","총수량","거래처 명","창고 명","창고 분류","구역"};

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

        for(InventoryFormDto items : inventoryFormDtos) {
            bodyRow = sheet.createRow(rowCount++);
            int i = 0;

            // 객체의 필드 값을 String으로 분리
            Field[] fields = items.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 접근 가능하도록 설정
                Object value = field.get(items); // 필드 값 가져오기
                String stringValue = String.valueOf(value); // String으로 변환

                bodyCell = bodyRow.createCell(i);
                bodyCell.setCellValue(stringValue); // 데이터 추가
                bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가

                if(i == 8){
                    break;
                }
                i++;
            }
        }
    }
}
