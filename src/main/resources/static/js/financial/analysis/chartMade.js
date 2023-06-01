const CHART_COLORS = {
    red: 'rgb(255, 99, 132)',
    red2: 'rgb(204, 51, 0)',
    orange: 'rgb(255, 159, 64)',
    yellow: 'rgb(255, 205, 86)',
    green: 'rgb(75, 192, 192)',
    green2: 'rgb(138, 196, 15)',
    blue: 'rgb(54, 162, 235)',
    blue2: 'rgb(33, 107, 222)',
    purple: 'rgb(153, 102, 255)',
    grey: 'rgb(201, 203, 207)'
};

function drawKeyFinancialChart(xValues, yValues1, yValues2, yValues3, yValues4, yValues5){

    const allYData = [...yValues1, ...yValues2, ...yValues3];
    var data = Math.max(...allYData);
    var dataString = data.toString();
    var firstDigit = parseInt(dataString[0]) + 1;
    var suggestedMax = parseInt(firstDigit + '0'.repeat(dataString.length - 1));

    console.log(suggestedMax);

    const allYData2 = [...yValues4, ...yValues5];
    var suggestedMax2 = Math.max(...allYData2);
    suggestedMax2 = Math.ceil(suggestedMax2/10) *10;

    new Chart("key_financial", {
        type: "bar",
        data: {
            labels: xValues,
            datasets: [
                {
                  label:'매출액',
                  backgroundColor: CHART_COLORS.blue2,
                  data: yValues1,
                  order: 0,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'영업이익',
                  backgroundColor: CHART_COLORS.red2,
                  data: yValues2,
                  order: 0,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'당기순이익',
                  backgroundColor: CHART_COLORS.green2,
                  data: yValues3,
                  order: 0,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'영업이익률',
                  data: yValues4,
                  type: 'line',
                  pointStyle: 'circle',
                  pointRadius:5,
                  backgroundColor: CHART_COLORS.purple,
                  borderColor: CHART_COLORS.purple,
                  order: 1,
                  yAxisID: 'right-y-axis'
                },
                {
                  label:'순이익률',
                  data: yValues5,
                  type: 'line',
                  pointStyle: 'rectRot',
                  pointRadius:5,
                  backgroundColor: CHART_COLORS.orange,
                  borderColor: CHART_COLORS.orange,
                  fill: false,
                  order: 1,
                  yAxisID: 'right-y-axis'
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction:{
                intersect: false,
                mode: 'index'
            },
            plugins: {
                legend:{
                    display: true,
                    position: 'bottom',
                    align:'center'
                },
                tooltip: {
                    // Disable the on-canvas tooltip
                    enabled: false,

                    external: function(context) {
                        // Tooltip Element
                        let tooltipEl = document.getElementById('chartjs-tooltip');

                        // Create element on first render
                        if (!tooltipEl) {
                            tooltipEl = document.createElement('div');
                            tooltipEl.id = 'chartjs-tooltip';
                            tooltipEl.innerHTML = '<table></table>';
                            document.body.appendChild(tooltipEl);
                        }

                        // Hide if no tooltip
                        const tooltipModel = context.tooltip;
                        if (tooltipModel.opacity === 0) {
                            tooltipEl.style.opacity = 0;
                            return;
                        }

                        // Set caret Position
                        tooltipEl.classList.remove('above', 'below', 'no-transform');
                        if (tooltipModel.yAlign) {
                            tooltipEl.classList.add(tooltipModel.yAlign);
                        } else {
                            tooltipEl.classList.add('no-transform');
                        }

                        function getBody(bodyItem) {
                            return bodyItem.lines;
                        }

                        // Set Text
                        if (tooltipModel.body) {
                            const titleLines = tooltipModel.title || [];
                            const bodyLines = tooltipModel.body.map(getBody);

                            let innerHtml = '<thead>';

                            titleLines.forEach(function(title) {
                                innerHtml += '<tr><th>' + title + '</th></tr>';
                            });
                            innerHtml += '</thead><tbody>';

                            bodyLines.forEach(function(body, i) {

                                const colonIndex = body[0].indexOf(':');
                                const key = body[0].slice(0, colonIndex);
                                const value = body[0].slice(colonIndex + 1);

                                if(key === '영업이익률'){
                                    const span = '<span style="color: ' + CHART_COLORS.purple + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> %';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '순이익률'){
                                    const span = '<span style="color: ' + CHART_COLORS.orange + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> %';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '매출액'){
                                    const span = '<span style="color: ' + CHART_COLORS.blue2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 원';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '영업이익'){
                                    const span = '<span style="color: ' + CHART_COLORS.red2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 원';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '당기순이익'){
                                    const span = '<span style="color: ' + CHART_COLORS.green2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 원';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }
                            });
                            innerHtml += '</tbody>';

                            let tableRoot = tooltipEl.querySelector('table');
                            tableRoot.innerHTML = innerHtml;
                        }

                        const position = context.chart.canvas.getBoundingClientRect();
                        const bodyFont = Chart.helpers.toFont(tooltipModel.options.bodyFont);

                        // Display, position, and set styles for font
                        tooltipEl.style.opacity = 1;
                        tooltipEl.style.position = 'absolute';
                        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 'px';
                        tooltipEl.style.top = position.top + window.pageYOffset + tooltipModel.caretY + 'px';
                        tooltipEl.style.font = bodyFont.string;
                        tooltipEl.style.padding = tooltipModel.padding + 'px ' + tooltipModel.padding + 'px';
                        tooltipEl.style.pointerEvents = 'none';
                        tooltipEl.style.backgroundColor = '#ffffff';
                        console.log(tooltipEl.style);
                        tooltipEl.style.borderTop = '1px solid #216bde';
                        tooltipEl.style.borderBottom = '1px solid #216bde';
                        tooltipEl.style.borderLeft = '1px solid #216bde';
                        tooltipEl.style.borderRight = '1px solid #216bde';
                        tooltipEl.style.borderRadius = '10px';
                        tooltipEl.style.boxShadow = '0 2px 4px rgba(0, 0, 0, 0.2)';
                        tooltipEl.style.padding = '5px';
                    }
                }
            },
            scales:{
                x:{
                    grid:{
                        display: false
                    }
                },
                'left-y-axis':{
                    id:'left-y-axis',
                    position: 'left',
                    max: suggestedMax * 1.5,
                    min: suggestedMax * -1.5,
                    ticks:{
                        stepSize: suggestedMax * 1.5 * 0.2
                    },
                    grid: {
                        display: false
                    }
                },
                'right-y-axis':{
                    id:'right-y-axis',
                    position: 'right',
                    max: suggestedMax2 * 1.5,
                    min: suggestedMax2 * -1.5,
                    ticks:{
                        stepSize: 20
                    },
                    grid: {
                        display: false
                    }
                }
            }
        }
    });
}

function drawProfitGrowthChart(xValues, yValues1, yValues2, yValues3){

    const allYData = [...yValues1, ...yValues2, ...yValues3];
    const suggestedMax = Math.max(...allYData);

    new Chart("profit_growth", {
        type: "line",
        data: {
            labels: xValues,
            datasets: [
                {
                  label:'매출액 증가율',
                  data: yValues1,
                  type: 'line',
                  pointStyle: 'circle',
                  pointRadius:5,
                  backgroundColor: CHART_COLORS.blue2,
                  borderColor: CHART_COLORS.blue2,
                  fill: false
                },
                {
                  label:'영업이익 증가율',
                  data: yValues2,
                  type: 'line',
                  pointStyle: 'rectRot',
                  pointRadius:5,
                  backgroundColor: CHART_COLORS.red2,
                  borderColor: CHART_COLORS.red2,
                  fill: false
                },
                {
                  label:'순이익 증가율',
                  data: yValues3,
                  type: 'line',
                  pointStyle: 'rect',
                  pointRadius:5,
                  backgroundColor: CHART_COLORS.green2,
                  borderColor: CHART_COLORS.green2,
                  fill: false
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction:{
                intersect: false,
                mode: 'index',
                axis: 'x',
                intersectAxis: 'x'
            },
            plugins: {
                legend:{
                    display: true,
                    position: 'bottom',
                    align:'center'
                },
                tooltip: {
                    // Disable the on-canvas tooltip
                    enabled: false,

                    external: function(context) {
                        // Tooltip Element
                        let tooltipEl = document.getElementById('chartjs-tooltip');

                        // Create element on first render
                        if (!tooltipEl) {
                            tooltipEl = document.createElement('div');
                            tooltipEl.id = 'chartjs-tooltip';
                            tooltipEl.innerHTML = '<table></table>';
                            document.body.appendChild(tooltipEl);
                        }

                        // Hide if no tooltip
                        const tooltipModel = context.tooltip;
                        if (tooltipModel.opacity === 0) {
                            tooltipEl.style.opacity = 0;
                            return;
                        }

                        // Set caret Position
                        tooltipEl.classList.remove('above', 'below', 'no-transform');
                        if (tooltipModel.yAlign) {
                            tooltipEl.classList.add(tooltipModel.yAlign);
                        } else {
                            tooltipEl.classList.add('no-transform');
                        }

                        function getBody(bodyItem) {
                            return bodyItem.lines;
                        }

                        // Set Text
                        if (tooltipModel.body) {
                            const titleLines = tooltipModel.title || [];
                            const bodyLines = tooltipModel.body.map(getBody);

                            let innerHtml = '<thead>';

                            titleLines.forEach(function(title) {
                                innerHtml += '<tr><th>' + title + '</th></tr>';
                            });
                            innerHtml += '</thead><tbody>';

                            bodyLines.forEach(function(body, i) {

                                const colonIndex = body[0].indexOf(':');
                                const key = body[0].slice(0, colonIndex);
                                const value = body[0].slice(colonIndex + 1);

                                if(key === '매출액 증가율'){
                                    const span = '<span style="color: ' + CHART_COLORS.blue2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> %';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '영업이익 증가율'){
                                    const span = '<span style="color: ' + CHART_COLORS.red2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> %';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '순이익 증가율'){
                                    const span = '<span style="color: ' + CHART_COLORS.green2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> %';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }
                            });
                            innerHtml += '</tbody>';

                            let tableRoot = tooltipEl.querySelector('table');
                            tableRoot.innerHTML = innerHtml;
                        }

                        const position = context.chart.canvas.getBoundingClientRect();
                        const bodyFont = Chart.helpers.toFont(tooltipModel.options.bodyFont);

                        // Display, position, and set styles for font
                        tooltipEl.style.opacity = 1;
                        tooltipEl.style.position = 'absolute';
                        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 'px';
                        tooltipEl.style.top = position.top + window.pageYOffset + tooltipModel.caretY + 'px';
                        tooltipEl.style.font = bodyFont.string;
                        tooltipEl.style.padding = tooltipModel.padding + 'px ' + tooltipModel.padding + 'px';
                        tooltipEl.style.pointerEvents = 'none';
                        tooltipEl.style.backgroundColor = '#ffffff';
                        console.log(tooltipEl.style);
                        tooltipEl.style.borderTop = '1px solid #216bde';
                        tooltipEl.style.borderBottom = '1px solid #216bde';
                        tooltipEl.style.borderLeft = '1px solid #216bde';
                        tooltipEl.style.borderRight = '1px solid #216bde';
                        tooltipEl.style.borderRadius = '10px';
                        tooltipEl.style.boxShadow = '0 2px 4px rgba(0, 0, 0, 0.2)';
                        tooltipEl.style.padding = '5px';
                    }
                }
            },
            scales:{
                x:{
                    offset:true,
                    grid:{
                        display: false
                    }
                },
                y:{
                    suggestedMax: suggestedMax + 100,
                    ticks:{
                        stepSize: 100
                    },
                    grid: {
                        drawBorder: false
                    }
                }
            }
        }
    });
}

function drawKeyFinancialChart2(xValues, yValues1, yValues2, yValues3){

    const allYData = [...yValues1, ...yValues2];
    var data = Math.max(...allYData);
    var dataString = data.toString();
    var firstDigit = parseInt(dataString[0]) + 1;
    var suggestedMax = parseInt(firstDigit + '0'.repeat(dataString.length - 1));

    const allYData2 = [...yValues3];
    var suggestedMax2 = Math.max(...allYData2);
    suggestedMax2 = Math.ceil(suggestedMax2/10) *10;

    new Chart("key_financial2", {
        type: "bar",
        data: {
            labels: xValues,
            datasets: [
                {
                  label:'자산총계(좌)',
                  backgroundColor: CHART_COLORS.blue2,
                  data: yValues1,
                  order: 0,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'부채총계(좌)',
                  backgroundColor: CHART_COLORS.red2,
                  data: yValues2,
                  order: 0,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'부채비율',
                  data: yValues3,
                  type: 'line',
                  pointStyle: 'circle',
                  pointRadius:5,
                  backgroundColor: CHART_COLORS.green2,
                  borderColor: CHART_COLORS.green2,
                  order: 1,
                  yAxisID: 'right-y-axis'
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction:{
                intersect: false,
                mode: 'index'
            },
            plugins: {
                legend:{
                    display: true,
                    position: 'bottom',
                    align:'center'
                },
                tooltip: {
                    // Disable the on-canvas tooltip
                    enabled: false,

                    external: function(context) {
                        // Tooltip Element
                        let tooltipEl = document.getElementById('chartjs-tooltip');

                        // Create element on first render
                        if (!tooltipEl) {
                            tooltipEl = document.createElement('div');
                            tooltipEl.id = 'chartjs-tooltip';
                            tooltipEl.innerHTML = '<table></table>';
                            document.body.appendChild(tooltipEl);
                        }

                        // Hide if no tooltip
                        const tooltipModel = context.tooltip;
                        if (tooltipModel.opacity === 0) {
                            tooltipEl.style.opacity = 0;
                            return;
                        }

                        // Set caret Position
                        tooltipEl.classList.remove('above', 'below', 'no-transform');
                        if (tooltipModel.yAlign) {
                            tooltipEl.classList.add(tooltipModel.yAlign);
                        } else {
                            tooltipEl.classList.add('no-transform');
                        }

                        function getBody(bodyItem) {
                            return bodyItem.lines;
                        }

                        // Set Text
                        if (tooltipModel.body) {
                            const titleLines = tooltipModel.title || [];
                            const bodyLines = tooltipModel.body.map(getBody);

                            let innerHtml = '<thead>';

                            titleLines.forEach(function(title) {
                                innerHtml += '<tr><th>' + title + '</th></tr>';
                            });
                            innerHtml += '</thead><tbody>';

                            bodyLines.forEach(function(body, i) {

                                const colonIndex = body[0].indexOf(':');
                                const key = body[0].slice(0, colonIndex);
                                const value = body[0].slice(colonIndex + 1);

                                if(key === '자산총계(좌)'){
                                    const span = '<span style="color: ' + CHART_COLORS.blue2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 원';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '부채총계(좌)'){
                                    const span = '<span style="color: ' + CHART_COLORS.red2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 원';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '부채비율'){
                                    const span = '<span style="color: ' + CHART_COLORS.green2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> %';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }
                            });
                            innerHtml += '</tbody>';

                            let tableRoot = tooltipEl.querySelector('table');
                            tableRoot.innerHTML = innerHtml;
                        }

                        const position = context.chart.canvas.getBoundingClientRect();
                        const bodyFont = Chart.helpers.toFont(tooltipModel.options.bodyFont);

                        // Display, position, and set styles for font
                        tooltipEl.style.opacity = 1;
                        tooltipEl.style.position = 'absolute';
                        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 'px';
                        tooltipEl.style.top = position.top + window.pageYOffset + tooltipModel.caretY + 'px';
                        tooltipEl.style.font = bodyFont.string;
                        tooltipEl.style.padding = tooltipModel.padding + 'px ' + tooltipModel.padding + 'px';
                        tooltipEl.style.pointerEvents = 'none';
                        tooltipEl.style.backgroundColor = '#ffffff';
                        console.log(tooltipEl.style);
                        tooltipEl.style.borderTop = '1px solid #216bde';
                        tooltipEl.style.borderBottom = '1px solid #216bde';
                        tooltipEl.style.borderLeft = '1px solid #216bde';
                        tooltipEl.style.borderRight = '1px solid #216bde';
                        tooltipEl.style.borderRadius = '10px';
                        tooltipEl.style.boxShadow = '0 2px 4px rgba(0, 0, 0, 0.2)';
                        tooltipEl.style.padding = '5px';
                    }
                }
            },
            scales:{
                x:{
                    grid:{
                        display: false
                    }
                },
                'left-y-axis':{
                    id:'left-y-axis',
                    position: 'left',
                    max: suggestedMax * 1.5,
                    min: suggestedMax * -1.5,
                    ticks:{
                        stepSize: suggestedMax * 1.5 * 0.2
                    },
                    grid: {
                        display: false
                    }
                },
                'right-y-axis':{
                    id:'right-y-axis',
                    position: 'right',
                    max: suggestedMax2 * 1.5,
                    min: suggestedMax2 * -1.5,
                    ticks:{
                        stepSize: 20
                    },
                    grid: {
                        display: false
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