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
        order : [[1, 'desc']],
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
                    return '<span id="tableInnerCheckBox"><input type="checkbox" name="checker" value="'+data+'"></span>';
                }
            },
            {
                targets : 5,
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
       if(valueOfElement.innerHTML !="출고일자" && valueOfElement.innerHTML !="입고일자"
       && valueOfElement.innerHTML !="수량" && indexInArray != 0){
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
    $("#accountTable").empty();    //테이블을 비워 줍니다.
    $("#productTable").empty();    //테이블을 비워 줍니다.

    var divElement = document.getElementById("customTable");
      while (divElement.firstChild) {
        divElement.firstChild.remove();
    }
 }

//정규 표현식을 이용한 자릿수 표현
function comma(num){
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
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
        url: "/logistics/warehousingInAndOut/Detail",
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

            var tableHead = '<thead><tr><th>거래처 명</th><th>주소</th><th>대표자 명</th><th>구매처 번호</th><th>홈페이지</th></tr></thead>';
            $('#accountTable').append(tableHead);

            var tableBody = '<tbody><tr><td>'+item.accountAddDto.acName+'</td>';
            tableBody += '<td>'+item.accountAddDto.acAddress+'</td>';
            tableBody += '<td>'+item.accountAddDto.acCeo+'</td>';
            tableBody += '<td>'+item.accountAddDto.acNumber+'</td>';
            tableBody += '<td><a href="https://'+item.accountAddDto.acHomepage+'/">'+item.accountAddDto.acHomepage+'</a></td>';
            tableBody += '</tr></tbody>';
            $('#accountTable').append(tableBody);

            /*상품 정보 그리기*/
            tableHead = '<thead><tr><th>상품 명</th><th>단가</th><th>분류</th><th>상세 분류</th>';
            tableBody = '<tbody><tr><td>'+item.productAddDto.prName+'</td>';
            tableBody += '<td>'+comma(item.productAddDto.prPrice)+'</td>';
            tableBody += '<td>'+item.productAddDto.prDivCategory+'</td>';
            tableBody += '<td>'+item.productAddDto.prCategory+'</td>';

            if(item.productAddDto.prStandard != null){
                tableHead += '<th>규격</th></tr></thead>';
                tableBody += '<td>'+item.productAddDto.prStandard+'</td></tr></tbody>';
            }else{
                tableHead += '</tr></thead>';
                tableBody += '</tr></tbody>';
            }
            $('#productTable').append(tableHead);
            $('#productTable').append(tableBody);

            if(item.orderSheetDetailAddmDto != null){
                $('#customTable').append(`<p>주문서 정보</p><table id=orderSheetTable></table>`);

                tableHead = '<thead><tr><th>규격</th><th>주문 단가</th><th>수량</th><th>공급 가액</th><th>세액</th>></tr></thead>';
                tableBody = '<tbody><tr><td>'+item.orderSheetDetailAddmDto.osStandard+'</td>';
                tableBody += '<td>'+comma(item.orderSheetDetailAddmDto.osPrice)+'</td>';
                tableBody += '<td>'+comma(item.orderSheetDetailAddmDto.osQuantity)+'</td>';
                tableBody += '<td>'+comma(item.orderSheetDetailAddmDto.osSupplyValue)+'</td>';
                tableBody += '<td>'+comma(item.orderSheetDetailAddmDto.osTaxAmount)+'</td>';
                tableBody += '</tr></tbody>';

                $('#orderSheetTable').append(tableHead);
                $('#orderSheetTable').append(tableBody);

            }else if(item.productionFormDto != null){
                $('#customTable').append(`<p>실적 정보</p><table id=productionTable></table>`);

                tableHead = '<thead><tr><th>조립 라인</th><th>등록자 명</th><th>제품 명</th><th>수량</th><th>창고 명</th><th>구역 명</th></tr></thead>';
                tableBody = '<tbody><tr><td>'+item.productionFormDto.productionLine+'</td>';
                tableBody += '<td>'+item.productionFormDto.meName+'</td>';
                tableBody += '<td>'+item.productionFormDto.prName+'</td>';
                tableBody += '<td>'+comma(item.productionFormDto.count)+'</td>';
                tableBody += '<td>'+item.productionFormDto.secName+'</td>';
                tableBody += '<td>'+item.productionFormDto.SACategory+'</td></tr></tbody>';

                $('#productionTable').append(tableHead);
                $('#productionTable').append(tableBody);
            }else if(item.materialDeliveryFormDto != null){
                $('#customTable').append(`<p>자재 불출 정보</p><table id=materialDeliveryTable></table>`);
                tableHead = '<thead><tr><th>불출 라인</th><th>등록자 명</th><th>자재 명</th><th>수량</th><th>창고 명</th><th>구역 명</th></tr></thead>';
                tableBody = '<tbody><tr><td>'+item.materialDeliveryFormDto.productionLine+'</td>';
                tableBody += '<td>'+item.materialDeliveryFormDto.createName+'</td>';
                tableBody += '<td>'+item.materialDeliveryFormDto.prName+'</td>';
                tableBody += '<td>'+comma(item.materialDeliveryFormDto.maDeliveryCount)+'</td>';
                tableBody += '<td>'+item.materialDeliveryFormDto.secName+'</td>';
                tableBody += '<td>'+item.materialDeliveryFormDto.stackAreaCategory+'</td></tr></tbody>';

                $('#materialDeliveryTable').append(tableHead);
                $('#materialDeliveryTable').append(tableBody);
            }
        },
        error: function (request, status) {
            alert(request.responseText);
        }
    })
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
    paramData[["warehousing"]]=values;

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
                var fileName = "Warehousing_Info.xlsx"
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
