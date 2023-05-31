$(document).ready(function(){

    var data_tbody = document.querySelector('.data-table > tbody');
    var data_trs = data_tbody.querySelectorAll('.data');

    var data_key = data_tbody.querySelector('.prKey');
    var data_key_tds = data_key.querySelectorAll('td');

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    // 테이블 값 수정
    var currentCell = null;

    $('td.editable').on('dblclick', function(){

        if(currentCell && currentCell !== this){

            $(currentCell).find('input').remove();
            $(currentCell).find('span').show();
        }

        currentCell = this;
        var text = $(this).find('span').text();
        var input = $('<input type="number" style="width:100%; box-sizing: border-box; text-align:right;">').val(text);

        $(this).find('span').hide();
        $(this).append(input);
        input.focus();
    });

    // input 태그에서 엔터 키를 누르면 값을 데이터 베이스에 반영
    $(document).on('keyup', 'td.editable input', function(event){

        if(event.which === 13){
            var text = $(this).val();
            var cell = $(this).parent();
            var column = cell.index();
            var row = cell.parent().index();

            cell.find('span')[0].innerText = comma(uncomma(text));
            cell.find('input').remove();
            cell.find('span').show();

            var data_name = cell.parent()[0].querySelector('.data_title').getAttribute('aria-label');
            var data_num = parseInt(data_key_tds[column-1].innerText);
            var data_value = parseInt(text);

            var paramData = {};
            paramData.num = data_num;
            paramData.name = data_name;
            paramData.value = data_value;

            var param = JSON.stringify(paramData);

            $.ajax({
                url:'/incomes/dataSave',
                type:'POST',
                contentType:'application/json',
                data:param,
                beforeSend:function(xhr){
                    xhr.setRequestHeader(header, token);
                },
                dataType:'json',
                cache:false,
                success:function(data){
                    console.log('데이터 업데이트 성공');
                    $("#example").DataTable().ajax.reload();
                },
                error:function(xhr, status, error){
                    if(xhr.status == '401'){
                        alert('로그인 후 이용 가능합니다.');
                    }else{

                    }
                }
            });

            $(this).blur();
        }
    });

    function comma(value){
        value = String(value);
        return value.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1, ');
    }

    function uncomma(value){
        value = String(value);
        return value.replace(/[^\d]+/g, '');
    }
});
