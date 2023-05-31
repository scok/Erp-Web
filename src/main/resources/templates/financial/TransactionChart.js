
               $.ajax({
                url:'/transaction/select',
                type:'GET',
                datatype:'JSON',
                success: function(data){
                    console.log('data : ' + data[0]);
                    $('#select_box').empty();


                    var uniqueCompanies = [];
                    $.each(data,function(index,company){

                         if (!uniqueCompanies.includes(company.companyName)) {
                            uniqueCompanies.push(company.companyName);

                            $('#select_box').append($('<option>', {
                                value: company.companyName,
                                text: company.companyName
                            }));
                        }
                    });
                },
                error: function(xhr, status ,error){
                    alert(xhr.responseJSON.message);
                }
            });

              $('#select_box').on('change', function(){

                var companyName = $(this).val();
                     console.log('선택된 거래처명:', companyName);
                $.ajax({
                    url:'/transaction/select',
                    type:'POST',
                     beforeSend: function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    datatype:'JSON',
                    data:{ companyName : companyName },

                    success: function(response){
                         // 차트 초기화
                        var previousChart = Chart.getChart('myChart4');
                        if (previousChart) {
                          previousChart.destroy();
                        }
                        var resultData = {} ;
                        for (let bean of response) {

                            var date = new Date(bean.trDate);
                            var year = date.getFullYear();
                            var month = ('0' + (date.getMonth() + 1)).slice(-2);
<!--                        var day = ('0' + date.getDate()).slice(-2);-->

                            var trDate = year + '-' + month ;
                            var amounts = bean.amount ;

                            if (resultData[trDate]) {
                                resultData[trDate] += amounts;
                            }else{
                                resultData[trDate] = amounts;
                            }
                        }


                        var aggregatedXValues = Object.keys(resultData);
                        var aggregatedYValues = Object.values(resultData);

                        drawTransactionChart2(aggregatedXValues, aggregatedYValues);


                    },
                    error: function(xhr, status ,error){
                    alert(xhr.responseJSON.message);
                    }
                });


            });





            drawTransactionChart();

            $.ajax({
                url: '/transaction/chart',
                type: 'GET',
                success: function(data) {
                    console.log('데이터 불러오기 성공2');
                    var aggregatedData = {};
                    console.log(data[0]);


                        var date = new Date(data[0].trDate);
                        var year = date.getFullYear();
                        var month = ('0' + (date.getMonth() + 1)).slice(-2);


                        var formattedDate = year + '-' + month ;
                        var amount = data[0].amount;


                        if (aggregatedData[formattedDate]) {
                            aggregatedData[formattedDate] += amount;
                        }else{
                            aggregatedData[formattedDate] = amount;
                        }


                    console.log(aggregatedData);
                    var ChartXValues = Object.keys(aggregatedData);
                    var ChartYValues = Object.values(aggregatedData);
                    drawTransactionChart2(ChartXValues, ChartYValues);
                },
                error: function(xhr, status, error) {
                    if (xhr.status == '401') {
                        alert('로그인 후 이용 가능합니다.');
                    }else{
                        alert(xhr.responseJSON.message);
                    }
                    console.log('에러 메시지:', error);
                }
                });

            });


        function drawTransactionChart2(xValues, yValues){

            new Chart('myChart4', {
                type: 'line',
                data: {
                    labels: xValues,
                    datasets: [{
                        label: '거래처 그래프',
                        data: yValues,
                        borderColor: 'rgba(146, 207, 149,0.6)',
                        backgroundColor: 'rgba(146, 207, 149,0.6)',

                   }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: '거래처 월별 거래금액'
                        }
                    }
                }
            });