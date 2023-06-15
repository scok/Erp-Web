$(document).ready(function(){

    // 버튼 클릭시 항목 이벤트
    $('#allOpenList').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '모두 펼치기'){
            btn[0].innerText = '모두 접기';

            $('#revenue_btn')[0].innerText = '-';
            $('.revenue_group').show();
            $('#other_revenue_btn')[0].innerText = '-';
            $('.other_revenue_group').show();
            $('.expenses_group').show();

            $('#expenses_btn')[0].innerText = '-';

            $('.manpower_group').show();
            $('#manpower_btn')[0].innerText = '-';

            $('#material_expenses_btn')[0].innerText = '-';
            $('.material_expenses_group').show();

            $('.operating_income_group').show();
            $('#operating_income_btn')[0].innerText = '-';

            $('.other_expenses_group').show();
            $('#other_expenses_btn')[0].innerText = '-';

            $('.tax_expenses_group').show();
            $('#tax_expenses_btn')[0].innerText = '-';
        }else{
            btn[0].innerText = '모두 펼치기';

            $('#revenue_btn')[0].innerText = '+';
            $('.revenue_group').hide();
            $('#other_revenue_btn')[0].innerText = '+';
            $('.other_revenue_group').hide();
            $('.expenses_group').hide();

            $('#expenses_btn')[0].innerText = '+';

            $('.manpower_group').hide();
            $('#manpower_btn')[0].innerText = '+';

            $('#material_expenses_btn')[0].innerText = '+';
            $('.material_expenses_group').hide();

            $('.operating_income_group').hide();
            $('#operating_income_btn')[0].innerText = '+';

            $('.other_expenses_group').hide();
            $('#other_expenses_btn')[0].innerText = '+';

            $('.tax_expenses_group').hide();
            $('#tax_expenses_btn')[0].innerText = '+';
        }
    });

    // 버튼 클릭시 항목 이벤트
    $('#revenue_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            btn[0].innerText = '-';
            $('.revenue_group').show();
        }else if(btn[0].innerText == '-'){
            btn[0].innerText = '+';
            $('.revenue_group').hide();
            $('#other_revenue_btn')[0].innerText = '+';
            $('.other_revenue_group').hide();
        }
    });

    $('#other_revenue_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.other_revenue_group').show();
            btn[0].innerText = '-';
        }else{
            $('.other_revenue_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#expenses_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.expenses_group').show();
            btn[0].innerText = '-';
        }else{
            $('.expenses_group').hide();
            btn[0].innerText = '+';

            $('#selling_administrative_btn')[0].innerText = '+';
            $('.selling_administrative_group').hide();
            $('#selling_expenses_btn')[0].innerText = '+';
            $('.selling_expenses_group').hide();
            $('#administrative_expenses_btn')[0].innerText = '+';
            $('.administrative_expenses_group').hide();

            $('#personnel_expenses_btn')[0].innerText = '+';
            $('.personnel_expenses_group').hide();

            $('#material_expenses_btn')[0].innerText = '+';
            $('.material_expenses_group').hide();
        }
    });

    $('#manpower_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.manpower_group').show();
            btn[0].innerText = '-';
        }else{
            $('.manpower_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#selling_administrative_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.selling_administrative_group').show();
            btn[0].innerText = '-';
        }else{
            $('.selling_administrative_group').hide();
            btn[0].innerText = '+';
            $('#selling_expenses_btn')[0].innerText = '+';
            $('.selling_expenses_group').hide();
            $('#administrative_expenses_btn')[0].innerText = '+';
            $('.administrative_expenses_group').hide();
        }
    });

    $('#selling_expenses_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.selling_expenses_group').show();
            btn[0].innerText = '-';
        }else{
            $('.selling_expenses_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#administrative_expenses_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.administrative_expenses_group').show();
            btn[0].innerText = '-';
        }else{
            $('.administrative_expenses_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#personnel_expenses_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.personnel_expenses_group').show();
            btn[0].innerText = '-';
        }else{
            $('.personnel_expenses_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#material_expenses_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.material_expenses_group').show();
            btn[0].innerText = '-';
        }else{
            $('.material_expenses_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#operating_income_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.operating_income_group').show();
            btn[0].innerText = '-';
        }else{
            $('.operating_income_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#other_expenses_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.other_expenses_group').show();
            btn[0].innerText = '-';
        }else{
            $('.other_expenses_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#tax_expenses_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.tax_expenses_group').show();
            btn[0].innerText = '-';
        }else{
            $('.tax_expenses_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#modalCloseBtn').on('click', function(){

        document.getElementById('incomeModal').style.display = 'none';
        setTimeout(function(){
            document.getElementById('incomeModal').classList.remove('show');
        })
        document.getElementById('modalBackdrop').style.display = 'none';
    });

    $('#dModalCloseBtn').on('click', function(){

        document.getElementById('duplicateModal').style.display = 'none';
        setTimeout(function(){
            document.getElementById('duplicateModal').classList.remove('show');
        })
        document.getElementById('modalBackdrop').style.display = 'none';
    });

    $('#authCloseBtn').on('click', function(){

        document.getElementById('authModal').style.display = 'none';
        setTimeout(function(){
            document.getElementById('authModal').classList.remove('show');
        })
        document.getElementById('modalBackdrop').style.display = 'none';
    });
});