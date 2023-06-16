$(document).ready(function () {
    var link =  document.location.href; //현재 접속한 페이지의 url을 가져옵니다.
    link = link.split("/");
    var myUrl ="";
    if(link[3].substring(0,3) == "buy"){  //현재 접속한 url을 가공하여 구매 ,판매인지 구분합니다.
        myUrl = "/buyEstimates/check";
    }else{
        myUrl = "/sellerEstimates/check";
    }
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    $.fn.dataTable.ext.search.push(
        function(settings, data, dataIndex){
            var min = Date.parse($('#fromDate').val());
            var max = Date.parse($('#toDate').val());
            var targetDate = Date.parse(data[9]);

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
            "url": myUrl,
            "type":"GET",
            "dataType":"JSON",
            "autoWidth": false,
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            }
        },
        order : [[1, 'desc']],
        columns: [
            {"data": "esCode"},
            {"data": "acCategory"},
            {"data": "prName"},
            {"data": "esTotalPrice"},
            {"data": "acName"},
            {"data": "acCeo"},
            {"data": "acHomePage"},
            {"data": "divisionStatus"},
            {"data": "esCreateName"},
            {"data": "esRegDate"}
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
       if(valueOfElement.innerHTML !="등록일자" && indexInArray != 0 && valueOfElement.innerHTML !="총금액"){
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
        $('input[name="checker"]').prop('checked',false);
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
    // 테이블 셀 더블 클릭 이벤트 핸들러
    $(document).on("dblclick", ".editable", function() {
        // 더블 클릭한 셀의 내용을 가져옴
        var value=$(this).text();
        // 새로운 input 엘리먼트를 만들어서 테이블 셀에 추가
        var input="<input type='text' class='input-data' value='"+value+"' class='form-control'>";
        $(this).html(input);
        // 셀에서 editable 클래스를 삭제
        $(this).removeClass("editable");

    });
       //상품을 select 박스를 선택하면 동적으로 상품 정보를 가져옵니다.
        $("#prCode").change(function (){
            var code = $(this).val();
            var bool = document.getElementById(code);
            if(bool != null){
                alert("이미 추가된 항목입니다.");
                return;
            }
            if(code == null || code.replace(/\s/g, "") == ""){
                alert("선택할 수 없는 항목입니다.");
                return;
            }

            $.ajax({
            url: "/products/productInfo",
            type: "POST",
            contentType:"application/json",
            data: code,
            dataType: "json",
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            },
            success: function (data) {
                var tableTd = '<tr id='+code+'> <td>' + data.acCategory + '</td>';
                tableTd += '<td style="display: none;" class="prCode" id="'+code+'-prCode" value='+code+' >' + code + '</td>';
                tableTd += '<td id='+code+'-prName value='+data["prName"]+'>' + data["prName"] + '</td>';
                tableTd += '<td id='+code+'-prPrice value='+data["prPrice"]+'>' + comma(data["prPrice"]) + '</td>';
                tableTd += '<td class="editable" id= '+code+'-esStandard></td>';
                tableTd += '<td class="editable count" id="' + code + '-esQuantity"></td>';
                tableTd += '<td class="SupplyValue" id='+code+'-esSupplyValue></td>';
                tableTd += '<td class="TaxAmount" id='+code+'-esTaxAmount></td>';
                tableTd += '<td><button type="button" id="deleteBtn" onclick="deleteRow(\''+code+'\')"><ion-icon name="trash-outline"></ion-icon></button></td> </tr>';

                $('#productTable tbody').append(tableTd);
            },
            error: function (request, status) {
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
            }
        });
    });
        // input 엘리먼트 keypress 이벤트 핸들러
        $(document).on("keypress", ".input-data", function(e) {
            // 엔터키 입력 시
            var key=e.which;
            if(key==13) {
                // input 엘리먼트에서 수정된 값을 가져옴
                var value=$(this).val();
                // input 엘리먼트의 부모 td 엘리먼트를 찾아서 변수에 할당
                var td=$(this).parent("td");
                // input 엘리먼트를 삭제
                $(this).remove();
                // 수정된 값을 td 엘리먼트에 추가
                td.html(value);
                td.attr("value",value);
                // td 엘리먼트에 editable 클래스 추가
                td.addClass("editable");

                //공급 가액 및 세액을 구하는 부분
                var id = td.attr("id"); // id 속성 값 가져오기
                var array = id.split("-");    //-기준으로 문자열을 쪼갭니다.
                var code = td.parent().attr("id"); // pr코드를 추출합니다.
                var lastCharacter = array[array.length - 1]; // 배열의 마지막 요소를 추출

                if(lastCharacter == "esQuantity"){ //수량값을 수정 완료 했을때.
                    //수량을 입력시 정규 표현식으로 숫자 인지 아닌지 구분합니다.
                    var regexp = /^[0-9]*$/
                    if( !regexp.test(value) || value.replaceAll("\\s+", "") == "") {
                    	alert("숫자만 입력하세요");
                    	td.html('');
                    	td.attr("value",'');
                    	return;
                    }

                    var price = $('#' + code + "-prPrice").attr('value');   //단가를 저장할 변수
                    var esSupplyValue = price * value;  //공급가액
                    var esTaxAmount = esSupplyValue *0.1;   //세액

                    if(!esSupplyValue % 1 === 0){   //실수일경우 반올림을 합니다.
                        esTaxAmount = Math.round(esTaxAmount); //세엑은 소숫점이 나올수 있으니 반올림 합니다.
                    }

                    //사용자가 웹 페이지에서도 확인 가능하게 value 와 html에 값을 넣습니다
                    $("#"+code+"-esSupplyValue").attr("value",esSupplyValue);
                    $("#"+code+"-esSupplyValue").html(comma(esSupplyValue));

                    $("#"+code+"-esTaxAmount").attr("value",esTaxAmount);
                    $("#"+code+"-esTaxAmount").html(comma(esTaxAmount));

                    changeTotalPrice();
                }
            }
    });
});
//거래처를 선택하면 동적으로 거래처와 매핑된 상품을 가져옵니다.
function AcCodeChange(value) {
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    if(value.replace(/\s/g, "") == '' || value==null){
        alert("거래처를 다시 선택해주세요.");
        return;
    }
    //거래처가 선택 되면 테이블을 한번 비워야 합니다.
    $("#tableBody").empty();    //거래처를 선택하면 사용자가 바꾸길 원하지 않는 이상 못바꾸게 수정 필요.

    var code = value;
     $.ajax({
            url: "/products/productList",
            type: "POST",
            contentType:"application/json",
            data: code,
            dataType: "json",
            beforeSend:function(xhr){
                xhr.setRequestHeader(header,token);
            },
            success: function (data) {
                //selectBox 불러오기
                var selectBox = document.getElementById('prCode');
                // 기존 옵션들을 모두 제거
                selectBox.innerHTML = '<option value="">==상품 선택==</option>';

                // 데이터를 기반으로 새로운 옵션들을 추가
                for (var item of data) {
                    var option = document.createElement('option');
                    option.value = item["prCode"];
                    option.textContent = item["prName"];
                    selectBox.appendChild(option);
                }
            },
            error: function (request, status) {
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
            }
     });

}

//총 금액 수정을 담당하는 함수 입니다.
function changeTotalPrice(){

    var total = 0;
    //공급 가액, 세엑, 수량을 불러옵니다.
    var SupplyValueElements = document.getElementsByClassName("SupplyValue");
    var TaxAmountElements = document.getElementsByClassName("TaxAmount");

    for (var i = 0; i < SupplyValueElements.length; i++) {
        var SupplyValue = parseInt(SupplyValueElements[i].getAttribute('value'));
        var TaxAmount = parseInt(TaxAmountElements[i].getAttribute('value'));
        total += SupplyValue + TaxAmount;
    }

    $("#esTotalPrice").attr("value",total);
    $("#esTotalPrice").html("Total: &#8361; "+ comma(total)); // 총 금액을 특정 요소에 반영
}
function deleteRow(code) { //테이블 한 행을 지웁니다. tr의 id를 찾고 그 행을 지워버립니다.

    var row = document.getElementById(code);
    row.remove();

    changeTotalPrice();
    // 삭제 로직 추가
    console.log("삭제 - 행 ID: " + code);
}

function modalOn() {
    modalOff();
    createButton();
    $("#prDelete").css("display", "");//삭제 기능을 보이게 해줍니다.
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
    var prSelectElement = document.getElementById("prCode");
    if(prSelectElement.disabled = false ){// 읽기 일때 전용으로 설정
        prSelectElement.disabled = true; // 설정 초기화 전용으로 설정
    }

    $("#esTotalPrice").attr("value",0);
    $("#esTotalPrice").html(0); // 총 금액을 특정 요소에 반영

    var divElement = document.getElementById("DivStatus");
      while (divElement.firstChild) {
        divElement.firstChild.remove();
    }
    var selectBox = document.getElementById('prCode');
    // 기존 옵션들을 모두 제거
    selectBox.innerHTML = '<option value="">==상품 선택==</option>';
 }

//정규 표현식을 이용한 자릿수 표현
function comma(num){
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

//동적 버튼 보이기
function createButton(){
    $("#saveBtn").css("display", "");//삭제 기능을 안보이게 해줍니다.
}
//동적 버튼 숨기기.
function deleteButton(){
    $("#saveBtn").css("display", "none");//삭제 기능을 안보이게 해줍니다.
}

//라디오 버튼 클릭시
function radioClick(value){
    var pElement = document.getElementById("companion");
    var inputElement = document.getElementById("esComment");
    if(pElement != null || inputElement != null){
        pElement.remove();
        inputElement.remove();
    }
    if(value == '반려'){  //반려시 코멘트를 작성해야합니다.
        $('#DivStatus')
        .append(`<p id="companion" name="companion">반려 사유</p>`)
        .append(`<input class="customInput" type="textarea" id="esComment" name="esComment" style="width:50%">`)
    }
    if(value == '승인'){  //승인시 코멘트를 작성란을 삭제 합니다..
        var companionP = document.getElementById("companion");
        var companionInput = document.getElementById("esComment");
        if(companionP != null && companionInput != null){
            companionP.remove();
            companionInput.remove();
        }
    }
}

//데이터 초기 등록 및 수정
function addEstimate(){
    var selectElement = document.getElementById("acCode");
    if(selectElement.disabled = false ){// 읽기 전용이 아닐때
        selectElement.disabled = true; // 다시 읽기 전용으로 설정
    }

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    // 테이블 태그 외 값을 가져옵니다.
    var formData = new FormData(document.forms.namedItem("myForm"));
    var targetData = {};
    var esData = {};
    for (var pair of formData.entries()) {
        if(pair[0] == "acCode" || pair[0] == "prCode"){
            // 공백을 제거 == null || accountCode,productCode 공백 alert 발생.
           if(pair[1].replace(/\s/g, "") == ''){
           var message ='';
                if(message = pair[0] == 'acCode' ? message='거래처를' : message='자재'){
                    alert(message+"를 선택해 주세요.");
                    return;
                }
            }
        }
        if(pair[0] != "prCode"){
            if(pair[0] == "code"){
                if(pair[1] != null && pair[1] != ''){
                    targetData[pair[0]]=pair[1];
                }
            }else{
                targetData[pair[0]]=pair[1];
            }
        }
    }
    targetData["esTotalPrice"]=$("#esTotalPrice").attr('value');
    esData["esInfo"]=targetData;

    //테이블 값을 가져오기 위한 코드 제이 쿼리에서는 FORM 태그와 TABLE 태그를 함께 가져오는 함수는 없다.
    //테이블 태그 값 가져오는 법
    var table = document.getElementById("productTable"); // 테이블의 ID를 지정
    var trCells = table.getElementsByTagName("tr");
    var result = {};
    var cnt = 1;

    for (var i = 0; i < trCells.length; i++) {
        var tableData = {};
        if(i != 0){ //첫번째 tr 태그는 thead 영역이기 때문에 스킵합니다.
            var trElement = trCells[i];
            var cells = trElement.getElementsByTagName("td");
            // 각 <td> 태그에 접근하여 원하는 작업을 수행합니다.
            var cellName ="";

            for (var j = 0; j < cells.length; j++) {
                var cell = cells[j];
                var cellId = cell.getAttribute("id");
                var cellValue = cell.getAttribute("value");

                if(cellId != null){ //기능 컬럼의 삭제 버튼이 id 값이 null이기 때문에 컬럼 오 저장 방지를 위한 조건문
                    cellName = cellId.substring(15) ;

                    if(cellName == "esStandard"){//value가 공백은 인식을 못하는 경우가 발생하여 값을 대체합니다.
                        var tdElement = document.getElementById(cellId);  // cellId에 실제 <td> 요소의 id를 넣습니다.
                        cellValue = tdElement.textContent;  // <td> 요소의 텍스트 콘텐츠를 가져옵니다.
                    }
                    if(cellName == "esStandard" || cellName == "esQuantity"){
                        if(cellValue == null || cellValue.replaceAll("\\s+", "") == ""){//값이 없는 경우.
                           alert("수량 과 규격을 입력해주세요.");
                           return;
                        }
                    }
                    tableData[cellName] = cellValue;
                }
                if(j ==(cells.length)-1){
                    esData["esdInfo"+[cnt]] = tableData;
                    cnt++;
                 }
            }
        }
    }
    $.ajax({
        url: "/estimates/addEstimate",
        type: "POST",
        contentType:'application/json',
        data: JSON.stringify(esData),
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function () {
            alert("저장 성공");
            //라디오 버튼이 존재하여 데이터 테이블을 분기 처리 하는 웹 페이지는 테이블 리로드시 문제 발생하여 함수 호출로 대신 사용
            var selectRadio = $('.tabs2').find('input[type="radio"]:checked').val();    //선택되어 있는 라디오 버튼 값.
            estimateClick(selectRadio);
            modalOff();
        },
        error: function (request, status) {
            alert(request.responseText);
            console.log("code:"+request.status+"\n"+"견적서 message:"+request.responseText+"\n");
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
        url: "/estimates/updateEstimate",
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

            AcCodeChange(item.acCode); //이벤트가 알아서 감지 못하기 때문에 함수 호출.

            $("#acCode").prop("disabled", true);    //거래처 셀렉트 박스를 읽기 전용으로 바꿔 줍니다.
            var editable = ""; //테이블 수정 기능을 담당
            for (var esdItem of item.estimateDetail){
                console.log(esdItem);
                var tableTd = '<tr id='+esdItem.product.prCode+'> <td>' + item.acCategory + '</td>';
                tableTd += '<td style="display: none;" id="'+esdItem.product.prCode+'-esdId" value='+esdItem.esdId+' >' + esdItem.esdId + '</td>';
                tableTd += '<td style="display: none;" class="prCode" id="'+esdItem.product.prCode+'-prCode" value='+esdItem.product.prCode+' >' + esdItem.product.prCode + '</td>';
                tableTd += '<td id='+esdItem.product.prCode+'-prName value='+esdItem.product.prName+'>' + esdItem.product.prName + '</td>';
                tableTd += '<td id='+esdItem.product.prCode+'-prPrice value='+esdItem.esPrice+'>' + comma(esdItem.esPrice) + '</td>';
                if(item.divisionStatus == '승인대기'){
                    tableTd += '<td class="editable" id= '+esdItem.product.prCode+'-esStandard>'+esdItem.esStandard+'</td>';
                    tableTd += '<td class="editable count" id="' + esdItem.product.prCode + '-esQuantity" value='+esdItem.esQuantity+'>'+esdItem.esQuantity+'</td>';
                }else{
                    tableTd += '<td id= '+esdItem.product.prCode+'-esStandard>'+esdItem.esStandard+'</td>';
                    tableTd += '<td class="count" id="' + esdItem.product.prCode + '-esQuantity" value='+esdItem.esQuantity+'>'+esdItem.esQuantity+'</td>';
                }
                tableTd += '<td class="SupplyValue" id='+esdItem.product.prCode+'-esSupplyValue value='+esdItem.esSupplyValue+'>'+comma(esdItem.esSupplyValue)+'</td>';
                tableTd += '<td class="TaxAmount" id='+esdItem.product.prCode+'-esTaxAmount value='+esdItem.esTaxAmount+'>'+comma(esdItem.esTaxAmount)+'</td>';
                if(item.divisionStatus == '승인대기'){
                    $("#prDelete").css("display", "");//삭제 기능을 보이게 해줍니다.
                    tableTd += '<td><button type="button" id="deleteBtn" onclick="deleteRow(\''+esdItem.product.prCode+'\')"><ion-icon name="trash-outline"></ion-icon></button></td> </tr>';
                }else{
                    $("#prDelete").css("display", "none");//삭제 기능을 안보이게 해줍니다.
                }
                $('#productTable tbody').append(tableTd);
            }

            $("#esTotalPrice").attr("value",item.esTotalPrice);
            $("#esTotalPrice").html("Total: &#8361; "+ comma(item.esTotalPrice)); // 총 금액을 특정 요소에 반영

            setTimeout(function() { //지연작업 비동기 통신으로 셀렉트 박스를 그리기 때문에 지연작업이 필요함 => 다그리기 전에 호출해버려서 실패함
                $("#prCode").val(data["data"].estimateDetail[0].product.prCode).prop("selected", true); //셀렉트 박스 체크
            }, 500);

            if(item.divisionStatus == '승인대기'){
                var radios = ['승인', '반려'];
                $('#DivStatus')
                .append(`<input type="hidden" id="esCode" name="esCode" value=${item.esCode}>`)
                .append(`<input type="radio" id="divisionStatus" name="divisionStatus" value=${radios[0]} onclick="radioClick(value)">`)
                .append(`<label for="divisionStatus">${radios[0]}</label></div>`)
                .append(`<input type="radio" id="divisionStatus2" name="divisionStatus" value=${radios[1]} onclick="radioClick(value)">`)
                .append(`<label for="divisionStatus2">${radios[1]}</label></div>`)
                .append(`<br>`);
            }
            if(item.divisionStatus != '승인대기'){
                $("#prCode").prop("disabled", true);    //상품 셀렉트 박스를 읽기 전용으로 바꿔 줍니다.
                deleteButton();
            }
            if(item.divisionStatus == '반려'){
                $('#DivStatus')
                .append(`<p>반려 사유</p>`)
                .append(`<p type="textarea" id="esComment" name="esComment">${item.esComment}</p>`)
                .append(`<br/>`);
            }
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
        url: "/estimates/deleteEstimate",
        type: "POST",
        contentType:"application/json",
        data: paramData,
        dataType: "json",
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function (data) {
            alert("success");
            var selectRadio = $('.tabs2').find('input[type="radio"]:checked').val();    //선택되어 있는 라디오 버튼 값.
            estimateClick(selectRadio);
            modalOff();
        },
        error: function (request, status) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
}
//견적서 상태별 값을 볼 수 있습니다.
function estimateClick(values){

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var category = $('#acCategory').attr('value');

    var table = $("#myTable").DataTable();
    table.destroy();
    paramData = {};

    paramData[["acCategory"]] = category;
    paramData[["filter"]] = values;

    $.fn.dataTable.ext.search.push(
        function(settings, data, dataIndex){
            var min = Date.parse($('#fromDate').val());
            var max = Date.parse($('#toDate').val());
            var targetDate = Date.parse(data[9]);

            if( (isNaN(min) && isNaN(max) ) ||
                (isNaN(min) && targetDate <= max )||
                ( min <= targetDate && isNaN(max) ) ||
                ( targetDate >= min && targetDate <= max) ){
                    return true;
            }
            return false;
        }
    )

    $.ajax({
        url: "/estimates/click",
        type: "POST",
        contentType:"application/json",
        data: JSON.stringify(paramData),
        dataType: "json",
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function (result) {
            var table = $('#myTable').DataTable({
                data:result.data,
                dataSrc:"",
                order : [[1, 'desc']],
                columns: [
                    {"data": "esCode"},
                    {"data": "acCategory"},
                    {"data": "prName"},
                    {"data": "esTotalPrice"},
                    {"data": "acName"},
                    {"data": "acCeo"},
                    {"data": "acHomePage"},
                    {"data": "divisionStatus"},
                    {"data": "esCreateName"},
                    {"data": "esRegDate"}
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
                        'render' : function(data) {
                        return '<th>'+comma(data)+'<th>';
                        }
                    }
                ]
            });
            /* Column별 검색기능 추가 */
            $('#myTable_filter').prepend('<select id="customSelect"></select>');
            $('#myTable > thead > tr').children().each(function (indexInArray, valueOfElement) {
               if(valueOfElement.innerHTML !="등록일자" && indexInArray != 0 && valueOfElement.innerHTML !="총금액"){
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
        },
        error: function (request, status) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
}


