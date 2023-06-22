package com.Erp.controller;

import com.Erp.dto.FinancialData;
import com.Erp.dto.FinancialDto;
import com.Erp.dto.SaveDto;
import com.Erp.dto.UserDto;
import com.Erp.repository.FinancialRepository;
import com.Erp.service.FinancialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
public class FinancialController {

    public final FinancialRepository financialRepository;
    @GetMapping(value = "/financials/financial")
    public String goFinancial(){ return "/financial/financialForm"; }

    @GetMapping("/financials/dataSetting")
    @ResponseBody
    public FinancialData getList(@Valid FinancialData data, @RequestParam MultiValueMap<String, String> formData) {

        Pattern pattern = Pattern.compile("\\d{4}");
        String searchValue = formData.get("columns[31][search][value]").toString();
        Matcher matcher = pattern.matcher(searchValue);

        Short year = 0;

        if(matcher.find()){
            String value = matcher.group(0);
            year = Short.parseShort(value);
        }else{
            System.out.println("데이터 없음");
        }

        int draw = Integer.parseInt(formData.get("draw").get(0));

        int total = (int)financialRepository.count();

        List<FinancialDto> dto = new ArrayList<>();

        if(year == 0){
            dto = financialService.findFinancialsList();
        }else{
            dto = financialService.findFinancialsList(year);
        }

        data.setDraw(draw);
        data.setRecordsFiltered(total);
        data.setRecordsTotal(total);
        data.setData(dto);

        return data;
    }

    private final FinancialService financialService;

    @PostMapping("/financials/dataAdd")
    public @ResponseBody ResponseEntity addData(@RequestBody FinancialDto dto, HttpServletRequest request){

        System.out.println("addData called");

        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute("User");

        Short year = 0;

        if(user.getDepartment().equals("재무팀")){
            year = financialService.addData(dto);
        }

        return new ResponseEntity(year, HttpStatus.OK);
    }

    @PostMapping("/financials/dataSave")
    public @ResponseBody ResponseEntity saveData(@RequestBody SaveDto dto) throws Exception{

        System.out.println("saveData called");

        Long id = financialService.updateData(dto);

        return new ResponseEntity(id, HttpStatus.OK);
    }
}
