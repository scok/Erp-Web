$(document).ready(function(){

    // 버튼 클릭시 항목 이벤트
    $('#assets_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            btn[0].innerText = '-';
            $('.assets_group').show();
        }else if(btn[0].innerText == '-'){
            btn[0].innerText = '+';
            $('.assets_group').hide();
            $('#current_assets_btn')[0].innerText = '+';
            $('.current_assets_group').hide();
            $('#non_current_assets_btn')[0].innerText = '+';
            $('.non_current_assets_group').hide();

            $('#cash_equivalents_btn')[0].innerText = '+';
            $('.cash_equivalents_group').hide();
            $('#inventory_btn')[0].innerText = '+';
            $('.inventory_group').hide();

            $('#prop_pl_equip_btn')[0].innerText = '+';
            $('.prop_pl_equip_group').hide();
            $('#lt_invest_btn')[0].innerText = '+';
            $('.lt_invest_group').hide();
            $('#intangible_assets_btn')[0].innerText = '+';
            $('.intangible_assets_group').hide();
            $('#other_non_current_assets_btn')[0].innerText = '+';
            $('.other_non_current_assets_group').hide();
        }
    });

    $('#current_assets_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.current_assets_group').show();
            btn[0].innerText = '-';
        }else{
            $('.current_assets_group').hide();
            btn[0].innerText = '+';
            $('#cash_equivalents_btn')[0].innerText = '+';
            $('.cash_equivalents_group').hide();
            $('#inventory_btn')[0].innerText = '+';
            $('.inventory_group').hide();
        }
    });

    $('#cash_equivalents_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.cash_equivalents_group').show();
            btn[0].innerText = '-';
        }else{
            $('.cash_equivalents_group').hide();
            btn[0].innerText = '+';

        }
    });

    $('#inventory_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.inventory_group').show();
            btn[0].innerText = '-';
        }else{
            $('.inventory_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#non_current_assets_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.non_current_assets_group').show();
            btn[0].innerText = '-';
        }else{
            $('.non_current_assets_group').hide();
            btn[0].innerText = '+';
            $('#prop_pl_equip_btn')[0].innerText = '+';
            $('.prop_pl_equip_group').hide();
            $('#lt_invest_btn')[0].innerText = '+';
            $('.lt_invest_group').hide();
            $('#intangible_assets_btn')[0].innerText = '+';
            $('.intangible_assets_group').hide();
            $('#other_non_current_assets_btn')[0].innerText = '+';
            $('.other_non_current_assets_group').hide();
        }
    });

    $('#prop_pl_equip_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.prop_pl_equip_group').show();
            btn[0].innerText = '-';
        }else{
            $('.prop_pl_equip_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#lt_invest_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.lt_invest_group').show();
            btn[0].innerText = '-';
        }else{
            $('.lt_invest_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#intangible_assets_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.intangible_assets_group').show();
            btn[0].innerText = '-';
        }else{
            $('.intangible_assets_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#other_non_current_assets_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.other_non_current_assets_group').show();
            btn[0].innerText = '-';
        }else{
            $('.other_non_current_assets_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#liabilities_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.liabilities_group').show();
            btn[0].innerText = '-';
        }else{
            $('.liabilities_group').hide();
            btn[0].innerText = '+';
            $('#current_liabilities_btn')[0].innerText = '+';
            $('.current_liabilities_group').hide();
            $('#non_current_liabilities_btn')[0].innerText = '+';
            $('.non_current_liabilities_group').hide();

            $('#st_borrowings_btn')[0].innerText = '+';
            $('.st_borrowings_group').hide();
            $('#other_current_liabilities_btn')[0].innerText = '+';
            $('.other_current_liabilities_group').hide();
            $('#lt_borrowings_btn')[0].innerText = '+';
            $('.lt_borrowings_group').hide();
            $('#other_non_current_liabilities_btn')[0].innerText = '+';
            $('.other_non_current_liabilities_group').hide();
        }
    });

    $('#current_liabilities_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.current_liabilities_group').show();
            btn[0].innerText = '-';
        }else{
            $('.current_liabilities_group').hide();
            btn[0].innerText = '+';
            $('#st_borrowings_btn')[0].innerText = '+';
            $('.st_borrowings_group').hide();
            $('#other_current_liabilities_btn')[0].innerText = '+';
            $('.other_current_liabilities_group').hide();
        }
    });

    $('#st_borrowings_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.st_borrowings_group').show();
            btn[0].innerText = '-';
        }else{
            $('.st_borrowings_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#other_current_liabilities_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.other_current_liabilities_group').show();
            btn[0].innerText = '-';
        }else{
            $('.other_current_liabilities_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#non_current_liabilities_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.non_current_liabilities_group').show();
            btn[0].innerText = '-';
        }else{
            $('.non_current_liabilities_group').hide();
            btn[0].innerText = '+';
            $('#lt_borrowings_btn')[0].innerText = '+';
            $('.lt_borrowings_group').hide();
            $('#other_non_current_liabilities_btn')[0].innerText = '+';
            $('.other_non_current_liabilities_group').hide();
        }
    });

    $('#lt_borrowings_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.lt_borrowings_group').show();
            btn[0].innerText = '-';
        }else{
            $('.lt_borrowings_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#other_non_current_liabilities_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.other_non_current_liabilities_group').show();
            btn[0].innerText = '-';
        }else{
            $('.other_non_current_liabilities_group').hide();
            btn[0].innerText = '+';
        }
    });

    $('#equity_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.equity_group').show();
            btn[0].innerText = '-';
        }else{
            $('.equity_group').hide();
            btn[0].innerText = '+';
            $('#shareholders_equity_btn')[0].innerText = '+';
            $('.shareholders_equity_group').hide();
            $('#retained_earnings_btn')[0].innerText = '+';
            $('.retained_earnings_group').hide();
        }
    });

    $('#shareholders_equity_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.shareholders_equity_group').show();
            btn[0].innerText = '-';
        }else{
            $('.shareholders_equity_group').hide();
            btn[0].innerText = '+';
            $('#retained_earnings_btn')[0].innerText = '+';
            $('.retained_earnings_group').hide();
        }
    });

    $('#retained_earnings_btn').on('click', function(){

        var btn = $(this);

        if(btn[0].innerText == '+'){
            $('.retained_earnings_group').show();
            btn[0].innerText = '-';
        }else{
            $('.retained_earnings_group').hide();
            btn[0].innerText = '+';
        }
    });
});