$(document).ready(function() {
              // 셀렉 조회 start
              $('#select_box01').on('change', function(){


                 Chart5NValues = $(this).val();
                    tanasctionName = $(this).val();
                $.ajax({
                    url:'/transaction/select',
                    type:'POST',
                     beforeSend: function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    datatype:'JSON',
                    data:{ companyName : Chart5NValues },

                    success: function(response){

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

                        $('#select_box02').empty();

                        //2번 셀렉창에 넘길 데이터
                        var uniqueCompanies = [] ;
                        var selectBox02 = $('#select_box02');
                        selectBox02.append($('<option>', {
                          value: '',
                          text: '연도'
                        }));


                        for(let bean of response){

                            var date = new Date(bean.trDate);
                            var year =  date.getFullYear();


                             if (!uniqueCompanies.includes(year)) {
                                uniqueCompanies.push(year);

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



                    },
                    //success_end
                    error: function(xhr, status ,error){
                    alert(xhr.responseJSON.message);
                    }
                });


            });
            //셀렉 조희 end
});

