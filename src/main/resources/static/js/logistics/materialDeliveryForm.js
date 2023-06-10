$(document).ready(function () {
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    $.fn.dataTable.ext.search.push(
        function(settings, data, dataIndex){
            var min = Date.parse($('#fromDate').val());
            var max = Date.parse($('#toDate').val());
            var targetDate = Date.parse(data[1]);

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
            "url":"/materialDelivery/check",
            "type":"GET",
            "dataType":"JSON",
            "autoWidth": false,
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            }
        },
        order : [[0, 'desc']],
        columns: [
            {"data": "maDeliveryId"},
            {"data": "maDeliveryDate"},
            {"data": "prName"},
            {"data": "maDeliveryCount"},
            {"data": "secName"},
            {"data": "stackAreaCategory"},
            {"data": "productionLine"},
            {"data": "createName"}
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
            return '<td><span class="maId">'+data+'</span></td>';
            }
        },
        {
            targets : 3,
            'render' : function(data) {
            return '<td>'+comma(data)+'</td>';
            }
        }
        ]
   });

    /* Column별 검색기능 추가 */
    $('#myTable_filter').prepend('<select id="customSelect"></select>');
    $('#myTable > thead > tr').children().each(function (indexInArray, valueOfElement) {
       if(valueOfElement.innerHTML !="불출 일자" && valueOfElement.innerHTML !="불출 수량" && valueOfElement.innerHTML !="불출 코드"){
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
    /* 날짜검색 이벤트 리바인딩 */
    $('#myTable_filter').prepend('<input type="date" id="toDate" placeholder="yyyy-MM-dd">');
    $('#myTable_filter').prepend('<input type="date" id="fromDate" placeholder="yyyy-MM-dd"> ~');
    $('#toDate, #fromDate').unbind().bind("change",function(){
       table.draw();
    })

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
//정규 표현식을 이용한 자릿수 표현
function comma(num){
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
//창고의 정보를 가져옵니다.
function getSectionInfo(value){

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

     $.ajax({
        url: "/logistics/inventory/sectionInfo",
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

    var inId = $("#inId option:selected").val();
    var productionLine = $("#productionLine option:selected").val();
    var maDeliveryCount = $("#maDeliveryCount").val();

    if(inId == null || inId == ""){
        alert("불출 상품을 선택해주세요.");
        return;
    }

    if(productionLine == null || productionLine == ""){
        alert("작업 라인을 선택해주세요.");
        return;
    }

    var pattern = /^\d+$/;
    if (!pattern.test(maDeliveryCount) || maDeliveryCount<=0) {
      alert("불출 수량을 재대로 입력해주세요.");
      return;
    }

    var formData = new FormData(document.forms.namedItem("myForm"));

    var array = {}
    for(var item of formData.entries()){
        array[item[0]]=item[1];
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
            modalOff();
        },
        error: function (request, status, error) {
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert(request.responseText);
        }
    });
}
/*데이터 베이스 엑셀화*/
function excel(){

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var inputs = document.getElementsByClassName("maId");
    var values = Array.from(inputs).map(function(input) {
      return input.innerText ;
    });
    console.log(values);
    paramData = {};
    paramData[["materialDelivery"]]=values;

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
                var fileName = "MaterialDelivery_Info.xlsx"
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

