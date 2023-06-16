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
            {"data": "secName"},
            {"data": "SACategory"},
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
        order : [[0, 'desc']],
        columnDefs: [
        {
            targets : 0,
            'render' : function(data) {
            return '<td><span class="proId">'+data+'</span></td>';
            }
        },
        {
             targets : 4,
             'render' : function(data) {
                return '<td>'+comma(data)+'</td>';
             }
        }
        ]
   });

    /* Column별 검색기능 추가 */
    $('#myTable_filter').prepend('<select id="customSelect"></select>');
    $('#myTable > thead > tr').children().each(function (indexInArray, valueOfElement) {
      if(valueOfElement.innerHTML !="실적 코드" && valueOfElement.innerHTML !="수량" && valueOfElement.innerHTML !="등록일자"){
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
//실적 등록.
function addProduction(){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var count = $("#count").val();
    var pattern = /^\d+$/;
    if (!pattern.test(count) || count<=0) {
      alert("수량을 재대로 입력해주세요.");
      return;
    }

    var formData = new FormData(document.forms.namedItem("myForm"));
    var targetData = {};
    for (var pair of formData.entries()) {
        targetData[pair[0]]=pair[1];
    }
    var paramData = JSON.stringify(targetData);

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
/*데이터 베이스 엑셀화*/
function excel(){

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var inputs = document.getElementsByClassName("proId");
    var values = Array.from(inputs).map(function(input) {
      return input.innerText ;
    });
    console.log(values);
    paramData = {};
    paramData[["production"]]=values;

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
                var fileName = "Production_Info.xlsx"
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