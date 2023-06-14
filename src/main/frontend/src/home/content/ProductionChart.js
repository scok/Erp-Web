import React from 'react';

import Chart from 'chart.js/auto';
import { Line } from 'react-chartjs-2';

export default function DataTable(props) {

    const ChartData = props.content;

    const options = {
        legend: {
            display: false, // label 숨기기
        },
       /* scales: {
            yAxes: [{
                ticks: {
                    min: 0, // 스케일에 대한 최솟갓 설정, 0 부터 시작
                    stepSize: 500, // 스케일에 대한 사용자 고정 정의 값
                }
            }]
        },*/
        maintainAspectRatio: false // false로 설정 시 사용자 정의 크기에 따라 그래프 크기가 결정됨.
    }

    const data = {
        labels : ChartData.map(item => item.productionLine),
        datasets: [
        {
          type: 'bar',
          label: '생산 수량',
          backgroundColor: '#33eaff',
          data: ChartData.map(item => item.totalCount),
          borderColor: '#33eaff',
          borderWidth: 2,
        }
        ],
    };
    return (
        <div>
            <Line type="line"
                data={data}
                height={500}
                options={options}
            />
        </div>
    );
}

