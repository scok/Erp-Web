// result 배열에서 y축 데이터를 추출하는 함수
function getYAxisData(dataArray, property) {
  return dataArray.map(item => item[property]);
}

document.addEventListener('DOMContentLoaded', function(){

    // 포괄 손익계산서 데이터 불러오기
    $.ajax({
        url:'/analysis/getIncomeChartData',
        type:'GET',
        success:function(data){

            console.log('데이터 불러오기 성공');
            console.log(data);

            // 주요 재무항목 차트
            const summary = {};

            data.forEach(item => {
                const year = item.year;
                if(!summary[year]){
                    summary[year] = {
                        salesRevenueSum: 0,
                        operateIncome: 0,
                        netIncomeSum: 0,
                        year: item.year
                    }
                }

                summary[year].salesRevenueSum += item.sales_revenue;
                summary[year].operateIncome += item.operate_revenue - item.operate_expenses;
                summary[year].netIncomeSum += item.netIncome;
            });

            const result = Object.values(summary);

            // result 배열에서 각 속성별로 y축 데이터 추출
            const salesRevenueData = getYAxisData(result, 'salesRevenueSum');
            const operateIncomeData = getYAxisData(result, 'operateIncome');
            const netIncomeData = getYAxisData(result, 'netIncomeSum');

            // result 배열에서 x축 레이블 추출
            const xAxisLabels = result.map(item => String(item.year));

            const operateIncomeRatio = [];

            for(let i=0; i < salesRevenueData.length; i++){
                let data = (operateIncomeData[i] / salesRevenueData[i]) * 100;
                operateIncomeRatio.push(data);
            }

            const netIncomeRatio = [];

            for(let i=0; i < salesRevenueData.length; i++){
                let data = (netIncomeData[i] / salesRevenueData[i]) * 100;
                netIncomeRatio.push(data);
            }

            drawKeyFinancialChart(xAxisLabels, salesRevenueData, operateIncomeData, netIncomeData, operateIncomeRatio, netIncomeRatio);

            // 수익 성장지표 차트
            const salesRevenueIncRatio = [0];

            for(let i=1; i < salesRevenueData.length; i++){
                const curNumber = salesRevenueData[i];
                const preNumber = salesRevenueData[i - 1];
                const incRatio = ((curNumber - preNumber) / preNumber ) * 100;
                salesRevenueIncRatio.push(incRatio);
            }

            const operateIncomeIncRatio = [0];

            for(let i=1; i < operateIncomeData.length; i++){
                const curNumber = operateIncomeData[i];
                const preNumber = operateIncomeData[i - 1];
                const incRatio = ((curNumber - preNumber) / preNumber ) * 100;
                operateIncomeIncRatio.push(incRatio);
            }

            const netIncomeIncRatio = [0];

            for(let i=1; i < netIncomeData.length; i++){
                const curNumber = netIncomeData[i];
                const preNumber = netIncomeData[i - 1];
                const incRatio = ((curNumber - preNumber) / preNumber ) * 100;
                netIncomeIncRatio.push(incRatio);
            }

            drawProfitGrowthChart(xAxisLabels, salesRevenueIncRatio, operateIncomeIncRatio, netIncomeIncRatio);
        },
        error:function(xhr, status, error){
            if(xhr.status == '401'){
                alert('로그인 후 이용 가능합니다.');
            }else{
                alert(xhr.responseJSON.message);
            }
        }
    });

    // 재무상태표 데이터 불러오기
    $.ajax({
        url:'/analysis/getFinancialChartData',
        type:'GET',
        success:function(data){

            console.log('데이터 불러오기 성공');
            console.log(data);

            // 주요 재무항목 차트
            const summary = {};

            data.forEach(item => {
                const year = item.year;
                if(!summary[year]){
                    summary[year] = {
                        assetsSum: 0,
                        liabilitiesSum: 0,
                        capitalSum: 0,
                        year: item.year
                    }
                }

                summary[year].assetsSum += item.total_assets;
                summary[year].liabilitiesSum += item.total_liabilities;
                summary[year].capitalSum += item.total_capital;
            });

            const result = Object.values(summary);

            // result 배열에서 각 속성별로 y축 데이터 추출
            const assetsData = getYAxisData(result, 'assetsSum');
            const liabilitiesData = getYAxisData(result, 'liabilitiesSum');
            const capitalData = getYAxisData(result, 'capitalSum');

            const liabilitiesRatio = [];

            for(let i=0; i < capitalData.length; i++){
                let data = (liabilitiesData[i] / capitalData[i]) * 100;
                liabilitiesRatio.push(data);
            }

            // result 배열에서 x축 레이블 추출
            const xAxisLabels = result.map(item => String(item.year));

            drawKeyFinancialChart2(xAxisLabels, assetsData, liabilitiesData, liabilitiesRatio);
        },
        error:function(xhr, status, error){
            if(xhr.status == '401'){
                alert('로그인 후 이용 가능합니다.');
            }else{
                alert(xhr.responseJSON.message);
            }
        }
    });
});