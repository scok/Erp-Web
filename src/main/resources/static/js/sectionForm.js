$(document).ready(function(){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var table = $('#myTable').DataTable({
        ajax: {
            "url":"/section/check",
            "type":"GET",
            "dataType":"JSON",
            "autoWidth":false,
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            },
            scrollX: true,
            scrollY: 200,
            ordering: true,
        },
        columns:[
            {"data":"secCode"},
            {"data":"secCategory"},
            {"data":"secName"},
            {"data":"secTotalCount"},
            {"data":"secMaxCount"},
            {"data":"inventoryLoadingRate"},
            {"data":"secAddress"}
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





//// 상품 등록, 수정 후 modal창 내용 리셋
//function resetForm(myForm) {
//  var form = document.forms[myForm];
//  form.reset();
//}


// 창고 등록
function addSection(){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var formData = new FormData(document.forms.namedItem("myForm"));

    var checkKey = "code";
    var array = {}
    for(var item of formData.entries()){
        if(item[0] == checkKey){
            if(item[1] != null && item[1] != ''){
                array[item[0]]=item[1];
            }
        }else{
            array[item[0]]=item[1];
        }
    }
    var paramData = JSON.stringify(array);
    alert("저장하기 클릭 시 전송되는 데이터" + paramData);

        $.ajax({
            url: "/section/addSection",
            type: "POST",
            contentType:"application/json",
            data: paramData,
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            },
            success: function (data) {
                alert("success");
                modalOff()
            },
            error: function (request, status, error) {
                console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            }
        });
}


//    });
//});


//// 상품 목록 상단 수정하기 버튼 클릭 시 실행
//function update(){
//    var token = $('meta[name="_csrf"]').attr('content');
//    var header = $('meta[name="_csrf_header"]').attr('content');
//
//    var checkList = $('input[name=checker]:checked');
//    alert('수정할 데이터 개수 :'+checkList.length);
//
//    if(checkList.length > 1 || checkList.length == 0 ){
//        alert('데이터 수정은 1개씩 가능합니다.');
//        return
//    }
//    var code = checkList.val();
//
//    var jsonSerializedData = JSON.stringify(code);
//    alert(jsonSerializedData);
//
//     $.ajax({
//        url: "/products/update",
//        type: "POST",
//        contentType:"application/json",
//        data: jsonSerializedData,
//        dataType: "json",
//        beforeSend:function(xhr){
//            xhr.setRequestHeader(header,token);
//        },
//        success: function (data) {
//            alert("success");
//
//            var key = Object.keys(data);    //넘겨 받은 데이터의 키 값
//            const entries = Object.entries(data);
//
//            //셀렉트 박스 체크
//            $("#acCodeSel").val(data.acCode).prop("selected", true);
//            $("#prCategorySel").val(data.prCategory).prop("selected", true);
//            $("#prDivCategorySel").val(data.prDivCategory).prop("selected", true);
//
//
//            for (let [key, value] of entries) {
//              console.log(`${key}: ${value}`);
//              $(`input[name="${key}"]`).val(value);
//            }
//            modalOn();
//
//                    },
//        error: function (request, status) {
//            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
//        }
//    });
//}
//
//
//
//// 삭제하기(웹에서만 삭제되고 DB에서는 삭제되지 않음)
//function deletePageN(){
//    var token = $('meta[name="_csrf"]').attr('content');
//    var header = $('meta[name="_csrf_header"]').attr('content');
//
//    var checkList = $('input[name=checker]:checked');
//    alert('삭제할 데이터 개수 :'+checkList.length);
//
//    if(checkList.length == 0 ){
//        alert('1개 이상 선택해주세요.');
//        return
//    }
//
//    var values = [];
//    checkList.each(function() {
//      values.push($(this).val());
//    });
//
//    var jsonSerializedData = JSON.stringify(values);
//    alert(jsonSerializedData);
//
//     $.ajax({
//        url: "/products/delete",
//        type: "POST",
//        contentType:"application/json",
//        data: jsonSerializedData,
//        dataType: "json",
//        beforeSend:function(xhr){
//            xhr.setRequestHeader(header,token);
//        },
//        success: function (data) {
//            alert("success");
//            $('#myTable').DataTable().ajax.reload();
//        },
//        error: function (request, status) {
//            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
//        }
//    });
//}























/* FormData 타입
$(function(){
    $('#submit').on("click",function () {

        var token = $('meta[name="_csrf"]').attr('content');
        var header = $('meta[name="_csrf_header"]').attr('content');

        var formData = new FormData(document.forms.namedItem("test"));

        $.ajax({
            url: "/products/addProduct",
            type: "POST",
            data: formData,
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            },
            processData: false,
            contentType: false,
            success: function (data) {
                alert("success");
                $('#myTable').DataTable().ajax.reload();
            },
            error: function (request, status, error) {
                console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            }
        });
    });
});
*/

 /*   var table = $('#myTable').DataTable({
        ajax: {
            'url':'MOCK_DATA.json',
            //'type': 'POST',
            'dataSrc':''
        },
        responsive: true,
        orderMulti: true,
        order : [[1, 'desc']],
        columns: [
            {"data": "id"},
            {"data": "first_name"},
            {"data": "last_name"},
            {"data": "email"},
            {"data": "gender"},
            {"data": "date"},
            {"data": "ip_address",
                "render": function(data, type, row){
                    *//*
                     * 다른 column의 값을 다루고 싶을 땐
                     * row['COLUMN명'] 으로 꺼내쓸 수 있다.
                     *//*
                    if(type=='display'){
                        data = '<a href="'+ data + '">' + data + '</a>';
                    }
                    return data;
            }},
            {"data":"money"}
        ],
        "language": {
            "emptyTable": "데이터가 없어요.",
            "lengthMenu": "페이지당 _MENU_ 개씩 보기",
            "info": "현재 _START_ - _END_ / _TOTAL_건",
            "infoEmpty": "데이터 없음",
            "infoFiltered": "( _MAX_건의 데이터에서 필터링됨 )",
            "search": "에서 검색: ",
            "zeroRecords": "일치하는 데이터가 없어요.",
            "loadingRecords": "로딩중...",
            "processing":     "잠시만 기다려 주세요...",
            "paginate": {
                "next": "다음",
                "previous": "이전"
            }
        }
});*/

/*
function list(){
var token = $('meta[name="_csrf"]').attr('content');
var header = $('meta[name="_csrf_header"]').attr('content');
myurl = '/products/list';
$.ajax({
     url:myurl,
     type:'POST',
     beforeSend:function(xhr){
     xhr.setRequestHeader(header,token);
     },
     dataType:'json',
     cache:false,
     success:function(prData){
         alert('로딩이 완료되었습니다.');
         console.log(data); // 여기서 로그 확인
         var dataList = data.list;
         var table = $("#myTable").dataTable({
            data: data,
            columns: [
                     {data: "null"},
                     {data: "code"},
                     {data: "name"},
                     {data: "price"},
                     {data: "divCategory"},
                     {data: "category"}
                 ],
                 "language": {
                     "emptyTable": "데이터가 없어요.",
                     "lengthMenu": "페이지당 _MENU_ 개씩 보기",
                     "info": "현재 _START_ - _END_ / _TOTAL_건",
                     "infoEmpty": "데이터 없음",
                     "infoFiltered": "( _MAX_건의 데이터에서 필터링됨 )",
                     "search": "에서 검색: ",
                     "zeroRecords": "일치하는 데이터가 없어요.",
                     "loadingRecords": "로딩중...",
                     "processing":     "잠시만 기다려 주세요...",
                     "paginate": {
                         "next": "다음",
                         "previous": "이전"
                     }
                 }
            });
         },
          error:function(xhr,status,error){
              if(xhr.status=='401'){
                  alert('로그인 후 이용해 주세요.');
                  location.href='/members/login';
              }else{
                  alert(xhr.responseText);
              }
          }
     });
}
*/
