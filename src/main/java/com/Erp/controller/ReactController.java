package com.Erp.controller;

import com.Erp.dto.ProductionChartDto;
import com.Erp.dto.UserDto;
import com.Erp.service.logistics.OrderSheetService;
import com.Erp.service.logistics.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReactController {

    private final ProductionService productionService;
    @GetMapping(value = "/react/productionForm")
    public List<ProductionChartDto> reactPFD(){

        LocalDate now = LocalDate.now();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        int year = now.getYear();
        String month =decimalFormat.format(now.getMonthValue());

        String lastDayOfMonth = decimalFormat.format(now.withDayOfMonth(now.lengthOfMonth()).getDayOfMonth());
        String startDayOfMonth = decimalFormat.format(1);

        String endDate = String.valueOf(year) +"-"+month+ "-"+lastDayOfMonth+"T00:00:00";
        String startDate = String.valueOf(year)+"-"+month+ "-"+startDayOfMonth+"T00:00:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter)
                .toLocalDate()
                .atStartOfDay();

        LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter)
                .toLocalDate()
                .atStartOfDay();

        List<ProductionChartDto> productFormDtos =  productionService.productionGetMonthChartData(startDateTime,endDateTime);
        System.out.println(productFormDtos.toString());

        return productFormDtos;
    }

    @GetMapping(value = "/userInfo")
    public Map<String,String> reactUIF(HttpServletRequest request){
        HttpSession session = request.getSession();
        Map<String, String> userInfo = new HashMap<String, String>();

        UserDto user = (UserDto) session.getAttribute("User");

        String userId = user.getId();
        String userName = user.getName();
        String userRole = String.valueOf(user.getRole());
        String userImage = user.getImageUrl();

        userInfo.put("userId", userId);
        userInfo.put("userName", userName);
        userInfo.put("userRole", userRole);
        userInfo.put("userImage", userImage);
        System.out.println(userInfo.toString());
        return userInfo;
    }
    @PostMapping(value = "/react/filterDate")
    public @ResponseBody ResponseEntity reactPFD(@RequestBody Map<String,LocalDateTime> request){
        // 시작 날짜와 종료 날짜를 받아옴
        LocalDateTime startDate = request.get("startDate");
        LocalDateTime endDate = request.get("endDate");

        // LocalDateTime에 9시간을 더하기
        //자바와 JavaScript 의 시간은 9시간 정도 차이가 나기 때문에 서로의 시간을 일치 시켜주기 위한 작업
        startDate = startDate.plusHours(9);
        endDate = endDate.plusHours(9);

        boolean isBefore = startDate.isBefore(endDate);
        if(isBefore == true){
            List<ProductionChartDto> productionChartDtos = productionService.productionGetChartDataFilter(startDate,endDate);
            if(productionChartDtos != null){
                return new ResponseEntity<>(productionChartDtos,HttpStatus.OK);
            }else {
                return new ResponseEntity<>("날짜를 다시 선택해주세요.",HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity<>("날짜를 다시 선택해주세요.",HttpStatus.BAD_REQUEST);
        }
    }
}
