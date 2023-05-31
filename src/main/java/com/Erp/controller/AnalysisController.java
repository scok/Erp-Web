package com.Erp.controller;

import com.Erp.dto.IncomeChartData;
import com.Erp.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AnalysisController {

    private final IncomeRepository incomeRepository;

    @GetMapping(value = "/analyses/analysis")
    public String goAnalysis(){ return "/financial/analysisForm"; }

    @GetMapping("/analysis/getIncomeChartData")
    public @ResponseBody List<IncomeChartData> getIncomeChartDataList(){

        List<IncomeChartData> incomeChartData = new ArrayList<>();

        incomeChartData = incomeRepository.findChartDataList();

        return incomeChartData;
    }
}
