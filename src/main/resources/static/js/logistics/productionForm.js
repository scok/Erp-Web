$(document).ready(function () {
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var table = $('#myTable').DataTable({
        ajax: {
            "url":"/productions/check",
            "type":"GET",
            "dataType":"JSON",
            "autoWidth": false,
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            },
            // 가로 스크롤바를 표시
           	// 설정 값은 true 또는 false
           	scrollX: true,

           	// 세로 스크롤바를 표시
           	// 설정 값은 px단위
           	scrollY: 200,
            ordering: true,
        },
        columns: [
            {"data": "proCode"},
            {"data": "productionLine"},
            {"data": "meName"},
            {"data": "prName"},
            {"data": "count"},
            {"data": "registrationDate"}
        ],
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
   //modal 관련 설정.
   const modal = document.getElementById("modal")
   const btnModal = document.getElementById("btn-modal")
   const closeBtn = modal.querySelector(".close-area")

    btnModal.addEventListener("click", e => {
        modalOn()
    })

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
    modal.style.display = "flex"
}
function isModalOn() {
    return modal.style.display === "flex"
}
function modalOff() {
    modal.style.display = "none"
    $('#myTable').DataTable().ajax.reload();
    $("#myForm")[0].reset();
 }
//실적 등록.
function addProduction(){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var formData = new FormData(document.forms.namedItem("myForm"));

    // 초기 등록시에는 code의 값이 없을수밖에 없으니 code에 값이 없으면 배열에 저장하지 않습니다.

    var targetData = {};
    for (var pair of formData.entries()) {
        targetData[pair[0]]=pair[1];
    }
    var paramData = JSON.stringify(targetData);

    console.log(paramData);
    $.ajax({
        url: "/productions/addProduction",
        type: "POST",
        contentType:'application/json',
        data: paramData,
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function () {
            alert("success");
            $('#myTable').DataTable().ajax.reload();
            modalOff();
        },
        error: function (request, status) {
            alert(request.responseText);
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
}
//데이터 베이스 수정 기능.
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
        url: "/products/updateProduct",
        type: "POST",
        contentType:"application/json",
        data: paramData,
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function (data) {
            var key = Object.keys(data);    //넘겨 받은 데이터의 키 값
            const entries = Object.entries(data);

            $("#acCode").val(data.acCode).prop("selected", true); //셀렉트 박스 체크
            $("#prCategory").val(data.prCategory).prop("selected", true); //셀렉트 박스 체크
            $("#prDivCategory").val(data.prDivCategory).prop("selected", true); //셀렉트 박스 체크

            for (let [key, value] of entries) {
              $('input[name='+key+']').val(value);
            }
            modalOn();
        },
        error: function (request, status) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
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
        url: "/products/deleteProduct",
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