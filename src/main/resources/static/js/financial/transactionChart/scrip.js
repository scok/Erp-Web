  $(document).ready(function() {
         var token = $('meta[name="_csrf"]').attr('content');
         var header = $('meta[name="_csrf_header"]').attr('content');



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
                    for(let bean of data){
                    var selectcomName = $('#select_box01').val();

                     var ComName = bean.companyName ;


                    $('#select_box01').empty();
                    $('#select_box02').empty();
                    //if start
                    if(ComName==selectcomName){


                        var date = new Date(bean.trDate);
                        var year = date.getFullYear();
                        var month = ('0' + (date.getMonth() + 1)).slice(-2);

                        var formattedDate = year + '-' + month ;
                        var amount = bean.amount;

                        if (aggregatedData[formattedDate]) {
                            aggregatedData[formattedDate] += amount;
                        }else{
                            aggregatedData[formattedDate] = amount;
                        }
                     }
                     //if end
                 }
                  //for end


                     Chart4XValues = Object.keys(aggregatedData);
                     Chart4YValues = Object.values(aggregatedData);
                     Chart5NValues = ComName;
                     drawTransactionChart2(Chart4XValues, Chart4YValues);



                         var previousChart = Chart.getChart('myChart5');
                                if (previousChart) {
                                  previousChart.destroy();
                                }

                      var selectDate = $('#select_box02').val();

                            var enddate = {};
                            for(let bean of data){
                                var OrgDate = DateChange(bean.trDate);

                                if(OrgDate==selectDate){

                                    var date = new Date(bean.trDate);

                                    var month = ('0' + (date.getMonth() + 1)).slice(-2);
                                    var day = ('0' + date.getDay()).slice(-2);

                                    var trDates = month + '-' + day ;
                                    var amonuts = bean.amount;

                                    if(enddate[trDates]){
                                       enddate[trDates] += amonuts;
                                    }else{
                                       enddate[trDates] = amonuts;
                                    }

                                }
                                //if_end
                            }
                            //for_end

                         Chart5XValues = Object.keys(enddate);
                         Chart5YValues = Object.values(enddate);

                        drawTransactionChart3(Chart5XValues, Chart5YValues);

                        //change_end


                    var uniqueCompanies = [];
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

                    $.each(data,function(index,company){

                         if (!uniqueCompanies.includes(company.companyName)) {
                            uniqueCompanies.push(company.companyName);

                            $('#select_box01').append($('<option>', {
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
             // 월별 차트 작성 end


        });
        //버튼end


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








            //차트start
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



         //데이터 테이블 start
         $.fn.dataTable.ext.search.push(
            function(settings, data, dataIndex){
                var min = Date.parse($('#fromDate').val());
                var max = Date.parse($('#toDate').val());
                var targetDate = Date.parse(data[3]);

                if( (isNaN(min) && isNaN(max) ) ||
                    (isNaN(min) && targetDate <= max )||
                    ( min <= targetDate && isNaN(max) ) ||
                    ( targetDate >= min && targetDate <= max) ){
                        return true;
                }
                return false;
            }
         );

          if (!$.fn.DataTable.isDataTable('#myTable')) {


            var table =  $('#myTable').DataTable({
                  "serverSide": true,
                    "processing": true,
                    "paging": true,
                ajax: {
                    "url": "/transaction/data",
                    "type": "POST",
                    beforeSend:function(xhr){
                        xhr.setRequestHeader(header, token);
                    },
                    "data": function (d) {


                        d.searchType = $("#searchType").val();
                        d.searchValue = $("#searchValue").val();

                        d.columnIndex = d.order[0].column;
                        d.orderDir = d.order[0].dir;



                    },
                  "dataSrc": function (response) {
                      var dataDate = response.data;
                        console.log('response.total' + response.recordTotal);
                        for (var i = 0; i < dataDate.length; i++) {
                        var dateItem = new Date(dataDate[i].trDate) ;

                        var Year = dateItem.getFullYear();
                        var month = ("0" + (dateItem.getMonth() + 1)).slice(-2);

                        var day = ("0" + dateItem.getDay()).slice(-2);

                        var formatDate = Year + "-"+ month + "-"+day;
                        dataDate[i].trDate = formatDate ;


                        response.draw = response.draw;
                        response.recordsTotal = response.recordTotal;
                        response.recordsFiltered = response.recordFiltered;

                      }

                     return response.data;

                }

                },

                    "autoWidth": false,
                    searching: true,
                    ordering: true,
                    "lengthMenu": [10, 20, 50],
                columns: [
                    { "data": "id" },
                    { "data": "companyName" },
                    { "data": "amount" },
                    { "data": "trDate" },
                    { "data": "quarter" },
                    { "data": "remark" },
                    { "data": "transactionCategory" }
                ],
                "language": {
                    "emptyTable": "데이터가 없어요.",
                    "lengthMenu": "Page _MENU_ ",
                    "info": "현재 _START_ - _END_ / _TOTAL_건",
                    "infoEmpty": "데이터 없음",
                    "infoFiltered": "( _MAX_건의 데이터에서 필터링됨 )",
                    "search": "검색: ",
                    "zeroRecords": "일치하는 데이터가 없어요.",
                    "loadingRecords": "로딩중...",
                    "processing": "잠시만 기다려 주세요...",
                    "paginate": {
                        "next": "다음",
                        "previous": "이전"
                    }
                },

                //거래금액 합산
                "footerCallback": function() {
                    var api = this.api(), data;
                    var result = 0;
                    api.column(2, {search:'applied'}).data().each(function(data, index) {
                        result += parseFloat(data);
                    });
                    $(api.column(3).footer()).html(result.toLocaleString() + '원');
                },


              <!--엑셀 파일 전송-->
               dom: 'fBrtlpa',
                buttons: [{
                        extend: 'csvHtml5',
                        text: 'Excel',
                        footer: true,
                        className: 'blueBtn',
                        exportOptions: {
                            charset: 'euc-kr'
                        },
                        bom: true

                    }],
            });


            <!--검색 기능-->

          $("#searchBtn").click(function () {
            var numCols = table.columns().nodes().length;
            for(var i=0; i<numCols; i++) { table.column(i).search(''); }

            var searchType = $("#searchType").val();
            var searchValue = $("#searchValue").val();

            table.column(searchType).search(searchValue).draw();
          });


            $('#myTable thead th').click(function () {
            var columnIndex = $(this).index();
            var orderDir = $(this).hasClass('asc') ? 'desc' : 'asc';

            // DataTables의 정렬 업데이트
            table.order(columnIndex, orderDir).draw();

            // 현재 클릭한 컬럼에 클래스를 추가하여 정렬 방향을 표시
            $('#myTable thead th').removeClass('asc').removeClass('desc');
            $(this).addClass(orderDir);


        });

     }
        //검색 기능 end


        $("#myTable_filter").attr("hidden", "hidden");



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

    });// ready 닫기