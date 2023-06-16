import React from 'react';
import Chart from 'chart.js/auto';
import { Line } from 'react-chartjs-2';

import {
      Chart as ChartJS,
      CategoryScale,
      LinearScale,
      BarElement,
      Title,
      Tooltip,
      Legend,
    } from 'chart.js';
    import { Bar } from 'react-chartjs-2';

    ChartJS.register(
      CategoryScale,
      LinearScale,
      BarElement,
      Title,
      Tooltip,
      Legend
    );

export default function DataTable(props) {
    const ChartDataList = props.content;    //가져온 데이터

    const allPrCode = [];   //모든 prCode를 가지고 있습니다.
    ChartDataList.map((item,index)=>{
        allPrCode.push(item.prCode);
    });
    const filterPrCode = [...new Set(allPrCode)]; //중복 제거한 prCode

    //조립 라인
    const labels=['자동조립1호기', '자동조립2호기', '자동조립3호기', '자동조립4호기', '자동조립5호기', '자동조립6호기'];

    const filteredChartData = filterPrCode.map((prCode,index) => {
         return ChartDataList.filter(item => item.prCode === prCode);
    });

    const options = {
        plugins: {
            title: {
                display: true,
                text: '라인별 생산 수량',
            },
        },
        responsive: true,
        scales: {
            x: {
                stacked: true,
            },
            y: {
                stacked: true,
            },
        },
        maintainAspectRatio: false // false로 설정 시 사용자 정의 크기에 따라 그래프 크기가 결정됨.
    };

    const color=['#ef9a9a','#e91e63','#7b1fa2'];

    const barChart = filteredChartData.map((item, index) => {
      const data = labels.map((label) => {
        const product = item.find((p) => p.productionLine === label);
        return product ? product.totalCount : 0;
      });

      return {
        label: item[0].prName,
        backgroundColor: color[index],
        data: data,
        borderColor: color[index],
        borderWidth: 2,
      };
    });

    const targetChart = {
        label: '목표 수량',
        type: "line",
        data: [10000,10000,10000,10000,10000,10000],
        fill: false,
        borderColor: 'rgb(75, 192, 192)',
        backgroundColor :'rgb(75, 192, 192)',
        tension: 0.1
    }

    const nestedDatasets = [targetChart,...barChart];
    console.log(nestedDatasets);

     const data = {
        labels:labels,
        datasets: nestedDatasets,
    };

    return (
        <div>
            <Bar
                data={data}
                height={600}
                options={options}
            />
        </div>
    );
}

