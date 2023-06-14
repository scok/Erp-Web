import {table} from './datatables.js';

$(document).ready(function(){

    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');

    // 모달 연도 select 태그 추가
    var currentYear = new Date().getFullYear();

    // 모달 창 띄우기
    $('#data_add_btn').on('click', function(){

        document.getElementById('financialModal').style.display='flex';
        setTimeout(function(){
            document.getElementById('financialModal').classList.add('show');
        })
        document.getElementById('modalBackdrop').style.display = 'block';
    });

    // 폼 추가
    $('#form_add_btn').on('click', function(){

        let new_year = currentYear - 2;

        var year_select = document.getElementById('year_select');

        var option = document.createElement('option');
        option.text = new_year + '년';
        option.value = new_year;

        var paramData = {};
        paramData.year = new_year;

        var param = JSON.stringify(paramData);

        $.ajax({
            url:'/financials/dataAdd',
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

                if(data == -1){
                    document.getElementById('financialModal').style.display = 'none';
                    setTimeout(function(){
                        document.getElementById('financialModal').classList.remove('show');
                    })
                    document.getElementById('duplicateModal').style.display = 'flex';
                    setTimeout(function(){
                        document.getElementById('duplicateModal').classList.add('show');
                    })
                    document.getElementById('modalBackdrop').style.display = 'block';
                }else{
                    year_select.add(option);
                    sortOptions(year_select);
                    year_select.value = new_year;
                    table.column(31).search(new_year).draw();
                    closeModal();
                }
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
    document.getElementById('financialModal').style.display = 'none';
    setTimeout(function(){
        document.getElementById('financialModal').classList.remove('show');
    })
    document.getElementById('modalBackdrop').style.display = 'none';
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