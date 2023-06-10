//창고 전체 조회
$(document).ready(function () {
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    $.ajax({
        url: "/logistics/inventory/check",
        type: "GET",
        contentType:"application/json",
        dataType: "json",
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function (result) {
            var table = $('#myTable').DataTable({
                data:result.data,
                dataSrc:"",
                columns: [
                    {data: "inId"},
                    {data: "secName"},
                    {data: "secCategory"},
                    {data: "prCode"},
                    {data: "stackAreaCategory"},
                    {data: "prName"},
                    {data: "osStandard"},
                    {data: "arTotalCount"},
                    {data: "acName"}
                ],

                "language": {
                    "emptyTable": "데이터가 없어요.",
                    "lengthMenu": "페이지당 _MENU_ 개씩 보기",
                    "info": "현재 _START_ - _END_ / _TOTAL_건",
                    "infoEmpty": "데이터 없음",
                    "infoFiltered": "( _MAX_건의 데이터에서 필터링됨 )",
                    "search": "검색: ",
                    "zeroRecords": "일치하는 데이터가 없어요.",
                    "loadingRecords": "로딩중...",
                    "processing":     "잠시만 기다려 주세요...",
                    "paginate": {
                        "next": "다음",
                        "previous": "이전"
                    }
                },
                columnDefs: [
                    {
                        targets : 0,
                        'render' : function(data) {
                        return '<td><span class="inId">'+data+'</span></td>';
                        }
                    },
                    {
                        targets : 6,
                        'render' : function(data) {
                        return '<th>'+comma(data)+'<th>';
                        }
                    }
                ],
            });
            /*테이블의 컬럼별로 검색하는 기능*/
            $('#myTable_filter').prepend('<select id="customSelect"></select>');
            $('#myTable > thead > tr').children().each(function (indexInArray, valueOfElement) {
                $('#customSelect').append('<option>'+valueOfElement.innerHTML+'</option>');
            });
            $('.dataTables_filter input').unbind().bind('keyup', function () {
                var colIndex = document.querySelector('#customSelect').selectedIndex;
                table.column(colIndex).search(this.value).draw();
            });
        },
        error: function (request, status) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
});
//정규 표현식을 이용한 자릿수 표현
function comma(num){
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
/*데이터 베이스 엑셀화*/
function excel(){

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var inputs = document.getElementsByClassName("inId");
    var values = Array.from(inputs).map(function(input) {
      return input.innerText ;
    });
    paramData = {};
    paramData[["inventory"]]=values;

    var excelDownloadState = false;
    if(excelDownloadState == false){

        excelDownloadState = true;

        //xmlhttprequest 통신.
        var request = new XMLHttpRequest();
        request.open('POST', 'http://localhost:8877/excel/download',true);
        request.setRequestHeader(header, token);
        request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        request.responseType = 'blob';

        request.onload = function(e) {
            excelDownloadState = false;

            if (this.status === 200) {
                var blob = this.response;
                var fileName = "Inventory_Info.xlsx"
                    if(window.navigator.msSaveOrOpenBlob) {
                        window.navigator.msSaveBlob(blob, fileName);
                    }else{
                        var downloadLink = window.document.createElement('a');
                        var contentTypeHeader = request.getResponseHeader("Content-Type");
                        downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
                        downloadLink.download = fileName;
                        document.body.appendChild(downloadLink);
                        downloadLink.click();
                        document.body.removeChild(downloadLink);
                   }
            }else{
               alert("엑셀파일생성에 실패하였습니다.");
            }
        };
    request.send(JSON.stringify(paramData));
    }
}


