$(document).ready(function () {
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var table = $('#myTable').DataTable({
        ajax: {
            "url":"/materialDelivery/check",
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
            {"data": "maDeliveryDate"},
            {"data": "prName"},
            {"data": "maDeliveryCount"},
            {"data": "secName"},
            {"data": "stackAreaCategory"},
            {"data": "productionLine"},
            {"data": "createBy"}
        ]
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

//창고의 정보를 가져옵니다.
function getSectionInfo(value){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

     $.ajax({
        url: "/logistics/sectionInfo",
        type: "POST",
        contentType:"application/json",
        data: JSON.stringify(value),
        dataType: "json",
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function (data) {

            //selectBox 불러오기
            var selectBox = document.getElementById('secCode');
            // 기존 옵션들을 모두 제거
            selectBox.innerHTML = "";
            // 데이터를 기반으로 새로운 옵션들을 추가
            var option = document.createElement('option');
            option.value = data["secCode"];
            option.textContent = data["secName"];
            selectBox.appendChild(option);

            //selectBox 불러오기
            selectBox = document.getElementById('stackAreaCategory');
            // 기존 옵션들을 모두 제거
            selectBox.innerHTML = '';
            // 데이터를 기반으로 새로운 옵션들을 추가
            var option02 = document.createElement('option');
            option02.value = data["SACategory"];
            option02.textContent = data["SACategory"];
            selectBox.appendChild(option02);
        },
        error: function (request, status, error) {
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
     });
}

//등록하기
function addMaDelivery(){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var formData = new FormData(document.forms.namedItem("myForm"));

    var array = {}
    for(var item of formData.entries()){
        if(item[1] != null && item[1] != ''){
            array[item[0]]=item[1];
        }else{
            return;
            alert("재대로 선택해주세요.");
        }
    }
    var paramData = JSON.stringify(array);
        $.ajax({
            url: "/materialDelivery/addMaterialDelivery",
            type: "POST",
            contentType:"application/json",
            data: paramData,
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            },
            success: function (data) {
                alert("success");
                $('#myTable').DataTable().ajax.reload();
                modalOff()
            },
            error: function (request, status, error) {
                console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
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
        url: "/materialDelivery/deleteMaDelivery",
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