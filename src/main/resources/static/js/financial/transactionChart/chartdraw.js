





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

        var Chart4XValues = [];
        var Chart4YValues = [];
         var tanasctionName ;
        var Chart5XValues = [] ;
        var Chart5YValues = [] ;
        var Chart5NValues   ;
           //팝업 start
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
                //팝업 end

                //버튼 클릭 start
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
                              text: '거래분류'
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

                       $.each(data,function(index,data){

                           if (!uniqueCompanies.includes(data.transactionCategory)) {
                              uniqueCompanies.push(data.transactionCategory);

                                if (data.transactionCategory === "INS") {
                                    data.transactionCategory = "입고";
                                } else if (data.transactionCategory === "OUTS") {
                                    data.transactionCategory = "출고";
                                }

                              $('#select_box03').append($('<option>', {
                                  value: data.transactionCategory,
                                  text: data.transactionCategory
                              }));
                             }
                      });

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
            //버튼end


});


