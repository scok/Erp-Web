import {table} from './datatables.js';

$(document).ready(function(){

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    // 모달 연도 select 태그 추가
    var select = document.getElementById('yearSelect');
    var currentYear = new Date().getFullYear();

    for(var year = currentYear; year >= currentYear - 10; year--){

        var option = document.createElement('option');
        option.value = year;
        option.text = year;
        select.appendChild(option);
    }

    // 모달 창 띄우기
    $('#data_add_btn').on('click', function(){

        document.getElementById('incomeModal').style.display = 'block';
    });

    // 폼 추가
    $('#form_add_btn').on('click', function(){

        let new_year = select.value;

        var year_select = document.getElementById('year_select');

        var option = document.createElement('option');
        option.text = new_year + '년';
        option.value = new_year;

        year_select.add(option);

        sortOptions(year_select);

        var paramData = {};
        paramData.year = new_year;

        var param = JSON.stringify(paramData);

        $.ajax({
            url:'/incomes/dataAdd',
            type:'POST',
            contentType:'application/json',
            data:param,
            beforeSend:function(xhr){
                xhr.setRequestHeader(header, token);
            },
            dataType:'json',
            cache:false,
            success:function(data){
                console.log('데이터 추가 성공');
                table.search(new_year ? '^' + new_year + '$' : '', true, false).draw(); // 새로 생성한 연도에 맞게 화면 갱신
                year_select.value = new_year;
            },
            error:function(xhr, status, error){
                if(xhr.status == '401'){
                    alert('로그인 후 이용 가능합니다.');
                }else{
                    alert(xhr.responseJSON.message);
                }
            }
        });

        closeModal();
    });
});

// 모달 창
function closeModal(){
    document.getElementById('incomeModal').style.display = 'none';
}

// 연도 배열 정렬
function sortOptions(sortSelect){

    var options = Array.from(sortSelect.options);

    options.sort(function(a, b){

        return b.value.localeCompare(a.value);
    });

    options.forEach(function(option){
        sortSelect.appendChild(option);
    });
}