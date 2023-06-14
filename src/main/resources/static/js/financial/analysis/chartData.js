// result 배열에서 y축 데이터를 추출하는 함수
function getYAxisData(dataArray, property) {
  return dataArray.map(item => item[property].toFixed(2));
}

document.addEventListener('DOMContentLoaded', function(){

    // 한눈에 보기 데이터 불러오기
    $.ajax({
        url:'/analysis/getAllChartData',
        type:'GET',
        success:function(data){

            console.log('한눈에 보기 데이터 불러오기 성공');
            console.log(data);

            const summary = {};

            data.forEach(item => {
                const year = item.year;
                if(!summary[year]){
                    summary[year] = {
                        totalRevenueSum: 0,
                        netIncomeSum: 0,
                        operateExpensesSum: 0,
                        mNumSum: 0,
                        operateIncomeSum: 0,
                        totalAssetsSum: 0,
                        salesRevenueSum: 0,
                        year: item.year
                    }
                }

                summary[year].totalRevenueSum += (item.total_revenue) / 100000000;
                summary[year].netIncomeSum += item.netIncome  / 100000000;
                summary[year].operateExpensesSum += item.operate_expenses  / 100000000;
                summary[year].mNumSum += item.mnum;
                summary[year].operateIncomeSum += item.operate_income  / 100000000;
                summary[year].totalAssetsSum += item.total_assets  / 100000000;
                summary[year].salesRevenueSum += item.sales_revenue  / 100000000;
            });

            const result = Object.values(summary);

            // result 배열에서 각 속성별로 y축 서브 데이터 추출
            const totalRevenueData = getYAxisData(result, 'totalRevenueSum');
            const netIncomeData = getYAxisData(result, 'netIncomeSum');
            const operateExpensesData = getYAxisData(result, 'operateExpensesSum');
            const mNumData = getYAxisData(result, 'mNumSum');
            const operateIncomeData = getYAxisData(result, 'operateIncomeSum');
            const totalAssetsData = getYAxisData(result, 'totalAssetsSum');
            const salesRevenueData = getYAxisData(result, 'salesRevenueSum');

            const turnOverData = [];

            for(let i=0; i < salesRevenueData.length; i++){
                let data = 0;
                if(salesRevenueData[i] !==0 && totalAssetsData[i] !==0){
                    data = salesRevenueData[i] / (totalAssetsData[i] / 365)
                }
                turnOverData.push(data);
            }

            // 배열에서 각 속성별로 y축 데이터 추출
            let trdata = 0;
            let trdata2 = 0;
            if(totalRevenueData[totalRevenueData - 2] !== 0){
                trdata = ((totalRevenueData[totalRevenueData.length - 1] - totalRevenueData[totalRevenueData.length - 2]) / Math.abs(totalRevenueData[totalRevenueData.length - 2]) * 100).toFixed(2);
                trdata2 = totalRevenueData[totalRevenueData.length - 1] - totalRevenueData[totalRevenueData.length - 2];
            }else{
                trdate2 = totalRevenueData[totalRevenueData.length - 1];
            }


            let nidata = 0;
            let nidata2 = 0;
            if(netIncomeData[netIncomeData - 2] !== 0){
                nidata = ((netIncomeData[netIncomeData.length - 1] - netIncomeData[netIncomeData.length - 2]) / Math.abs(netIncomeData[netIncomeData.length - 2]) * 100).toFixed(2);
                nidata2 = netIncomeData[netIncomeData.length - 1] - netIncomeData[netIncomeData.length - 2];
            }else{
                nidata2 = netIncomeData[netIncomeData.length - 1];
            }


            let oedata = 0;
            let oedata2 = 0;
            if(operateExpensesData[operateExpensesData.length - 2] !== 0){
                oedata = ((operateExpensesData[operateExpensesData.length - 1] - operateExpensesData[operateExpensesData.length - 2]) / Math.abs(operateExpensesData[operateExpensesData.length - 2]) * 100).toFixed(2);
                oedata2 = (operateExpensesData[operateExpensesData.length - 1] - operateExpensesData[operateExpensesData.length - 2]).toFixed(2);
            }else{
                oedata2 = operateExpensesData[operateExpensesData.length - 1].toFixed(2);
            }


            let mnumdata = 0;
            let mnumdata2 = 0;
            if(mNumData[mNumData.length - 1] !== mNumData[mNumData.length - 2]){
                mnumdata = ((mNumData[mNumData.length - 1] - mNumData[mNumData.length - 2]) / mNumData[mNumData.length - 2] * 100).toFixed(2);
                mnumdata2 = mNumData[mNumData.length - 1] - mNumData[mNumData.length - 2];
            }else{
                mnumdata2 = mNumData[mNumData.length - 1];
            }


            let oidata = 0;
            let oidata2 = 0;
            if(operateIncomeData[operateIncomeData.length - 2] !== 0){
                oidata = ((operateIncomeData[operateIncomeData.length - 1] - operateIncomeData[operateIncomeData.length - 2]) / Math.abs(operateIncomeData[operateIncomeData.length - 2]) * 100).toFixed(2);
                oidata2 = (operateIncomeData[operateIncomeData.length - 1] - operateIncomeData[operateIncomeData.length - 2]).toFixed(2);
            }else{
                oidata2 = operateIncomeData[operateIncomeData.length - 1].toFixed(2);
            }


            let todata = 0;
            let todata2 = 0;
            if(turnOverData[turnOverData.length - 2] !== 0){
                todata = ((turnOverData[turnOverData.length - 1] - turnOverData[turnOverData.length - 2]) / Math.abs(turnOverData[turnOverData.length - 2]) * 100).toFixed(2);
                todata2 = (turnOverData[turnOverData.length - 1] - turnOverData[turnOverData.length - 2]).toFixed(2);
            }else{
                todata2 = turnOverData[turnOverData.length - 1].toFixed(2);
            }

            // result 배열에서 x축 레이블 추출
            const xAxisLabels = result.map(item => String(item.year));

            drawTotalRevenueChart(trdata, trdata2);
            drawNetIncomeChart(nidata, nidata2);
            drawTRNIChart(xAxisLabels, totalRevenueData, netIncomeData);

            drawOperateExpensesChart(oedata, oedata2);
            drawMNumChart(mnumdata, mnumdata2);

            drawOEMNChart(xAxisLabels, operateExpensesData, mNumData);

            drawOperateIncomeChart(oidata, oidata2);
            drawTurnOverChart(todata, todata2);
            drawOITOChart(xAxisLabels, operateIncomeData, turnOverData);
        },
        error:function(xhr, status, error){
            if(xhr.status == '401'){
                alert('로그인 후 이용 가능합니다.');
            }else{
                alert(xhr.responseJSON.message);
            }
        }
    });

    // 포괄 손익계산서 데이터 불러오기
    $.ajax({
        url:'/analysis/getIncomeChartData',
        type:'GET',
        success:function(data){

            console.log('손익계산서 데이터 불러오기 성공');
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

                summary[year].salesRevenueSum += item.sales_revenue / 100000000;
                summary[year].operateIncome += (item.operate_revenue - item.operate_expenses) / 100000000;
                summary[year].netIncomeSum += item.netIncome / 100000000;
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
                if(salesRevenueData[i] != 0){
                    let data = (operateIncomeData[i] / salesRevenueData[i]) * 100;
                    operateIncomeRatio.push(data);
                }else{
                    operateIncomeRatio.push(0);
                }
            }

            const netIncomeRatio = [];

            for(let i=0; i < salesRevenueData.length; i++){
                if(salesRevenueData[i] != 0){
                    let data = (netIncomeData[i] / salesRevenueData[i]) * 100;
                    netIncomeRatio.push(data);
                }else{
                    netIncomeRatio.push(0);
                }
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

            console.log('재무상태표 데이터 불러오기 성공');
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
                        curAssetsSum: 0,
                        paidCapSum:0,
                        tangAssetsSum: 0,
                        year: item.year
                    }
                }

                summary[year].assetsSum += item.total_assets/ 100000000;
                summary[year].liabilitiesSum += item.total_liabilities/ 100000000;
                summary[year].capitalSum += item.total_capital/ 100000000;
                summary[year].curAssetsSum += item.current_assets/ 100000000;
                summary[year].paidCapSum += item.paid_capital/ 100000000;
                summary[year].tangAssetsSum += item.tangible_assets/ 100000000;
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

            // 자산 성장지표 차트
            const curAssetsData = getYAxisData(result, 'curAssetsSum');
            const paidCapData = getYAxisData(result, 'paidCapSum');
            const tangAssetsData = getYAxisData(result, 'tangAssetsSum');

            const totalAssetsRatio = [0];

            for(let i=1; i < assetsData.length; i++){
                const curNumber = assetsData[i];
                const preNumber = assetsData[i - 1];
                const incRatio = ((curNumber - preNumber) / preNumber ) * 100;
                totalAssetsRatio.push(incRatio);
            }

            const curAssetsRatio = [0];

            for(let i=1; i < curAssetsData.length; i++){
                const curNumber = curAssetsData[i];
                const preNumber = curAssetsData[i - 1];
                const incRatio = ((curNumber - preNumber) / preNumber ) * 100;
                curAssetsRatio.push(incRatio);
            }

            const tangAssetsRatio = [0];

            for(let i=1; i < tangAssetsData.length; i++){
                const curNumber = tangAssetsData[i];
                const preNumber = tangAssetsData[i - 1];
                const incRatio = ((curNumber - preNumber) / preNumber ) * 100;
                tangAssetsRatio.push(incRatio);
            }

            const paidCapRatio = [0];

            for(let i=1; i < paidCapData.length; i++){
                const curNumber = paidCapData[i];
                const preNumber = paidCapData[i - 1];
                const incRatio = ((curNumber - preNumber) / preNumber ) * 100;
                paidCapRatio.push(incRatio);
            }

            drawAssetGrowthChart(xAxisLabels, totalAssetsRatio, curAssetsRatio, tangAssetsRatio, paidCapRatio);
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