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

            console.log(result);
            console.log(xAxisLabels);
            console.log(salesRevenueData);
            console.log(operateIncomeData);
            console.log(netIncomeData);

            const operateIncomeRatio = [];

            for(let i=0; i < salesRevenueData.length; i++){
                let data = (operateIncomeData[i] / salesRevenueData[i]) * 100;
                operateIncomeRatio.push(data);
            }

            console.log(operateIncomeRatio);

            const netIncomeRatio = [];

            for(let i=0; i < salesRevenueData.length; i++){
                let data = (netIncomeData[i] / salesRevenueData[i]) * 100;
                netIncomeRatio.push(data);
            }

            console.log(netIncomeRatio);

            drawKeyFinancialChart(xAxisLabels, salesRevenueData, operateIncomeData, netIncomeData, operateIncomeRatio, netIncomeRatio);
            //drawProfitGrowthChart();
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