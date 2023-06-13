$(document).ready(function() {
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    //데이터 테이블 start
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

                    for (var i = 0; i < dataDate.length; i++) {
                        // TransactionCategory 값을 문자열로 변환하여 할당
                        if (dataDate[i].transactionCategory === "INS") {
                            dataDate[i].transactionCategory = "입고";
                        } else if (dataDate[i].transactionCategory === "OUTS") {
                            dataDate[i].transactionCategory = "출고";
                        }
                    }

                    var numberFormatter = new Intl.NumberFormat('en-US');

                    for (var i = 0; i < dataDate.length; i++) {
                        // amount 값을 포맷팅하여 할당
                        var formattedAmount = numberFormatter.format(dataDate[i].amount);
                        dataDate[i].amount = formattedAmount;
                    }

                    for (var i = 0; i < dataDate.length; i++) {
                        var dateItem = new Date(dataDate[i].trDate) ;
                        var Year = dateItem.getFullYear();
                        var month = ("0" + (dateItem.getMonth() + 1)).slice(-2);
                        var day = ("0" + dateItem.getDate()).slice(-2);
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
                    var numericValue = parseFloat(data.replace(/,/g, ''));
                    result += numericValue;
                });

                var numberFormatter = new Intl.NumberFormat('en-US', {
                    style: 'decimal',
                    maximumFractionDigits: 0
                });
                var formattedResult = numberFormatter.format(result);
                $(api.column(3).footer()).html(formattedResult + '원');
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
            if(searchValue==='입고'){
                searchValue = 'INS';
            }else if(searchValue==='출고'){
                searchValue = 'OUTS';
            }

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

    //기존 검색기능 숨김
    $("#myTable_filter").attr("hidden", "hidden");
});