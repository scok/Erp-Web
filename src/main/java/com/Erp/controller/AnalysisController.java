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

    // 분석 페이지 이동
    @GetMapping(value = "/analyses/analysis")
    public String goAnalysis(){ return "/financial/analysisForm"; }

    // 손익계산서 차트 데이터 불러오기
    @GetMapping("/analysis/getIncomeChartData")
    public @ResponseBody List<IncomeChartData> getIncomeChartDataList(){

        List<IncomeChartData> incomeChartData = new ArrayList<>();

        incomeChartData = incomeRepository.findChartDataList();

        return incomeChartData;
    }

    // 재무상태표 차트 데이터 불러오기
    @GetMapping("/analysis/getFinancialChartData")
    public @ResponseBody List<FinancialChartData> getFinancialChartDataList(){

        List<FinancialChartData> financialChartData = new ArrayList<>();

        financialChartData = financialRepository.findChartDataList();

        return financialChartData;
    }

    // 한눈에 보기 차트 데이터 불러오기
    @GetMapping("/analysis/getAllChartData")
    public @ResponseBody List<AllChartData> getAllChartDataList(){

        List<AllChartData> allChartData = new ArrayList<>();

        allChartData = incomeRepository.findChartDataList2();

        for (AllChartData allChartData1 : allChartData){

            Long num = memberRepository.countDistinctById(Integer.valueOf(allChartData1.getYear()));
            allChartData1.setMNum(num);
        }

        return allChartData;
    }
}
