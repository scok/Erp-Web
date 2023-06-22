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

function drawTotalRevenueChart(yValues1, yValues2){

    class circularChart extends Chart.DoughnutController{
        draw(){
            super.draw(arguments);

            const { ctx, data, chartArea: { top, bottom, left,
              right, width, height}} = this.chart;

            var score = '';
            var percentage = '';

            if(yValues1 > 0){
                score = '+ ' + yValues2.toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }else if(yValues1 == 0){
                score = '= ' + Math.abs(yValues2).toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%  -' || '';
            }else{
                score = '- ' + Math.abs(yValues2).toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }

            ctx.save();

            ctx.font = 'bold 20px sans-serif';
            ctx.textAlign = 'center';

            if(yValues1 > 0){
                ctx.fillStyle = CHART_COLORS.green;
            }else if(yValues1 == 0){
                ctx.fillStyle = CHART_COLORS.black;
            }else{
                ctx.fillStyle = CHART_COLORS.red;
            }

            ctx.fillText(percentage, width / 2, bottom - 40);

            ctx.font = '15px sans-serif';
            ctx.textAlign = 'center';

            ctx.fillText(score, width / 2, bottom - 10);

            const arrowSize = 8; // 화살표 크기
            const arrowX = width / 2 + 45; // 화살표 X 좌표
            const arrowY = bottom - 48; // 화살표 Y 좌표

            ctx.beginPath();

            if(yValues1 > 0){
                ctx.moveTo(arrowX, arrowY - arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY + arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY + arrowSize);
            }else if(yValues1 < 0){
                ctx.moveTo(arrowX, arrowY + arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY - arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY - arrowSize);
            }

            ctx.closePath();
            ctx.fill();
        }
    }

    circularChart.id = 'circularGauge';
    circularChart.defaults = Chart.DoughnutController.defaults;
    circularChart.defaults = {
        cutout: '75%',
        circumference: 180,
        rotation: 270,
        responsive: false
    };

    circularChart.overrides = {
        aspectRatio: 2,
        plugins:{
            legend:{
                display: false
            },
            tooltip:{
                enabled: false
            }
        }
    }
    Chart.register(circularChart);

    let data = [0];

    data = [Math.min(Math.abs(yValues1), 100), 100 - Math.min(Math.abs(yValues1), 100)];

    let backgroundColor;

    if(yValues1 > 0 ){
        backgroundColor = [CHART_COLORS.green, CHART_COLORS.grey];
    }else{
        backgroundColor = [CHART_COLORS.red, CHART_COLORS.grey];
    }

    new Chart("revenue", {
      type: "circularGauge",
      data: {
        labels: ['수입'],
        datasets: [{
          label: '수입',
          data: data,
          backgroundColor: backgroundColor,
          hoverOffset: 4
        }]
      }
    });
}

function drawNetIncomeChart(yValues1, yValues2){

    class circularChart2 extends Chart.DoughnutController{
        draw(){
            super.draw(arguments);

            const { ctx, data, chartArea: { top, bottom, left,
              right, width, height}} = this.chart;

            var score = '';
            var percentage = '';

            if(yValues1 > 0){
                score = '+ ' + yValues2.toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }else if(yValues1 == 0){
                score = '= ' + Math.abs(yValues2).toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%  -' || '';
            }else{
                score = '- ' + Math.abs(yValues2).toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }

            ctx.save();

            ctx.font = 'bold 20px sans-serif';
            ctx.textAlign = 'center';

            if(yValues1 > 0){
                ctx.fillStyle = CHART_COLORS.green;
            }else if(yValues1 == 0){
                ctx.fillStyle = CHART_COLORS.black;
            }else{
                ctx.fillStyle = CHART_COLORS.red;
            }

            ctx.fillText(percentage, width / 2, bottom - 40);

            ctx.font = '15px sans-serif';
            ctx.textAlign = 'center';

            ctx.fillText(score, width / 2, bottom - 10);

            const arrowSize = 8; // 화살표 크기
            const arrowX = width / 2 + 45; // 화살표 X 좌표
            const arrowY = bottom - 48; // 화살표 Y 좌표

            ctx.beginPath();

            if(yValues1 > 0){
                ctx.moveTo(arrowX, arrowY - arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY + arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY + arrowSize);
            }else if(yValues1 < 0){
                ctx.moveTo(arrowX, arrowY + arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY - arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY - arrowSize);
            }

            ctx.closePath();
            ctx.fill();
        }
    }

    circularChart2.id = 'circularGauge2';
    circularChart2.defaults = Chart.DoughnutController.defaults;
    circularChart2.defaults = {
        cutout: '75%',
        circumference: 180,
        rotation: 270,
        responsive: false
    };

    circularChart2.overrides = {
        aspectRatio: 2,
        plugins:{
            legend:{
                display: false
            },
            tooltip:{
                enabled: false
            }
        }
    }
    Chart.register(circularChart2);

    let data = [0];

    data = [Math.min(Math.abs(yValues1), 100), 100 - Math.min(Math.abs(yValues1), 100)];

    let backgroundColor;

    if(yValues1 > 0 ){
        backgroundColor = [CHART_COLORS.green, CHART_COLORS.grey];
    }else{
        backgroundColor = [CHART_COLORS.red, CHART_COLORS.grey];
    }

    new Chart("netIncome", {
      type: "circularGauge2",
      data: {
        labels: ['순이익'],
        datasets: [{
          label: '순이익',
          data: data,
          backgroundColor: backgroundColor,
          hoverOffset: 4
        }]
      }
    });
}

function drawTRNIChart(xValues, yValues1, yValues2){

    const hoverLine = {
        id: 'hoverLine',
        beforeDraw: chart => {
            if(chart.tooltip._active && chart.tooltip._active.length){
                const ctx = chart.ctx;
                ctx.save();
                const activePoint = chart.tooltip._active[0];

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x + 32, chart.chartArea.top);
                ctx.lineTo(activePoint.element.x + 32, activePoint.element.y);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x + 32, activePoint.element.y);
                ctx.lineTo(activePoint.element.x + 32, chart.chartArea.bottom);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();
            };
        }
    }

    new Chart("rv_nI_chart", {
        type: "bar",
        data: {
            labels: xValues,
            datasets: [
                {
                  label:'수입',
                  backgroundColor: CHART_COLORS.blue2,
                  data: yValues1
                },
                {
                  label:'순이익',
                  backgroundColor: CHART_COLORS.red2,
                  data: yValues2
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

                                if(key === '수입'){
                                    const span = '<span style="color: ' + CHART_COLORS.blue2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 억 원';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '순이익'){
                                    const span = '<span style="color: ' + CHART_COLORS.red2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 억 원';
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
                        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 15 + 'px';
                        tooltipEl.style.top = position.top + window.pageYOffset + 20  + 'px';
                        tooltipEl.style.font = bodyFont.string;
                        tooltipEl.style.padding = tooltipModel.padding + 'px ' + tooltipModel.padding + 'px';
                        tooltipEl.style.pointerEvents = 'none';
                        tooltipEl.style.backgroundColor = '#ffffff';
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
                }
            }
        },
        plugins: [hoverLine]
    });
}

function drawOperateExpensesChart(yValues1, yValues2){

    class circularChart3 extends Chart.DoughnutController{
        draw(){
            super.draw(arguments);

            const { ctx, data, chartArea: { top, bottom, left,
              right, width, height}} = this.chart;

            var score = '';
            var percentage = '';

            if(yValues1 > 0){
                score = '+ ' + yValues2.toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }else if(yValues1 == 0){
                score = '= ' + Math.abs(yValues2).toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%  -' || '';
            }else{
                score = '- ' + Math.abs(yValues2).toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }

            ctx.save();

            ctx.font = 'bold 20px sans-serif';
            ctx.textAlign = 'center';

            if(yValues1 > 0){
                ctx.fillStyle = CHART_COLORS.green;
            }else if(yValues1 == 0){
                ctx.fillStyle = CHART_COLORS.black;
            }else{
                ctx.fillStyle = CHART_COLORS.red;
            }

            ctx.fillText(percentage, width / 2, bottom - 40);

            ctx.font = '15px sans-serif';
            ctx.textAlign = 'center';

            ctx.fillText(score, width / 2, bottom - 10);

            const arrowSize = 8; // 화살표 크기
            const arrowX = width / 2 + 45; // 화살표 X 좌표
            const arrowY = bottom - 48; // 화살표 Y 좌표

            ctx.beginPath();

            if(yValues1 > 0){
                ctx.moveTo(arrowX, arrowY - arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY + arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY + arrowSize);
            }else if(yValues1 < 0){
                ctx.moveTo(arrowX, arrowY + arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY - arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY - arrowSize);
            }

            ctx.closePath();
            ctx.fill();
        }
    }

    circularChart3.id = 'circularGauge3';
    circularChart3.defaults = Chart.DoughnutController.defaults;
    circularChart3.defaults = {
        cutout: '75%',
        circumference: 180,
        rotation: 270,
        responsive: false
    };

    circularChart3.overrides = {
        aspectRatio: 2,
        plugins:{
            legend:{
                display: false
            },
            tooltip:{
                enabled: false
            }
        }
    }
    Chart.register(circularChart3);

    let data = [0];

    data = [Math.min(Math.abs(yValues1), 100), 100 - Math.min(Math.abs(yValues1), 100)];

    let backgroundColor;

    if(yValues1 > 0 ){
        backgroundColor = [CHART_COLORS.green, CHART_COLORS.grey];
    }else{
        backgroundColor = [CHART_COLORS.red, CHART_COLORS.grey];
    }

    new Chart("operate_expenses", {
      type: "circularGauge3",
      data: {
        labels: ['운영비용'],
        datasets: [{
          label: '운영비용',
          data: data,
          backgroundColor: backgroundColor,
          hoverOffset: 4
        }]
      }
    });
}

function drawMNumChart(yValues1, yValues2){

    class circularChart4 extends Chart.DoughnutController{
        draw(){
            super.draw(arguments);

            const { ctx, data, chartArea: { top, bottom, left,
              right, width, height}} = this.chart;

            var score = '';
            var percentage = '';

            if(yValues1 > 0){
                score = '+ ' + yValues2.toLocaleString() + ' 명' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }else if(yValues1 == 0){
                score = '= ' + Math.abs(yValues2).toLocaleString() + ' 명' || '';
                percentage = Math.round(yValues1) + '%  -' || '';
            }else{
                score = '- ' + Math.abs(yValues2).toLocaleString() + ' 명' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }

            ctx.save();

            ctx.font = 'bold 20px sans-serif';
            ctx.textAlign = 'center';

            if(yValues1 > 0){
                ctx.fillStyle = CHART_COLORS.green;
            }else if(yValues1 == 0){
                ctx.fillStyle = CHART_COLORS.black;
            }else{
                ctx.fillStyle = CHART_COLORS.red;
            }

            ctx.fillText(percentage, width / 2, bottom - 40);

            ctx.font = '15px sans-serif';
            ctx.textAlign = 'center';

            ctx.fillText(score, width / 2, bottom - 10);

            const arrowSize = 8; // 화살표 크기
            const arrowX = width / 2 + 45; // 화살표 X 좌표
            const arrowY = bottom - 48; // 화살표 Y 좌표

            ctx.beginPath();

            if(yValues1 > 0){
                ctx.moveTo(arrowX, arrowY - arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY + arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY + arrowSize);
            }else if(yValues1 < 0){
                ctx.moveTo(arrowX, arrowY + arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY - arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY - arrowSize);
            }

            ctx.closePath();
            ctx.fill();
        }
    }

    circularChart4.id = 'circularGauge4';
    circularChart4.defaults = Chart.DoughnutController.defaults;
    circularChart4.defaults = {
        cutout: '75%',
        circumference: 180,
        rotation: 270,
        responsive: false
    };

    circularChart4.overrides = {
        aspectRatio: 2,
        plugins:{
            legend:{
                display: false
            },
            tooltip:{
                enabled: false
            }
        }
    }
    Chart.register(circularChart4);

    let data = [0];

    data = [Math.min(Math.abs(yValues1), 100), 100 - Math.min(Math.abs(yValues1), 100)];

    let backgroundColor;

    if(yValues1 > 0 ){
        backgroundColor = [CHART_COLORS.green, CHART_COLORS.grey];
    }else{
        backgroundColor = [CHART_COLORS.red, CHART_COLORS.grey];
    }

    new Chart("member_ratio", {
      type: "circularGauge4",
      data: {
        labels: ['직원수'],
        datasets: [{
          label: '직원수',
          data: data,
          backgroundColor: backgroundColor,
          hoverOffset: 4
        }]
      }
    });
}

function drawOEMNChart(xValues, yValues1, yValues2){

    const allYData = [...yValues1];
    var data = Math.floor(Math.floor(Math.max(...allYData)));
    var dataString = data.toString();
    var firstDigit = parseInt(dataString[0]) + 1;
    var suggestedMax = parseInt(firstDigit + '0'.repeat(dataString.length - 1));

    const allYData2 = [...yValues2];
    var suggestedMax2 = Math.floor(Math.max(...allYData2));
    suggestedMax2 = Math.ceil(suggestedMax2/10) *10;

    const hoverLine = {
        id: 'hoverLine',
        beforeDraw: chart => {
            if(chart.tooltip._active && chart.tooltip._active.length){
                const ctx = chart.ctx;
                ctx.save();
                const activePoint = chart.tooltip._active[0];

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x + 32, chart.chartArea.top);
                ctx.lineTo(activePoint.element.x + 32, activePoint.element.y);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x + 32, activePoint.element.y);
                ctx.lineTo(activePoint.element.x + 32, chart.chartArea.bottom);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();
            };
        }
    }

    new Chart("oe_mr_chart", {
        type: "bar",
        data: {
            labels: xValues,
            datasets: [
                {
                  label:'운영 비용',
                  backgroundColor: CHART_COLORS.blue2,
                  data: yValues1,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'직원 수',
                  backgroundColor: CHART_COLORS.red2,
                  data: yValues2,
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

                                if(key === '운영 비용'){
                                    const span = '<span style="color: ' + CHART_COLORS.blue2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 억 원';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '직원 수'){
                                    const span = '<span style="color: ' + CHART_COLORS.red2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 명';
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
                        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 15 + 'px';
                        tooltipEl.style.top = position.top + window.pageYOffset + 20  + 'px';
                        tooltipEl.style.font = bodyFont.string;
                        tooltipEl.style.padding = tooltipModel.padding + 'px ' + tooltipModel.padding + 'px';
                        tooltipEl.style.pointerEvents = 'none';
                        tooltipEl.style.backgroundColor = '#ffffff';
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
                    max: suggestedMax + 10,
                    min: (suggestedMax + 10) * -1,
                    ticks:{
                        stepSize: (suggestedMax + 10) * 2 / 10
                    }
                },
                'right-y-axis':{
                    id:'right-y-axis',
                    position: 'right',
                    max: suggestedMax2 + 10,
                    min: (suggestedMax2 + 10) * -1,
                    ticks:{
                        stepSize: (suggestedMax2 + 10) * 2 / 10
                    },
                    grid: {
                        drawOnChartArea: false
                    }
                }
            }
        },
        plugins: [hoverLine]
    });
}

function drawOperateIncomeChart(yValues1, yValues2){

    class circularChart5 extends Chart.DoughnutController{
        draw(){
            super.draw(arguments);

            const { ctx, data, chartArea: { top, bottom, left,
              right, width, height}} = this.chart;

            var score = '';
            var percentage = '';

            if(yValues1 > 0){
                score = '+ ' + yValues2.toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }else if(yValues1 == 0){
                score = '= ' + Math.abs(yValues2).toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%  -' || '';
            }else{
                score = '- ' + Math.abs(yValues2).toLocaleString() + ' 억 원' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }

            ctx.save();

            ctx.font = 'bold 20px sans-serif';
            ctx.textAlign = 'center';

            if(yValues1 > 0){
                ctx.fillStyle = CHART_COLORS.green;
            }else if(yValues1 == 0){
                ctx.fillStyle = CHART_COLORS.black;
            }else{
                ctx.fillStyle = CHART_COLORS.red;
            }

            ctx.fillText(percentage, width / 2, bottom - 40);

            ctx.font = '15px sans-serif';
            ctx.textAlign = 'center';

            ctx.fillText(score, width / 2, bottom - 10);

            const arrowSize = 8; // 화살표 크기
            const arrowX = width / 2 + 45; // 화살표 X 좌표
            const arrowY = bottom - 48; // 화살표 Y 좌표

            ctx.beginPath();

            if(yValues1 > 0){
                ctx.moveTo(arrowX, arrowY - arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY + arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY + arrowSize);
            }else if(yValues1 < 0){
                ctx.moveTo(arrowX, arrowY + arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY - arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY - arrowSize);
            }

            ctx.closePath();
            ctx.fill();
        }
    }

    circularChart5.id = 'circularGauge5';
    circularChart5.defaults = Chart.DoughnutController.defaults;
    circularChart5.defaults = {
        cutout: '75%',
        circumference: 180,
        rotation: 270,
        responsive: false
    };

    circularChart5.overrides = {
        aspectRatio: 2,
        plugins:{
            legend:{
                display: false
            },
            tooltip:{
                enabled: false
            }
        }
    }
    Chart.register(circularChart5);

    let data = [0];

    data = [Math.min(Math.abs(yValues1), 100), 100 - Math.min(Math.abs(yValues1), 100)];

    let backgroundColor;

    if(yValues1 > 0 ){
        backgroundColor = [CHART_COLORS.green, CHART_COLORS.grey];
    }else{
        backgroundColor = [CHART_COLORS.red, CHART_COLORS.grey];
    }

    new Chart("total_income", {
      type: "circularGauge5",
      data: {
        labels: ['총이익'],
        datasets: [{
          label: '총이익',
          data: data,
          backgroundColor: backgroundColor,
          hoverOffset: 4
        }]
      }
    });
}

function drawTurnOverChart(yValues1, yValues2){

    class circularChart6 extends Chart.DoughnutController{
        draw(){
            super.draw(arguments);

            const { ctx, data, chartArea: { top, bottom, left,
              right, width, height}} = this.chart;

            var score = '';
            var percentage = '';

            if(yValues1 > 0){
                score = '+ ' + yValues2.toLocaleString() + ' %' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }else if(yValues1 == 0){
                score = '= ' + Math.abs(yValues2).toLocaleString() + ' %' || '';
                percentage = Math.round(yValues1) + '%  -' || '';
            }else{
                score = '- ' + Math.abs(yValues2).toLocaleString() + ' %' || '';
                percentage = Math.round(yValues1) + '%' || '';
            }

            ctx.save();

            ctx.font = 'bold 20px sans-serif';
            ctx.textAlign = 'center';

            if(yValues1 > 0){
                ctx.fillStyle = CHART_COLORS.green;
            }else if(yValues1 == 0){
                ctx.fillStyle = CHART_COLORS.black;
            }else{
                ctx.fillStyle = CHART_COLORS.red;
            }

            ctx.fillText(percentage, width / 2, bottom - 40);

            ctx.font = '15px sans-serif';
            ctx.textAlign = 'center';

            ctx.fillText(score, width / 2, bottom - 10);

            const arrowSize = 8; // 화살표 크기
            const arrowX = width / 2 + 45; // 화살표 X 좌표
            const arrowY = bottom - 48; // 화살표 Y 좌표

            ctx.beginPath();

            if(yValues1 > 0){
                ctx.moveTo(arrowX, arrowY - arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY + arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY + arrowSize);
            }else if(yValues1 < 0){
                ctx.moveTo(arrowX, arrowY + arrowSize);
                ctx.lineTo(arrowX - arrowSize, arrowY - arrowSize);
                ctx.lineTo(arrowX + arrowSize, arrowY - arrowSize);
            }

            ctx.closePath();
            ctx.fill();
        }
    }

    circularChart6.id = 'circularGauge6';
    circularChart6.defaults = Chart.DoughnutController.defaults;
    circularChart6.defaults = {
        cutout: '75%',
        circumference: 180,
        rotation: 270,
        responsive: false
    };

    circularChart6.overrides = {
        aspectRatio: 2,
        plugins:{
            legend:{
                display: false
            },
            tooltip:{
                enabled: false
            }
        }
    }
    Chart.register(circularChart6);

    let data = [0];

    data = [Math.min(Math.abs(yValues1), 100), 100 - Math.min(Math.abs(yValues1), 100)];

    let backgroundColor;

    if(yValues1 > 0 ){
        backgroundColor = [CHART_COLORS.green, CHART_COLORS.grey];
    }else{
        backgroundColor = [CHART_COLORS.red, CHART_COLORS.grey];
    }

    new Chart("turnOver", {
      type: "circularGauge6",
      data: {
        labels: ['매출회전율'],
        datasets: [{
          label: '매출회전율',
          data: data,
          backgroundColor: backgroundColor,
          hoverOffset: 4
        }]
      }
    });
}

function drawOITOChart(xValues, yValues1, yValues2){

    const allYData = [...yValues1];
    var data = Math.floor(Math.floor(Math.max(...allYData)));
    var dataString = data.toString();
    var firstDigit = parseInt(dataString[0]) + 1;
    var suggestedMax = parseInt(firstDigit + '0'.repeat(dataString.length - 1));

    const allYData2 = [...yValues2];
    var suggestedMax2 = Math.floor(Math.max(...allYData2));
    suggestedMax2 = Math.ceil(suggestedMax2/10) *10;

    const hoverLine = {
        id: 'hoverLine',
        beforeDraw: chart => {
            if(chart.tooltip._active && chart.tooltip._active.length){
                const ctx = chart.ctx;
                ctx.save();
                const activePoint = chart.tooltip._active[0];

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x, chart.chartArea.top);
                ctx.lineTo(activePoint.element.x, activePoint.element.y);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x, activePoint.element.y);
                ctx.lineTo(activePoint.element.x, chart.chartArea.bottom);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();
            };
        }
    }

    new Chart("ti_to_chart", {
        type: "bar",
        data: {
            labels: xValues,
            datasets: [
                {
                  label:'총이익',
                  backgroundColor: CHART_COLORS.blue2,
                  data: yValues1,
                  order: 1,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'매출회전율',
                  data: yValues2,
                  type: 'line',
                  pointStyle: 'circle',
                  pointRadius:5,
                  backgroundColor: CHART_COLORS.red2,
                  borderColor: CHART_COLORS.red2,
                  order: 0,
                  yAxisID: 'right-y-axis'
                },
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

                                if(key === '총이익'){
                                    const span = '<span style="color: ' + CHART_COLORS.blue2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 억 원';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '매출회전율'){
                                    const span = '<span style="color: ' + CHART_COLORS.red2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> %';
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
                        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 15 + 'px';
                        tooltipEl.style.top = position.top + window.pageYOffset + 20  + 'px';
                        tooltipEl.style.font = bodyFont.string;
                        tooltipEl.style.padding = tooltipModel.padding + 'px ' + tooltipModel.padding + 'px';
                        tooltipEl.style.pointerEvents = 'none';
                        tooltipEl.style.backgroundColor = '#ffffff';
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
                    max: suggestedMax + 10,
                    min: (suggestedMax + 10) * -1,
                    ticks:{
                        stepSize: (suggestedMax + 10) * 2 / 10
                    }
                },
                'right-y-axis':{
                    id:'right-y-axis',
                    position: 'right',
                    max: suggestedMax2 + 10,
                    min: (suggestedMax2 + 10) * -1,
                    ticks:{
                        stepSize: (suggestedMax2 + 10) * 2 / 10,
                        callback: function(value, index, values){
                            return `${value} %`;
                        }
                    },
                    grid: {
                        drawOnChartArea: false
                    }
                }
            }
        },
        plugins: [hoverLine]
    });
}

function drawKeyFinancialChart(xValues, yValues1, yValues2, yValues3, yValues4, yValues5){

    const allYData = [...yValues1, ...yValues2, ...yValues3];
    var data = Math.floor(Math.max(...allYData));
    var dataString = data.toString();
    var firstDigit = parseInt(dataString[0]) + 1;
    var suggestedMax = parseInt(firstDigit + '0'.repeat(dataString.length - 1));

    const allYData2 = [...yValues4, ...yValues5];
    var suggestedMax2 = Math.floor(Math.max(...allYData2));
    suggestedMax2 = Math.ceil(suggestedMax2/10) *10;

    const hoverLine = {
        id: 'hoverLine',
        beforeDraw: chart => {
            if(chart.tooltip._active && chart.tooltip._active.length){
                const ctx = chart.ctx;
                ctx.save();
                const activePoint = chart.tooltip._active[0];

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x, chart.chartArea.top);
                ctx.lineTo(activePoint.element.x, activePoint.element.y);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x, activePoint.element.y);
                ctx.lineTo(activePoint.element.x, chart.chartArea.bottom);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();
            };
        }
    }

    new Chart("key_financial", {
        type: "bar",
        data: {
            labels: xValues,
            datasets: [
                {
                  label:'매출액',
                  backgroundColor: CHART_COLORS.blue2,
                  data: yValues1,
                  order: 1,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'영업이익',
                  backgroundColor: CHART_COLORS.red2,
                  data: yValues2,
                  order: 1,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'당기순이익',
                  backgroundColor: CHART_COLORS.green2,
                  data: yValues3,
                  order: 1,
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
                  order: 0,
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
                  order: 0,
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
                                    const span = '<span style="color: ' + CHART_COLORS.blue2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 억 원';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '영업이익'){
                                    const span = '<span style="color: ' + CHART_COLORS.red2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 억 원';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '당기순이익'){
                                    const span = '<span style="color: ' + CHART_COLORS.green2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 억 원';
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
                        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 15 + 'px';
                        tooltipEl.style.top = position.top + window.pageYOffset + 20  + 'px';
                        tooltipEl.style.font = bodyFont.string;
                        tooltipEl.style.padding = tooltipModel.padding + 'px ' + tooltipModel.padding + 'px';
                        tooltipEl.style.pointerEvents = 'none';
                        tooltipEl.style.backgroundColor = '#ffffff';
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
                    max: suggestedMax + 10,
                    min: (suggestedMax + 10) * -1,
                    ticks:{
                        stepSize: (suggestedMax + 10) * 2 / 10
                    }
                },
                'right-y-axis':{
                    id:'right-y-axis',
                    position: 'right',
                    max: (suggestedMax2 + 10),
                    min: (suggestedMax2 + 10) * -1,
                    ticks:{
                        stepSize: (suggestedMax2 + 10) * 2 / 10,
                        callback: function(value, index, values){
                            return `${value} %`;
                        }
                    },
                    grid: {
                        drawOnChartArea: false
                    }
                }
            }
        },
        plugins: [hoverLine]
    });
}

function drawProfitGrowthChart(xValues, yValues1, yValues2, yValues3){

    const hoverLine = {
        id: 'hoverLine',
        beforeDraw: chart => {
            if(chart.tooltip._active && chart.tooltip._active.length){
                const ctx = chart.ctx;
                ctx.save();
                const activePoint = chart.tooltip._active[0];

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x, chart.chartArea.top);
                ctx.lineTo(activePoint.element.x, activePoint.element.y);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x, activePoint.element.y);
                ctx.lineTo(activePoint.element.x, chart.chartArea.bottom);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();
            };
        }
    }

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
                        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 15 + 'px';
                        tooltipEl.style.top = position.top + window.pageYOffset + 30 + 'px';
                        tooltipEl.style.font = bodyFont.string;
                        tooltipEl.style.padding = tooltipModel.padding + 'px ' + tooltipModel.padding + 'px';
                        tooltipEl.style.pointerEvents = 'none';
                        tooltipEl.style.backgroundColor = '#ffffff';
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
                    ticks:{
                        callback: function(value, index, values){
                            return `${value} %`;
                        }
                    },
                    grid: {
                        drawOnChartArea: true
                    }
                }
            }
        },
        plugins: [hoverLine]
    });
}

function drawKeyFinancialChart2(xValues, yValues1, yValues2, yValues3){

    const allYData = [...yValues1, ...yValues2];
    var data = Math.floor(Math.max(...allYData));

    var dataString = data.toString();
    var firstDigit = parseInt(dataString[0]) + 1;
    var suggestedMax = parseInt(firstDigit + '0'.repeat(dataString.length - 1));
    const allYData2 = [...yValues3];
    var suggestedMax2 = Math.floor(Math.max(...allYData2));
    suggestedMax2 = Math.ceil(suggestedMax2/10) *10;

    const hoverLine = {
        id: 'hoverLine',
        beforeDraw: chart => {
            if(chart.tooltip._active && chart.tooltip._active.length){
                const ctx = chart.ctx;
                ctx.save();
                const activePoint = chart.tooltip._active[0];

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x, chart.chartArea.top);
                ctx.lineTo(activePoint.element.x, activePoint.element.y);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x, activePoint.element.y);
                ctx.lineTo(activePoint.element.x, chart.chartArea.bottom);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();
            };
        }
    }

    new Chart("key_financial2", {
        type: "bar",
        data: {
            labels: xValues,
            datasets: [
                {
                  label:'자산총계(좌)',
                  backgroundColor: CHART_COLORS.blue2,
                  data: yValues1,
                  order: 1,
                  yAxisID: 'left-y-axis'
                },
                {
                  label:'부채총계(좌)',
                  backgroundColor: CHART_COLORS.red2,
                  data: yValues2,
                  order: 1,
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
                  order: 0,
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
                                    const span = '<span style="color: ' + CHART_COLORS.blue2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 억 원';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '부채총계(좌)'){
                                    const span = '<span style="color: ' + CHART_COLORS.red2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> 억 원';
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
                        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 15 + 'px';
                        tooltipEl.style.top = position.top + window.pageYOffset + 20 + 'px';
                        tooltipEl.style.font = bodyFont.string;
                        tooltipEl.style.padding = tooltipModel.padding + 'px ' + tooltipModel.padding + 'px';
                        tooltipEl.style.pointerEvents = 'none';
                        tooltipEl.style.backgroundColor = '#ffffff';
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
                    max: (suggestedMax + 10),
                    min: (suggestedMax + 10) * -1,
                    ticks:{
                        stepSize: (suggestedMax + 10) * 2 / 10
                    }
                },
                'right-y-axis':{
                    id:'right-y-axis',
                    position: 'right',
                    max: (suggestedMax2 + 10),
                    min: (suggestedMax2 + 10) * -1,
                    ticks:{
                        stepSize: (suggestedMax2 + 10) * 2 / 10,
                        callback: function(value, index, values){
                            return `${value} %`;
                        }
                    },
                    grid: {
                        drawOnChartArea: false
                    }
                }
            }
        },
        plugins: [hoverLine]
    });
}

function drawAssetGrowthChart(xValues, yValues1, yValues2, yValues3, yValues4){

    const hoverLine = {
        id: 'hoverLine',
        beforeDraw: chart => {
            if(chart.tooltip._active && chart.tooltip._active.length){
                const ctx = chart.ctx;
                ctx.save();
                const activePoint = chart.tooltip._active[0];

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x, chart.chartArea.top);
                ctx.lineTo(activePoint.element.x, activePoint.element.y);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();

                ctx.beginPath();
                ctx.moveTo(activePoint.element.x, activePoint.element.y);
                ctx.lineTo(activePoint.element.x, chart.chartArea.bottom);
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'grey';
                ctx.stroke();
                ctx.restore();
            };
        }
    }

    new Chart("asset_growth", {
        type: "line",
        data: {
            labels: xValues,
            datasets: [
                {
                  label:'총자산 증가율',
                  data: yValues1,
                  type: 'line',
                  pointStyle: 'circle',
                  pointRadius:5,
                  backgroundColor: CHART_COLORS.blue2,
                  borderColor: CHART_COLORS.blue2,
                  fill: false
                },
                {
                  label:'유동자산 증가율',
                  data: yValues2,
                  type: 'line',
                  pointStyle: 'rectRot',
                  pointRadius:5,
                  backgroundColor: CHART_COLORS.red2,
                  borderColor: CHART_COLORS.red2,
                  fill: false
                },
                {
                  label:'유형자산 증가율',
                  data: yValues3,
                  type: 'line',
                  pointStyle: 'rect',
                  pointRadius:5,
                  backgroundColor: CHART_COLORS.green2,
                  borderColor: CHART_COLORS.green2,
                  fill: false
                },
                {
                  label:'자기자본 증가율',
                  data: yValues4,
                  type: 'line',
                  pointStyle: 'triangle',
                  pointRadius:5,
                  backgroundColor: CHART_COLORS.purple,
                  borderColor: CHART_COLORS.purple,
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

                                if(key === '총자산 증가율'){
                                    const span = '<span style="color: ' + CHART_COLORS.blue2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> %';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '유동자산 증가율'){
                                    const span = '<span style="color: ' + CHART_COLORS.red2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> %';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '유형자산 증가율'){
                                    const span = '<span style="color: ' + CHART_COLORS.green2 + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> %';
                                    innerHtml += '<tr><td>' + span + '</td></tr>';
                                }else if(key === '자기자본 증가율'){
                                    const span = '<span style="color: ' + CHART_COLORS.purple + ';">' + key + '</span> : <span style="color: black;">' + value + '</span> %';
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
                        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 15 + 'px';
                        tooltipEl.style.top = position.top + window.pageYOffset + 30 + 'px';
                        tooltipEl.style.font = bodyFont.string;
                        tooltipEl.style.padding = tooltipModel.padding + 'px ' + tooltipModel.padding + 'px';
                        tooltipEl.style.pointerEvents = 'none';
                        tooltipEl.style.backgroundColor = '#ffffff';
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
                    ticks:{
                        callback: function(value, index, values){
                            return `${value} %`;
                        }
                    },
                    grid: {
                        drawOnChartArea: true
                    }
                }
            }
        },
        plugins: [hoverLine]
    });
}