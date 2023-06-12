package com.Erp.controller;

import com.Erp.dto.ProductionChartDto;
import com.Erp.dto.UserDto;
import com.Erp.dto.logistics.ProductionFormDto;
import com.Erp.service.logistics.OrderSheetService;
import com.Erp.service.logistics.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReactController {


    private final OrderSheetService orderSheetService;
    private final ProductionService productionService;
    @GetMapping(value = "/productionForm")
    public List<ProductionChartDto> reactPFD(){

        List<ProductionChartDto> productFormDtos =  productionService.productionGetChartData();
        System.out.println(productFormDtos.toString());

        return productFormDtos;
    }

    @GetMapping(value = "/userInfo")
    public Map<String,String> reactUIF(HttpServletRequest request){
        HttpSession session = request.getSession();
        Map<String, String> userInfo = new HashMap<String, String>();

        UserDto user = (UserDto) session.getAttribute("User");
        String userName = user.getName();
        String userRole = String.valueOf(user.getRole());

        userInfo.put("userName", userName);
        userInfo.put("userRole", userRole);
        System.out.println(userInfo.toString());
        return userInfo;
    }
}
