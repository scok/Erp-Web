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
                        targets : 6,
                        'render' : function(data) {
                        return '<th>'+comma(data)+'<th>';
                        }
                    }
                ],
                dom : 'Blfrtip',
                buttons:[{
                    extend:'csvHtml5',
                    text: 'Export CSV',
                    footer: true,
                    bom: true,
                    className: 'exportCSV'
                }]
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
function modalOn() {
    modalOff();
    modal.style.display = "flex"
}
function isModalOn() {
    return modal.style.display === "flex"
}
function modalOff() {
    modal.style.display = "none"
    $("#myForm")[0].reset();
    $("#tableBody").empty();    //테이블을 비워 줍니다.
    var acSelectElement = document.getElementById("acCode");
    if(acSelectElement.disabled = false ){// 읽기 일때 전용으로 설정
        acSelectElement.disabled = true; // 설정 초기화 전용으로 설정
    }
    $("#esTotalPrice").attr("value",0);
    $("#esTotalPrice").html(0); // 총 금액을 특정 요소에 반영
    deleteButton();
    divSectionStatusDisplayOff();
 }

//정규 표현식을 이용한 자릿수 표현
function comma(num){
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}



