$(document).ready(function() {
 var token = $('meta[name="_csrf"]').attr('content');
         var header = $('meta[name="_csrf_header"]').attr('content');


         //차트 1 start
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
                                        borderColor: 'rgba(18, 20, 193,0.8)',
                                        backgroundColor: 'rgba(18, 20, 193,0.8)',

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
                        //차트1 end

                      //차트2start

                          function drawTransactionChart3(xValues, yValues){
                          var previousChart = Chart.getChart('myChart5');
                            if (previousChart) {
                              previousChart.destroy();
                            }
                            var selectComName = $('#select_box01').val();
                        new Chart('myChart5', {
                            type: 'bar',
                            data: {
                                labels: xValues,
                                datasets: [{
                                    label: '거래금액',
                                    data: yValues,
                                    borderColor: 'rgb(198, 20, 29)',
                                    backgroundColor: 'rgb(198, 20, 29)',

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

          //날짜 변환 메소드
            function DateChange(comeOn) {
                var date = new Date(comeOn);
                var year = date.getFullYear();
                var month = ('0' + (date.getMonth() + 1)).slice(-2);

                var trDate = year + '-' + month ;
                return trDate ;
            };






                    // 입고출고 셀렉 start
             $('#select_box03').on('change', function(){


                    var thisInAndOut = $(this).val();



                     if (thisInAndOut === "입고") {
                        var transactionCategory = "INS";
                      } else if (thisInAndOut === "출고") {
                        var transactionCategory = "OUTS";
                      }else{
                        return   ;

                      }


                 $.ajax({
                  url:'/transaction/select',
                  type:'POST',
                   beforeSend: function(xhr) {
                      xhr.setRequestHeader(header, token);
                  },
                  datatype:'JSON',
                  data:{ transactionCategory : transactionCategory },

                  success: function(response){




                   var uniqueCompanies = [];
                    for(let bean of response){

                     $('#select_box01').empty();
                    $('#select_box02').empty();

                    var selectBox01 = $('#select_box01');
                    var selectBox02 = $('#select_box02');


                    selectBox01.append($('<option>', {
                      value: '',
                      text: '거래처명'
                    }));
                    selectBox02.append($('<option>', {
                          value: '',
                          text: '연도'
                    }));





                     if (!uniqueCompanies.includes(bean.companyName)) {
                              uniqueCompanies.push(bean.companyName);

                        $('#select_box01').append($('<option>', {
                            value: bean.companyName,
                            text: bean.companyName
                        }));

                    }
                   }


              // 셀렉 조회 start
              $('#select_box01').on('change', function(){
                $('#select_box02').empty();


                var selectBox02 = $('#select_box02');

                selectBox02.append($('<option>', {
                      value: '',
                      text: '연도'
                }));


                    tanasctionName = $(this).val();


                         // 차트 초기화
                        var previousChart = Chart.getChart('myChart4');
                        if (previousChart) {
                          previousChart.destroy();
                        }
                        var resultData = {} ;
                        var datatrDate = {};

                        for (let bean of response) {

                            var date = new Date(bean.trDate);
                            var year =  date.getFullYear()+'/12';

                            var amounts = bean.amount ;

                            if (resultData[year]) {
                                resultData[year] += amounts;
                            }else{
                                resultData[year] = amounts;
                            }
                        }
                        //for_end

                        var aggregatedXValues = Object.keys(resultData);
                        var aggregatedYValues = Object.values(resultData);


                        drawTransactionChart2(aggregatedXValues, aggregatedYValues);





                        //2번 셀렉창에 넘길 데이터
                        var uniqueCompanies2 = [] ;


                        for(let bean of response){


                            $('#select_box02').empty();


                            var selectBox02 = $('#select_box02');



                            selectBox02.append($('<option>', {
                                  value: '',
                                  text: '연도'
                            }));


                            var date = new Date(bean.trDate);
                            var year =  date.getFullYear();


                             if (!uniqueCompanies2.includes(year)) {
                                uniqueCompanies2.push(year);

                                $('#select_box02').append($('<option>', {
                                    value: year,
                                    text: year
                            }));

                            }
                        }
                         //for_end

                        //select_box02, chart5 start
                        $('#select_box02').on('change', function(){
                              var previousChart = Chart.getChart('myChart5');
                                if (previousChart) {
                                  previousChart.destroy();
                                }


                            var selectDate = $('#select_box02').val();
                                Chart5NValues = $('#select_box02').val();
                            var enddate = {};
                            var amonuts = [] ;
                            var OrgDate = [] ;
                            var totalData = {} ;
                            var totalData02 = 0;

                           // key와 val add 메소드
                          function addKey(key,val){
                                 if(enddate[key]){
                                   enddate[key] += val;
                                }else{
                                   enddate[key] = val;
                                }
                                return enddate ;
                            };

                            for(let bean of response){
                                OrgDate = DateChange(bean.trDate);
                                  amonuts = bean.amount;
                                  var compareDate =  OrgDate.split('-') ;
                                  var dateYear = compareDate[0] ;

                                    if(dateYear===selectDate){
                                        var dateMonth = parseInt(compareDate[1]);

                                        if(dateMonth>= 1 && dateMonth<=3){
                                            var keyVal = dateYear+ '-03' ;
                                              addKey(keyVal,amonuts);

                                        }else if(dateMonth>=4 && dateMonth<=6){
                                              var keyVal = dateYear+ '-06' ;
                                               addKey(keyVal,amonuts);

                                        }else if(dateMonth>= 7 && dateMonth<=9){
                                            var keyVal = dateYear+ '-09' ;
                                               addKey(keyVal,amonuts);

                                        }else{
                                            var keyVal = dateYear+ '-12' ;
                                                addKey(keyVal,amonuts);
                                        }
                                    }
                                    for(var key in enddate){
                                        totalData02 += enddate[key];
                                    }

                            }
                            //for_end

                         var aggregatedXValues = Object.keys(enddate);
                         var aggregatedYValues = Object.values(enddate);

                        drawTransactionChart3(aggregatedXValues, aggregatedYValues);
                        });
                        //change_end


                       });
                                //셀렉 조희 end

                    },
                    //success_end
                    error: function(xhr, status ,error){
                    alert(xhr.responseJSON.message);
                    }
                });




            });
            //입고 출고 셀렉 end
});

