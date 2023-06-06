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
            $('#myTable').DataTable({
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
                ]
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

//창고를 선택하면 재고를 구역별 재고 현황을 알려줍니다.
function getSectionInfo(secCode){

    var table = $("#myTable").DataTable();
    table.destroy();

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    if(secCode == "" || secCode == null){
        alert("창고를 선택해주세요.");
        return;
    }

    $.ajax({
        url: "/logistics/inventory/sectionInfo",
        type: "POST",
        contentType:"application/json",
        data: JSON.stringify(secCode),
        dataType: "json",
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function (result) {
            $('#myTable').DataTable({
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
                /*columnDefs: [
                    {
                        targets : 0,
                        // 검색 기능 숨기기
                        order: desc
                    }
                ]*/
                });
            },
        error: function (request, status) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
}



