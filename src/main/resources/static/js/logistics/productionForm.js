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
            }
        },
        columns: [
            {"data": "proCode"},
            {"data": "productionLine"},
            {"data": "meName"},
            {"data": "prName"},
            {"data": "count"},
            {"data": "registrationDate"}
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