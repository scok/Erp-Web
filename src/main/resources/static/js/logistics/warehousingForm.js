$(document).ready(function () {
    var link =  document.location.href; //현재 접속한 페이지의 url을 가져옵니다.
    link = link.split("/");
    var myUrl ="";
    if(link[4].substring(0,2) == "in"){  //현재 접속한 url을 가공하여 구매 ,판매인지 구분합니다.
        myUrl = "/logistics/inWarehousing/check";
    }else if(link[4].substring(0,3) == "out"){
        myUrl = "/logistics/outWarehousing/check";
    }
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    $.fn.dataTable.ext.search.push(
        function(settings, data, dataIndex){
            var min = Date.parse($('#fromDate').val());
            var max = Date.parse($('#toDate').val());
            var targetDate = Date.parse(data[6]);

            if( (isNaN(min) && isNaN(max) ) ||
                (isNaN(min) && targetDate <= max )||
                ( min <= targetDate && isNaN(max) ) ||
                ( targetDate >= min && targetDate <= max) ){
                    return true;
            }
            return false;
        }
    )


    var table = $('#myTable').DataTable({
        ajax: {
            "url":myUrl,
            "type":"GET",
            "dataType":"JSON",
            "autoWidth": false,
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            }
        },
        columns: [
            {"data": "wioId"},
            {"data": "acName"},
            {"data": "secName"},
            {"data": "stackAreaCategory"},
            {"data": "prName"},
            {"data": "osQuantity"},
            {"data": "inAndOutDate"},
            {"data": "divisionStatus"}
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
                //체크박스 설정
                targets : 0,
                orderable: false,
                'render' : function(data, type, full, meta) {
                    return '<input type="checkbox" name="checker" value="'+data+'">';
                }
            }
        ],
        initComplete: function(settings, json) {
            //all 체크 박스 누를때 동작하는 함수
            $("#checkall").prop("checked",false);
            $("#checkall").click(function(){
                if($(this).prop("checked")){
                    $('input[name="checker"]').prop('checked',true);
                }
                else {
                    $('input[name="checker"]').prop('checked',false);
                }
            });
        }
    });

    /*테이블의 컬럼별로 검색하는 기능*/
    $('#myTable_filter').prepend('<select id="customSelect"></select>');
    $('#myTable > thead > tr').children().each(function (indexInArray, valueOfElement) {
        if(indexInArray  != 0){
            $('#customSelect').append('<option>'+valueOfElement.innerHTML+'</option>');
        }
    });

    $('.dataTables_filter input').unbind().bind('keyup', function () {
        var colIndex = document.querySelector('#customSelect').selectedIndex;
        table.column(colIndex).search(this.value).draw();
    });

    /* 날짜검색 이벤트 리바인딩 */
    $('#myTable_filter').prepend('<input type="text" id="toDate" placeholder="yyyy-MM-dd"> ');
    $('#myTable_filter').prepend('<input type="text" id="fromDate" placeholder="yyyy-MM-dd">~');
    $('#toDate, #fromDate').unbind().bind('keyup',function(){
        table.draw();
    })

   //modal 관련 설정.
   const modal = document.getElementById("modal")
   const closeBtn = modal.querySelector(".close-area")

    //x 버튼 누를시 나가짐
    closeBtn.addEventListener("click", e => {
        modalOff()
    })

    //esc 누를시 modal 나가짐
    window.addEventListener("keyup", e => {
        if(isModalOn() && e.key === "Escape") {
            modalOff()
        }
    })

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

//데이터 입고 등록
function OrderSheetUpdate(){
    //창고의 정보와 섹션의 정보가 추가로 들어와야 합니다.
    var selectElement = document.getElementById("acCode");
    if(selectElement.disabled = false ){// 읽기 전용이 아닐때
        selectElement.disabled = true; // 다시 읽기 전용으로 설정
    }

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var formData = new FormData(document.forms.namedItem("myForm"));

    var array = {}
    for(var item of formData.entries()){
        if(item[0] == "secCode" || item[0] == "SACategory"){
            if(item[1] != null && item[1] != ''){
                array[item[0]]=item[1];
            }else{
                alert("창고 또는 구역을 선택해주세요.");
                return;
            }
        }else{
            array[item[0]]=item[1];
        }
    }
    $.ajax({
        url: "/logistics/updateOrderSheets",
        type: "POST",
        contentType:'application/json',
        data: JSON.stringify(array),
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function () {
            $('#myTable').DataTable().ajax.reload();
            modalOff();
            alert("저장 성공");
        },
        error: function (request, status) {
            alert(request.responseText);
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
   });
}

//주문서 정보를 불러옵니다.
function update(){

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var checkList = $('input[name=checker]:checked');

    if(checkList.length > 1 || checkList.length == 0 ){
        alert('데이터 수정은 1개씩 가능합니다.');
        return
    }
    var code = checkList.val();

    var paramData = JSON.stringify(code);

    $.ajax({
        url: "/orderSheets/updateOrderSheet",
        type: "POST",
        contentType:"application/json",
        data: paramData,
        dataType: "json",
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function (data) {
            modalOn();
            var item = data["data"];
            console.log(item);
            $("#acCode").val(item.acCode).prop("selected", true); //셀렉트 박스 체크

            $("#acCode").prop("disabled", true);    //거래처 셀렉트 박스를 읽기 전용으로 바꿔 줍니다.

            for (var osdItem of item.orderSheetDetails){
                console.log(osdItem);
                var tableTd = '<tr id='+osdItem.product.prCode+'> <td>' + item.acCategory + '</td>';
                tableTd += '<td id='+item.osCode+'-prName value='+osdItem.product.prName+'>' + osdItem.product.prName + '</td>';
                tableTd += '<td id='+osdItem.product.prCode+'-osPrice value='+osdItem.osPrice+'>' + comma(osdItem.osPrice) + '</td>';
                tableTd += '<td id= '+osdItem.product.prCode+'-osStandard>'+osdItem.osStandard+'</td>';
                tableTd += '<td id='+ osdItem.product.prCode + '-osQuantity value='+osdItem.osQuantity+'>'+osdItem.osQuantity+'</td>';
                tableTd += '<td id='+osdItem.product.prCode+'-osSupplyValue value='+osdItem.osSupplyValue+'>'+comma(osdItem.osSupplyValue)+'</td>';
                tableTd += '<td id='+osdItem.product.prCode+'-osTaxAmount value='+osdItem.osTaxAmount+'>'+comma(osdItem.osTaxAmount)+'</td>';

                $('#productTable tbody').append(tableTd);
            }

            $("#osTotalPrice").attr("value",item.osTotalPrice);
            $("#osTotalPrice").html(comma(item.osTotalPrice)); // 총 금액을 특정 요소에 반영

            $("#osReceiptDate").attr("value",item.osReceiptDate);
            $("#osReceiptDate").html(item.osReceiptDate); // 입고 예정 일자 요소에 반영

            $("#osComment").attr("value",item.osComment);
            $("#osComment").html(item.osComment); // 특이사항 요소에 반영

            setTimeout(function() { //지연작업 비동기 통신으로 셀렉트 박스를 그리기 때문에 지연작업이 필요함 => 다그리기 전에 호출해버려서 실패함
                $("#prCode").val(item.orderSheetDetails[0].product.prCode).prop("selected", true); //셀렉트 박스 체크
            }, 150);

            $('#myForm')
            .append(`<input type="hidden" id="osCode" name="osCode" value=${item.osCode}>`);
        },
        error: function (request, status) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    })
}
//웹페이지 삭제 기능.
function deletePageN(){

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var checkList = $('input[name=checker]:checked');
    if(checkList.length == 0 ){
        alert('1개 이상 선택해주세요.');
        return
    }

    var values = [];
    checkList.each(function() {
      values.push($(this).val());
    });

    var paramData = JSON.stringify(values);

     $.ajax({
        url: "/orderSheets/deleteOrderSheets",
        type: "POST",
        contentType:"application/json",
        data: paramData,
        dataType: "json",
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function (data) {
            alert("success");
            $('#myTable').DataTable().ajax.reload();
        },
        error: function (request, status) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
}
