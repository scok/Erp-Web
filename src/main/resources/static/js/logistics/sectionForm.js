$(document).ready(function(){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    $.fn.dataTable.ext.search.push(
        function(settings, data, dataIndex){
            var min = Date.parse($('#fromDate').val());
            var max = Date.parse($('#toDate').val());
            var targetDate = Date.parse(data[8]);

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
        order : [[1, 'desc']],
        columns:[
            {"data":"secCode"},
            {"data":"secCategory"},
            {"data":"secName"},
            {"data":"secTotalCount"},
            {"data":"secMaxCount"},
            {"data":"inventoryLoadingRate"},
            {"data":"secAddress"},
            {"data":"secCreateName"},
            {"data":"secRegDate"}
            {"data":"secRegDate"}
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
            },
            {
                targets : 4,
                'render' : function(data) {
                return '<th>'+comma(data)+'<th>';
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
      if(valueOfElement.innerHTML !="등록일자" && indexInArray != 0
      &&valueOfElement.innerHTML !="용적률" && valueOfElement.innerHTML !="최대 적재량" && valueOfElement.innerHTML !="현재 적재량"){
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
// 주소 검색
function addressSh(){
    new daum.Postcode({
       oncomplete: function(data) {
       document.querySelector("#secAddress").value =  data.address
       }
    }).open();
}
//정규 표현식을 이용한 자릿수 표현
function comma(num){
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
// 창고 등록
function addSection(){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var formData = new FormData(document.forms.namedItem("myForm"));

    if(Math.sign($('#secMaxCount').val()) == -1 || $('#secMaxCount').val() == 0){
        alert("최대 적재 수량을 알 수 없습니다.");
        return ;
    }

    if($('#secCategorySel').val() == null || ($('#secCategorySel').val()).trim('') == ''){
        alert("창고를 선택해주세요.");
        return ;
    }

    var array = {};
    for(var item of formData.entries()){
        array[item[0]]=item[1];
    }

    var paramData = JSON.stringify(array);

    $.ajax({
        url: "/section/addSection",
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
        error: function (request, status, error) {
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
}

// 상품 목록 상단 수정하기 버튼 클릭 시 실행
function update(){
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    var checkList = $('input[name=checker]:checked');

    if(checkList.length > 1 || checkList.length == 0 ){
        alert('수정할 항목은 1개만 선택해주세요.');
        return
    }
    var code = checkList.val();

    var jsonSerializedData = JSON.stringify(code);

     $.ajax({
        url: "/section/updateSection",
        type: "POST",
        contentType:"application/json",
        data: jsonSerializedData,
        dataType: "json",
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function (data) {
            var key = Object.keys(data);    //넘겨 받은 데이터의 키 값
            const entries = Object.entries(data);
            for (let [key, value] of entries) {
              $(`input[name="${key}"]`).val(value);
            }
            modalOn();
            setTimeout(function() { //지연작업 비동기 통신으로 셀렉트 박스를 그리기 때문에 지연작업이 필요함 => 다그리기 전에 호출해버려서 실패함
                //셀렉트 박스 체크
                $("#secCategorySel").val(data.secCategory).prop("selected", true);
            }, 150);
        },
        error: function (request, status) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
}

// 삭제하기(웹에서만 삭제되고 DB에서는 삭제되지 않음)
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

    var jsonSerializedData = JSON.stringify(values);

     $.ajax({
        url: "/section/deleteSection",
        type: "POST",
        contentType:"application/json",
        data: jsonSerializedData,
        beforeSend:function(xhr){
            xhr.setRequestHeader(header,token);
        },
        success: function () {
            $('#myTable').DataTable().ajax.reload();
            modalOff();
        },
        error: function (request, status) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n");
        }
    });
}

