$(document).ready(function () {
    var link =  document.location.href; //현재 접속한 페이지의 url을 가져옵니다.
    link = link.split("/");
    var myUrl ="";
    if(link[3] == "buys"){  //현재 접속한 url을 가공하여 구매 ,판매인지 구분합니다.
        myUrl = "/buys/check";
    }else{
        myUrl = "/sellers/check";
    }

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    $.fn.dataTable.ext.search.push(
    function(settings, data, dataIndex){
        var min = Date.parse($('#fromDate').val());
        var max = Date.parse($('#toDate').val());
        var targetDate = Date.parse(data[4]);

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
        order : [[1, 'desc']],
        columns: [
            {"data": "acCode"},
            {"data": "acCategory"},
            {"data": "acCreateName"},
            {"data": "acName"},
            {"data": "acRegDate"}
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
            //0번째 열에 체크 박스 넣기
             targets : 0,
             orderable: false,
             'render' : function(data, type, full, meta) {
                return '<span id="tableInnerCheckBox"><input type="checkbox" name="checker" value="'+data+'"></span>';
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
        if(valueOfElement.innerHTML =="작성자" || valueOfElement.innerHTML =="구매처 명"){
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
   $("#tableUpdate")[0].reset();
}

function addressSh(){
    /*다음 우편번호 검색*/
    new daum.Postcode({
        oncomplete: function(data) {
        document.querySelector("#acAddress").value =  data.address
        }
    }).open();
}
//거래처 등록,수정 함수
function addAccount(){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var formData = new FormData(document.forms.namedItem("tableUpdate"));

    // 초기 등록시에는 code의 값이 없을수밖에 없으니 code에 값이 없으면 배열에 저장하지 않습니다.
    var array = {}
    for(var item of formData.entries()){
        array[item[0]]=item[1];
    }
    var paramData = JSON.stringify(array);

    $.ajax({
        url: "/accounts/addAccount",
        type: "POST",
        contentType:"application/json",
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
        }
    });
};

//데이터 베이스에 1건의 데이터를 가져옵니다.
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
        url: "/accounts/updateAccount",
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

            $("#accountCategory").val(data.category).prop("selected", true); //셀렉트 박스 체크

            for (let [key, value] of entries) {
              $('input[name='+key+']').val(value);
            }
            modalOn();
        },
        error: function (request, status) {
            alert(request.responseText);
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
        url: "/accounts/deleteAccount",
        type: "POST",
        contentType:"application/json",
        data: paramData,
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function () {
            alert("삭제 완료.");
            $('#myTable').DataTable().ajax.reload();
        },
        error: function (request, status) {
            alert(request.responseText);
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
    paramData[["account"]]=values;

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
                var fileName = "Account_Info.xlsx"
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