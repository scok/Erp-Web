const CHART_COLORS = {
    red: 'rgb(255, 99, 132)',
    orange: 'rgb(255, 159, 64)',
    yellow: 'rgb(255, 205, 86)',
    green: 'rgb(75, 192, 192)',
    blue: 'rgb(54, 162, 235)',
    purple: 'rgb(153, 102, 255)',
    grey: 'rgb(201, 203, 207)'
};

function drawKeyFinancialChart(xValues, yValues1, yValues2, yValues3, yValues4, yValues5){
    new Chart("key_financial", {
        type: "bar",
        data: {
            labels: xValues,
            datasets: [
                {
                  label:'매출액',
                  backgroundColor: CHART_COLORS.blue,
                  data: yValues1,
                  order: 1,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'영업이익',
                  backgroundColor: CHART_COLORS.red,
                  data: yValues2,
                  order: 1,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'당기순이익',
                  backgroundColor: CHART_COLORS.green,
                  data: yValues3,
                  order: 1,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'영업이익률',
                  backgroundColor: CHART_COLORS.purple,
                  data: yValues4,
                  borderColor: CHART_COLORS.purple,
                  type: 'line',
                  order: 0,
                  yAxisID: 'right-y-axis'
                },
                {
                  label:'순이익률',
                  backgroundColor: CHART_COLORS.yellow,
                  data: yValues5,
                  borderColor: CHART_COLORS.yellow,
                  type: 'line',
                  order: 0,
                  yAxisID: 'right-y-axis'
                }
            ]
        },
        options: {
            maintainAspectRatio: false,
            plugins: {
                legend:{
                    display: true
                }
            },
            scales:{
                xAxes:[{
					gridLines:{
						display:false
					},
					offset:true
				}],
                yAxes:[{
					id:'left-y-axis',
					type:'linear',
					position:'left',
					ticks:{
						max:25000,
						stepSize:5000
					},
					gridLines:{
						drawBorder:false
					}
				},{
					id:'right-y-axis',
					type:'linear',
					position:'right',
					ticks:{
						max:100,
						stepSize:20
					},
					gridLines:{
						drawBorder:false
					}
				}]
            }
        }
    });
}

function drawIncomeChart(xValues, yValues){

    new Chart("myChart", {
      type: "bar",
      data: {
        labels: xValues,
        datasets: [{
          backgroundColor: [CHART_COLORS.red, CHART_COLORS.green, CHART_COLORS.yellow],
          data: yValues
        }]
      },
      options: {
        maintainAspectRatio: false,
        plugins: {
            legend:{
                display: false
            }
        },
        scales:{
            x:{
                ticks:{
                    font:{
                        weight: 'bold'
                    }
                }
            }
        }
      }
    });
}

function drawIncomeChart2(xValues, yValues){

    new Chart("myChart2", {
      type: "line",
      data: {
        labels: xValues,
        datasets: [{
          borderColor: CHART_COLORS.red,
          backgroundColor: 'rgba(255, 99, 132, 0.2)',
          data: yValues,
          tension: 0.4,
          fill: true
        }]
      },
      options: {
        maintainAspectRatio: false,
        plugins: {
            legend:{
                display: false,
            }
        },
        scales:{
            x:{
                ticks:{
                    font:{
                        weight: 'bold'
                    }
                }
            }
        }
      }
    });
}

function drawTransactionChart(){

    new Chart('myChart3', {
      type: "pie",
      data: {
        labels: ['Red', 'Green', 'Yellow', 'Orange'],
        datasets: [{
            label: 'DataSet 1',
            data: [1500,2000,1700,2500],
            backgroundColor: [CHART_COLORS.red, CHART_COLORS.green, CHART_COLORS.yellow, CHART_COLORS.orange],
            hoverOffset: 10
        }]
      },
      options: {
        responsive: true,
        plugins: {
            legend:{
                display:true,
                position: 'right',
                labels:{
                    font:{
                        size: 16,
                        weight:'bold'
                    }
                }
            }
        }
      }
    });
}