$(document).ready(function() {
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    function drawTransactionChart2(xValues, yValues){
        var previousChart = Chart.getChart('myChart4');
        if (previousChart) {
            previousChart.destroy();
        }
        var selectComName = $('#select_box01').val();
        new Chart('myChart4', {
            type: 'bar',
            data: {
                labels: xValues,
                datasets: [{
                    label:'거래금액',
                    data: yValues,
                    borderColor: 'rgb(255, 99, 132)',
                    backgroundColor: 'rgb(255, 99, 132)',
               }]
            },
            options: {
                scales: {
                    x: {
                        ticks: {
                            font: {
                                size: 10 // x축 레이블 폰트 크기 설정
                            }
                        }
                    },
                    y: {
                        ticks: {
                            font: {
                                size: 10 // y축 레이블 폰트 크기 설정
                            }
                        }
                    }
                },
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: (selectComName==null) ? '연도별 거래금액 차트' : selectComName + ' 연도별 거래금액 차트'
                    }
                }
            }
        });
    }
    //차트4 end

    //차트start
    function drawTransactionChart3(xValues, yValues){
        var selectComName = $('#select_box01').val();
        new Chart('myChart5', {
            type: 'bar',
            data: {
                labels: xValues,
                datasets: [{
                    label: '거래금액',
                    data: yValues,
                    borderColor: 'rgb(255, 205, 86)',
                    backgroundColor: 'rgb(255, 205, 86)',
               }]
            },
            options: {
                scales: {
                    x: {
                        ticks: {
                            font: {
                                size: 10 // x축 레이블 폰트 크기 설정
                            }
                        }
                    },
                    y: {
                        ticks: {
                            font: {
                                size: 10 // y축 레이블 폰트 크기 설정
                            }
                        }
                    }
                },
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: (selectComName==null) ? '분기별 거래금액 차트' : selectComName + ' 분기별 거래금액 차트'
                    }
                }
            }
        });
    }
    //차트5end


    //입고 pie 차트 start
    function drawTransactionChart4(xValues, yValues){
        var previousChart = Chart.getChart('myChartPie');
            if (previousChart) {
              previousChart.destroy();
            }

        new Chart('myChartPie', {
            type: 'pie',
            data : {
                labels:
                    yValues
                ,
                datasets: [{
                    label: '입고 거래수',
                    backgroundColor: [
                        'rgb(68, 150, 77)',
                        'rgb(149, 146, 137)',
                        'rgb(153, 150, 186)',
                        'rgb(181, 193, 133)',
                        'rgb(68, 150, 186)'
                    ],
                    data: xValues
                }]
            },
            options: {
                title: {
                    display: true,
                        text: '입고 거래수'
                    }
                }
        });
    }
    //입고 파이 차트 end

    //출고 파이차트 start
    function drawTransactionChart5(xValues, yValues){
        var previousChart = Chart.getChart('myChartPie2');

        if (previousChart) {
            previousChart.destroy();
        }

        new Chart('myChartPie2', {
            type: 'pie',
            data : {
                labels:
                    yValues
                ,
                datasets: [{
                    label: '출고 거래수',
                    backgroundColor: [
                        'rgb(68, 150, 77)',
                        'rgb(149, 146, 137)',
                        'rgb(153, 150, 186)',
                        'rgb(181, 193, 133)',
                        'rgb(68, 150, 186)'
                    ],
                    data: xValues
                }]
            },
            options: {
                title: {
                    display: true,
                    text: '출고 거래수'
                }
            }
        });
    }
    //출고 파이 차트end

    //입고 거래수
    $.ajax({
        url:'/transaction/countIn',
        type:'GET',
        datatype:'json',
        success: function(data){
            var aggregatedXValues = [] ;
            var aggregatedYValues = [] ;
            for(let bean of data){
                var count = bean.amount ;
                var companyName = bean.companyName ;
                aggregatedXValues.push(count);
                aggregatedYValues.push(companyName);
            }


            drawTransactionChart4(aggregatedXValues, aggregatedYValues);
         },
         error: function(xhr, status ,error){
             alert(xhr.responseJSON.message);
         }
    });
    //입고 거래수 end


    // 출고 거래수 차트
    $.ajax({
        url:'/transaction/countOut',
        type:'GET',
        datatype:'json',
        success: function(data){
            var aggregatedXValues = [] ;
            var aggregatedYValues = [] ;
            for(let bean of data){
                var count = bean.amount ;
                var companyName = bean.companyName ;
                aggregatedXValues.push(count);
                aggregatedYValues.push(companyName);
            }
            drawTransactionChart5(aggregatedXValues, aggregatedYValues);
        },
        error: function(xhr, status ,error){
            alert(xhr.responseJSON.message);
        }
    });
    //출고 거래수 차트 end

    //날짜 변환 메소드
    function DateChange(comeOn) {
        var date = new Date(comeOn);
        var year = date.getFullYear();
        var month = ('0' + (date.getMonth() + 1)).slice(-2);

        var trDate = year + '-' + month ;
        return trDate ;
    };

    var Chart4XValues = [];
    var Chart4YValues = [];
    var tanasctionName ;
    var Chart5XValues = [] ;
    var Chart5YValues = [] ;
    var Chart5NValues ;

    //팝업창 기능 start
    function layer_popup(el){

        var $el = $(el);
        var isDim = $el.prev().hasClass('dimBg');

        isDim ? $('.dim-layer').fadeIn() : $el.fadeIn();

        var $elWidth = ~~($el.outerWidth()),
        $elHeight = ~~($el.outerHeight()),
        docWidth = $(document).width(),
        docHeight = $(document).height();

        if ($elHeight < docHeight || $elWidth < docWidth) {
            $el.css({
            marginTop: -$elHeight /2,
            marginLeft: -$elWidth/2
            });
        } else {
            $el.css({top: 0, left: 0});
        }

        $el.find('a.btn-layerClose').click(function(){
            isDim ? $('.dim-layer').fadeOut() : $el.fadeOut();
            return false;
        });

        $('.layer .dimBg').click(function(){
            $('.dim-layer').fadeOut();
            return false;
        });
    }
    //팝업창 기능 end

    //팝업 버튼 클릭 start
    $('.btn-example').click(function(){

        var $href = $(this).attr('href');
        layer_popup($href);

        // 월별 차트 작성 start
        $.ajax({
            url:'/transaction/select',
            type:'GET',
            datatype:'json',
            success: function(data){
                var aggregatedData = {};
                var uniqueCompanies = [] ;

                for(let bean of data){
                    var selectcomName = $('#select_box01').val();
                    var ComName = bean.companyName ;

                    $('#select_box03').empty();
                    $('#select_box01').empty();
                    $('#select_box02').empty();

                    var selectBox03 = $('#select_box03');
                    var selectBox01 = $('#select_box01');
                    var selectBox02 = $('#select_box02');

                    selectBox03.append($('<option>', {
                          value: '',
                          text: '분류'
                    }));

                    selectBox01.append($('<option>', {
                      value: '',
                      text: '거래처명'
                    }));

                    selectBox02.append($('<option>', {
                          value: '',
                          text: '연도'
                    }));

                    var transactionCategory = bean.transactionCategory ;
                    var uniqueCompanies = [];

                    for (var i = 0; i < data.length; i++) {
                      var transactionCategory = data[i].transactionCategory;

                      if (transactionCategory === "INS" && !uniqueCompanies.includes("입고")) {
                        uniqueCompanies.push("입고");

                        // 중복된 값이 없는 경우에만 "입고" 옵션 추가
                        $('#select_box03').append($('<option>', {
                          value: "입고",
                          text: "입고"
                        }));
                      } else if (transactionCategory === "OUTS" && !uniqueCompanies.includes("출고")) {
                        uniqueCompanies.push("출고");

                        // 중복된 값이 없는 경우에만 "출고" 옵션 추가
                        $('#select_box03').append($('<option>', {
                          value: "출고",
                          text: "출고"
                        }));
                      }
                    }

                }
                //for end

                Chart5NValues = ComName;
                drawTransactionChart2(Chart4XValues, Chart4YValues);

                var previousChart = Chart.getChart('myChart5');
                if (previousChart) {
                    previousChart.destroy();
                }

                var selectDate = $('#select_box02').val();

                drawTransactionChart3(Chart5XValues, Chart5YValues);
                //change_end
            },
            error: function(xhr, status ,error){
                alert(xhr.responseJSON.message);
            }
        });
        // 월별 차트 작성 end
    });
    //팝업버튼end
});


