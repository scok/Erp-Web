$(document).ready(function () {
    deleteButton();
    divSectionStatusDisplayOff();

    var link =  document.location.href; //현재 접속한 페이지의 url을 가져옵니다.
    link = link.split("/");
    var myUrl ="";
    if(link[4].substring(0,3) == "buy"){  //현재 접속한 url을 가공하여 구매 ,판매인지 구분합니다.
        myUrl = "/logistics/buyOrderSheet/check";
    }else if(link[4].substring(0,6) == "seller"){
        myUrl = "/logistics/sellerOrderSheet/check";
    }
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

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
        order : [[1, 'desc']],
        columns: [
            {"data": "osCode"},
            {"data": "acCategory"},
            {"data": "prName"},
            {"data": "osTotalPrice"},
            {"data": "acName"},
            {"data": "acCeo"},
            {"data": "acHomePage"},
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
              return '<span id="tableInnerCheckBox"><input type="checkbox" name="checker" value="'+data+'"></span>';
             }
           },
            {
                targets : 3,
                orderable: false,
                'render' : function(data) {
                 return '<td>'+comma(data)+'</td>';
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

   /* Column별 검색기능 추가 */
   $('#myTable_filter').prepend('<select id="customSelect"></select>');
   $('#myTable > thead > tr').children().each(function (indexInArray, valueOfElement) {
     if(valueOfElement.innerHTML !="총금액" && indexInArray != 0
     &&valueOfElement.innerHTML !="입고예정일"){
         $('#customSelect').append('<option>'+valueOfElement.innerHTML+'</option>');
     }
   });

   $('#customSelect').on("change",function(){
     table.search('').draw();
     table.columns().search('').draw();
   });

   $('.dataTables_filter input').unbind().bind('keyup', function () {

     var colValue = document.querySelector('#customSelect').value;

     var colHeaders = table.columns().header().toArray();

     var targetIndex = colHeaders.findIndex(function(header) {
       return header.innerHTML === colValue;
     });

     var keyWord = this.value;

     table.column(targetIndex).search(keyWord).draw();
   });

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
//라디오 버튼 클릭시
function radioClick(value){
    if(value == '입고' || value == '출고'){
        createButton();
        divSectionStatusDisplayOn();

        var token = $('meta[name="_csrf"]').attr('content');
        var header = $('meta[name="_csrf_header"]').attr('content');

        var link =  document.location.href; //현재 접속한 페이지의 url을 가져옵니다.
        link = link.split("/");
        var myUrl ="";
        if(link[4].substring(0,3) == "buy"){  //현재 접속한 url을 가공하여 구매 ,판매인지 구분합니다.
            myUrl = "/section/getSectionsMaterial";
        }else if(link[4].substring(0,6) == "seller"){
            myUrl = "/section/getSectionsProduct";
        }

        $.ajax({
            url: myUrl,
            type: "GET",
            contentType:'application/json',
            dataType: "json",
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            },
            success: function (data) {
                console.log(data);

                //selectBox 불러오기
                var selectBox = document.getElementById('secCode');
                // 기존 옵션들을 모두 제거
                selectBox.innerHTML = '<option value="">==창고 선택==</option>';

                // 데이터를 기반으로 새로운 옵션들을 추가
                for (var item of Object.values(data)) {
                    var option = document.createElement('option');
                    option.value = item["secCode"];
                    option.textContent = item["secName"];
                    selectBox.appendChild(option);
                }
            },
            error: function (request, status) {
                alert(request.responseText);
                console.log("code:"+request.status+"\n"+"견적서 message:"+request.responseText+"\n");
            }
            });
    }
}

//동적 버튼 보이기
function createButton(){
    $("#saveBtn").css("display", "");//저장 기능을 보이게 해줍니다.
}
//동적 버튼 숨기기.
function deleteButton(){
    $("#saveBtn").css("display", "none");//저장 기능을 안보이게 해줍니다.
}
function divSectionStatusDisplayOn(){   //창고, 구역등록을 보여줍니다.
     $("#DivSectionStatus").css("display", "");//저장 기능을 보이게 해줍니다.
}
function divSectionStatusDisplayOff(){  //창고, 구역등록을 안보여줍니다.
    $("#DivSectionStatus").css("display", "none");//저장 기능을 보이게 해줍니다.
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
        error: function (message,request, status) {
            alert(Object.values(message));
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
            $("#osTotalPrice").html("Total: &#8361; "+ comma(item.osTotalPrice)); // 총 금액을 특정 요소에 반영

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


/*$(".modal_close").on("click", function () {
    action_popup.close(this);
});*/

/**
 *  alert, confirm 대용 팝업 메소드 정의 <br/>
 *  timer : 애니메이션 동작 속도 <br/>
 *  alert : 경고창 <br/>
 *  confirm : 확인창 <br/>
 *  open : 팝업 열기 <br/>
 *  close : 팝업 닫기 <br/>
 */
var action_popup = {
    confirm: function (txt, callback) {
        if (txt == null || txt.trim() == "") {
            console.warn("confirm message is empty.");
            return;
        } else if (callback == null || typeof callback != 'function') {
            console.warn("callback is null or not function.");
            return;
        } else {
            $(".type-confirm .btn_ok").on("click", function () {
                $(this).unbind("click");
                callback(true);
                action_popup.close(this);
            });
            this.open("type-confirm", txt);
        }
    },

    alert: function (txt) {
        if (txt == null || txt.trim() == "") {
            console.warn("confirm message is empty.");
            return;
        } else {
            this.open("type-alert", txt);
        }
    },

    open: function (type, txt) {
        var popup = $("." + type);
        popup.find(".menu_msg").text(txt);
        $("body").append("<div class='dimLayer'></div>");
        $(".dimLayer").css('height', $(document).height()).attr("target", type);
        popup.fadeIn(this.timer);
    },

    close: function (target) {
        var modal = $(target).closest(".modal-section");
        var dimLayer;
        if (modal.hasClass("type-confirm")) {
            dimLayer = $(".dimLayer[target=type-confirm]");
            $(".type-confirm .btn_ok").unbind("click");
        } else if (modal.hasClass("type-alert")) {
            dimLayer = $(".dimLayer[target=type-alert]")
        } else {
            console.warn("close unknown target.")
            return;
        }
        modal.fadeOut(this.timer);
        setTimeout(function () {
            dimLayer != null ? dimLayer.remove() : "";
        }, this.timer);
    }
}
