$(document).ready(function () {
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

     $.fn.dataTable.ext.search.push(
        function(settings, data, dataIndex){
            var min = Date.parse($('#fromDate').val());
            var max = Date.parse($('#toDate').val());
            var targetDate = Date.parse(data[7]);

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
            "url":"/products/check",
            "type":"GET",
            "dataType":"JSON",
            "autoWidth": false,
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            }
        },
        order : [[1, 'desc']],
        columns: [
            {"data": "prCode"},
            {"data": "prName"},
            {"data": "prPrice"},
            {"data": "prDivCategory"},
            {"data": "prCategory"},
            {"data": "acName"},
            {"data": "prCreateName"},
            {"data": "prRegDate"}
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
            targets : 2,
            'render' : function(data) {
             return '<td>'+comma(data)+'<td>';
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
       if(valueOfElement.innerHTML !="등록 일자" && indexInArray != 0 && valueOfElement.innerHTML !="단가"){
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
    $('#prStandard').remove();
 }
 //정규 표현식을 이용한 자릿수 표현
 function comma(num){
     return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
 }
//거래처를 선택하면 거래처 유형에 맞게 상품 분류를 선택합니다. 셀렉트 박스를 읽기 전용으로 변경시켜 줍니다.
function getAccountCategory(acCode){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    if(acCode.replace(/(\s*)/g, "") == ""){
        alert("거래처를 선택해주세요.");
        return;
    }
    $.ajax({
        url: "/accounts/getAccountCategory",
        type: "POST",
        contentType:'application/json',
        data: JSON.stringify(acCode),
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function (data) {
            $('#prStandard').remove();
            $("#prDivCategory").val(data).prop("selected", true); //셀렉트 박스 체크
            if(data == "제품"){
                 $('#standard').append(`<input type="text" id="prStandard" class="customInput" name="prStandard" placeholder="제품 규격을 입력해주세요.">`);
            }
        },
        error: function (request, status) {
        alert(request.responseText);
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
}

//상품 등록.
function addProduct(){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var formData = new FormData(document.forms.namedItem("myForm"));
    // 초기 등록시에는 code의 값이 없을수밖에 없으니 code에 값이 없으면 배열에 저장하지 않습니다.

    var targetData = {};
    for (var pair of formData.entries()) {
        if(pair[0] == "prCategory" || pair[0] == "prDivCategory"){
            // 공백을 제거 == null || pair["prCategory"] 공백 alert 발생.
           if(pair[1].replace(/\s/g, "") == ''){
           var message ='';
                if(message = pair[0] == 'prCategory' ? message='상품 상세 분류' : message='상품 분류'){
                    alert(message+"를 재대로 입력해주세요.");
                    return;
                }
            }
        }
        if(pair[0] == "prCode"){
            if(pair[1] != null && pair[1] != ''){
                targetData[pair[0]]=pair[1];
            }
        }else{
            targetData[pair[0]]=pair[1];
        }
    }
    var paramData = JSON.stringify(targetData);
    $.ajax({
        url: "/products/addProduct",
        type: "POST",
        contentType:'application/json',
        data: paramData,
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function () {
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
    $('#prStandard').remove();

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
        dataType: "json",
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function (data) {
            var key = Object.keys(data);    //넘겨 받은 데이터의 키 값
            const entries = Object.entries(data);

            $("#acCode").val(data.acCode).prop("selected", true); //셀렉트 박스 체크
            $("#prCategory").val(data.prCategory).prop("selected", true); //셀렉트 박스 체크
            $("#prDivCategory").val(data.prDivCategory).prop("selected", true); //셀렉트 박스 체크

            if(data.prDivCategory == "제품"){
                $('#standard').append(`<input type="text" id="prStandard" name="prStandard" placeholder="제품 규격을 입력해주세요.">`);
            }

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
        success: function () {
            $('#myTable').DataTable().ajax.reload();
        },
        error: function (request, status) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
}
/*데이터 베이스 엑셀화*/
function excel(){

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var inputs = document.getElementsByName("checker");
    var values = Array.from(inputs).map(function(input) {
      return input.value;
    });

    paramData = {};
    paramData[["product"]]=values;

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
                var fileName = "Product_Info.xlsx"
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