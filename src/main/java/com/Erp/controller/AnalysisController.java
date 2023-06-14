package com.Erp.controller;

import com.Erp.dto.AllChartData;
import com.Erp.dto.FinancialChartData;
import com.Erp.dto.IncomeChartData;
import com.Erp.repository.FinancialRepository;
import com.Erp.repository.IncomeRepository;
import com.Erp.repository.MemberRepository;
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
    private final FinancialRepository financialRepository;
    private final MemberRepository memberRepository;


    @GetMapping(value = "/analyses/analysis")
    public String goAnalysis(){ return "/financial/analysisForm"; }

    @GetMapping("/analysis/getIncomeChartData")
    public @ResponseBody List<IncomeChartData> getIncomeChartDataList(){

        List<IncomeChartData> incomeChartData = new ArrayList<>();

        incomeChartData = incomeRepository.findChartDataList();

        return incomeChartData;
    }

    @GetMapping("/analysis/getFinancialChartData")
    public @ResponseBody List<FinancialChartData> getFinancialChartDataList(){

        List<FinancialChartData> financialChartData = new ArrayList<>();

        financialChartData = financialRepository.findChartDataList();

        return financialChartData;
    }

    @GetMapping("/analysis/getAllChartData")
    public @ResponseBody List<AllChartData> getAllChartDataList(){

        List<AllChartData> allChartData = new ArrayList<>();

        allChartData = incomeRepository.findChartDataList2();

        Long num = memberRepository.countDistinctById();

        for (AllChartData allChartData1 : allChartData){

            allChartData1.setMNum(num);
        }

        return allChartData;
    }
}
