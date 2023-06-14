var table;

$(document).ready(function(){

    var year_array = [];

    var data_length = 0;

    table = $("#example").DataTable({
        ordering : false,
        paging : false,
        info : false,
        language: {
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
        serverSide : true,
        processing : false,
        fixedColumns: false,
        ajax : {
            url : '/financials/dataSetting',
            dataSrc: function(res){
                console.log(res);
                var data = res.data;
                var dataArray = res.data;
                for(var i of dataArray){
                    year_array.push(i.year);
                }
                data_length = data.length;
                return data;
            }
        },
        columns : [
                { data : 'id'},
                { data : 'quarter'},
                { data : 'cash',
                  render : formatNumber
                },
                { data : 'cash_equivalents',
                  render : formatNumber
                },
                { data : 'raw_mt',
                  render : formatNumber
                },
                { data : 'product_mt',
                  render : formatNumber
                },
                { data : 'fixture_mt',
                  render : formatNumber
                },
                { data : 'real_estate',
                  render : formatNumber
                },
                { data : 'equipment',
                  render : formatNumber
                },
                { data : 'vehicles',
                  render : formatNumber
                },
                { data : 'equity_invest',
                  render : formatNumber
                },
                { data : 'real_estate_invest',
                  render : formatNumber
                },
                { data : 'corporate_invest',
                  render : formatNumber
                },
                { data : 'trademarks',
                  render : formatNumber
                },
                { data : 'licenses',
                  render : formatNumber
                },
                { data : 'notes_receivable',
                  render : formatNumber
                },
                { data : 'deposits',
                  render : formatNumber
                },
                { data : 'pension_assets',
                  render : formatNumber
                },
                { data : 'total_assets',
                  render : formatNumber
                },
                { data : 'bank_loans',
                  render : formatNumber
                },
                { data : 'trade_credit',
                  render : formatNumber
                },
                { data : 'advance_payments',
                  render : formatNumber
                },
                { data : 'tax_liabilities',
                  render : formatNumber
                },
                { data : 'bonds',
                  render : formatNumber
                },
                { data : 'lt_borrow_pay',
                  render : formatNumber
                },
                { data : 'lt_deposits',
                  render : formatNumber
                },
                { data : 'total_liabilities',
                  render : formatNumber
                },
                { data : 'paid_capital',
                  render : formatNumber
                },
                { data : 'netIncome',
                  render : formatNumber
                },
                { data : 'total_capital',
                  render : formatNumber
                },
                { data : 'totalLiabilitiesCapital',
                  render : formatNumber
                },
                { data : 'year'}
        ],
        initComplete: function () {
            this.api()
                .columns()
                .every(function () {
                    var column = this;
                    $('#year_select').on('change', function(){
                        var val = $.fn.dataTable.util.escapeRegex($(this).val());
                        column.search(val ? '^' + val + '$' : '', true, false).draw();
                    });
                });

            year_array = [...new Set(year_array)];

            var select = document.getElementById('year_select');

            for(var year of year_array){

                var option = document.createElement('option');
                option.text = year + '년';
                option.value = year;

                select.add(option);
            }

            sortOptions(select);
            var maxYear = Math.max(...year_array);
            select.value = maxYear;
            this.api().column(31).search(maxYear).draw();
        },
    });

    $("#example_filter").remove();
    $("#example_length").attr("hidden", "hidden");


    // 화면 표시 테이블 값 설정
    var data_tbody = document.querySelector('.data-table > tbody');
    var data_trs = data_tbody.querySelectorAll('.data');

    var data_key = data_tbody.querySelector('.prKey');
    var data_key_tds = data_key.querySelectorAll('td');

    table.on('draw.dt', ()=>{

        if(data_length != 0){
            var tbody = document.querySelector('#example > tbody');
            var trs = tbody.querySelectorAll('tr');
            var tds1 = trs[0].querySelectorAll('td');
            var tds2 = trs[1].querySelectorAll('td');
            var tds3 = trs[2].querySelectorAll('td');
            var tds4 = trs[3].querySelectorAll('td');

            data_key_tds[1].innerText = tds1[0].innerText;
            data_key_tds[2].innerText = tds2[0].innerText;
            data_key_tds[3].innerText = tds3[0].innerText;
            data_key_tds[4].innerText = tds4[0].innerText;

            for(let i=0;i<data_trs.length;i++){

                var data_tds = data_trs[i].querySelectorAll('td');

                if(i == (data_trs.length - 1) || i == 16 || i == 24 || i == 27 || i == 28){

                    var data_span1 = data_tds[1].querySelector('span');
                    var data_span2 = data_tds[2].querySelector('span');
                    var data_span3 = data_tds[3].querySelector('span');
                    var data_span4 = data_tds[4].querySelector('span');

                    data_span1.innerText = tds1[i+2].innerText.toLocaleString();
                    data_span2.innerText = tds2[i+2].innerText.toLocaleString();
                    data_span3.innerText = tds3[i+2].innerText.toLocaleString();
                    data_span4.innerText = tds4[i+2].innerText.toLocaleString();
                }else{

                    var data_span1 = data_tds[2].querySelector('span');
                    var data_span2 = data_tds[3].querySelector('span');
                    var data_span3 = data_tds[4].querySelector('span');
                    var data_span4 = data_tds[5].querySelector('span');

                    data_span1.innerText = tds1[i+2].innerText.toLocaleString();
                    data_span2.innerText = tds2[i+2].innerText.toLocaleString();
                    data_span3.innerText = tds3[i+2].innerText.toLocaleString();
                    data_span4.innerText = tds4[i+2].innerText.toLocaleString();
                }
            }

            /* 자산 */

            // 현금 및 현금성 자산
            var total_cash_equivalents_tds = document.querySelectorAll('.total_cash_equivalents');
            var cash_equivalents_group_trs = document.querySelectorAll('.cash_equivalents_group');

            for(let i=0; i<total_cash_equivalents_tds.length;i++){

                var total_span = total_cash_equivalents_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<cash_equivalents_group_trs.length;j++){

                    let cash_equivalents_group_tds = cash_equivalents_group_trs[j].querySelectorAll('td');
                    let data_span = cash_equivalents_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 재고 자산
            var total_inventory_tds = document.querySelectorAll('.total_inventory');
            var inventory_group_trs = document.querySelectorAll('.inventory_group');

            for(let i=0; i<total_inventory_tds.length;i++){

                var total_span = total_inventory_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<inventory_group_trs.length;j++){

                    let inventory_group_tds = inventory_group_trs[j].querySelectorAll('td');
                    let data_span = inventory_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 총 유동 자산
            var total_current_assets_tds = document.querySelectorAll('.total_current_assets');

            for(let i=0; i<total_current_assets_tds.length;i++){

                var sum = 0;

                var total_span = total_current_assets_tds[i].querySelector('span');

                var cash_equivalents = parseFloat(total_cash_equivalents_tds[i].innerText.replace(/,/g, ""));
                var inventory = parseFloat(total_inventory_tds[i].innerText.replace(/,/g, ""));

                if(!isNaN(cash_equivalents) && !isNaN(inventory)){
                    sum += cash_equivalents + inventory;
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 부동산 및 장비
            var total_prop_pl_equip_tds = document.querySelectorAll('.total_prop_pl_equip');
            var prop_pl_equip_group_trs = document.querySelectorAll('.prop_pl_equip_group');

            for(let i=0; i<total_prop_pl_equip_tds.length;i++){

                var total_span = total_prop_pl_equip_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<prop_pl_equip_group_trs.length;j++){

                    let prop_pl_equip_group_tds = prop_pl_equip_group_trs[j].querySelectorAll('td');
                    let data_span = prop_pl_equip_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 장기 투자
            var total_lt_invest_tds = document.querySelectorAll('.total_lt_invest');
            var lt_invest_group_trs = document.querySelectorAll('.lt_invest_group');

            for(let i=0; i<total_lt_invest_tds.length;i++){

                var total_span = total_lt_invest_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<lt_invest_group_trs.length;j++){

                    let lt_invest_group_tds = lt_invest_group_trs[j].querySelectorAll('td');
                    let data_span = lt_invest_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 무형 자산
            var total_intangible_assets_tds = document.querySelectorAll('.total_intangible_assets');
            var intangible_assets_group_trs = document.querySelectorAll('.intangible_assets_group');

            for(let i=0; i<total_intangible_assets_tds.length;i++){

                var total_span = total_intangible_assets_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<intangible_assets_group_trs.length;j++){

                    let intangible_assets_group_tds = intangible_assets_group_trs[j].querySelectorAll('td');
                    let data_span = intangible_assets_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();;
            }

            // 기타 비유동 자산
            var total_other_non_current_assets_tds = document.querySelectorAll('.total_other_non_current_assets');
            var other_non_current_assets_group_trs = document.querySelectorAll('.other_non_current_assets_group');

            for(let i=0; i<total_other_non_current_assets_tds.length;i++){

                var total_span = total_other_non_current_assets_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<other_non_current_assets_group_trs.length;j++){

                    let other_non_current_assets_group_tds = other_non_current_assets_group_trs[j].querySelectorAll('td');
                    let data_span = other_non_current_assets_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();;
            }

            // 총 비유동 자산
            var total_non_current_assets_tds = document.querySelectorAll('.total_non_current_assets');

            for(let i=0; i<total_non_current_assets_tds.length;i++){

                var sum = 0;

                var total_span = total_non_current_assets_tds[i].querySelector('span');

                var prop_pl_equip = parseFloat(total_prop_pl_equip_tds[i].innerText.replace(/,/g, ""));
                var lt_invest = parseFloat(total_lt_invest_tds[i].innerText.replace(/,/g, ""));
                var intangible = parseFloat(total_intangible_assets_tds[i].innerText.replace(/,/g, ""));
                var other_non_current = parseFloat(total_other_non_current_assets_tds[i].innerText.replace(/,/g, ""));

                if(!isNaN(prop_pl_equip) && !isNaN(lt_invest) && !isNaN(intangible) && !isNaN(other_non_current)){
                    sum += prop_pl_equip + lt_invest + intangible + other_non_current;
                }

                total_span.innerText = sum.toLocaleString();
            }

            /* 부채 */

            // 단기 차입금
            var total_st_borrowings_tds = document.querySelectorAll('.total_st_borrowings');
            var st_borrowings_group_trs = document.querySelectorAll('.st_borrowings_group');

            for(let i=0; i<total_st_borrowings_tds.length;i++){

                var total_span = total_st_borrowings_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<st_borrowings_group_trs.length;j++){

                    let st_borrowings_group_tds = st_borrowings_group_trs[j].querySelectorAll('td');
                    let data_span = st_borrowings_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 기타 유동 부채
            var total_other_current_liabilities_tds = document.querySelectorAll('.total_other_current_liabilities');
            var other_current_liabilities_group_trs = document.querySelectorAll('.other_current_liabilities_group');

            for(let i=0; i<total_other_current_liabilities_tds.length;i++){

                var total_span = total_other_current_liabilities_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<other_current_liabilities_group_trs.length;j++){

                    let other_current_liabilities_group_tds = other_current_liabilities_group_trs[j].querySelectorAll('td');
                    let data_span = other_current_liabilities_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 총 유동 부채
            var total_current_liabilities_tds = document.querySelectorAll('.total_current_liabilities');

            for(let i=0; i<total_current_liabilities_tds.length;i++){

                var sum = 0;

                var total_span = total_current_liabilities_tds[i].querySelector('span');

                var st_borrowings = parseFloat(total_st_borrowings_tds[i].innerText.replace(/,/g, ""));
                var other_current_liabilities = parseFloat(total_other_current_liabilities_tds[i].innerText.replace(/,/g, ""));

                if(!isNaN(st_borrowings) && !isNaN(other_current_liabilities)){
                    sum += st_borrowings + other_current_liabilities;
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 장기 차입금
            var total_lt_borrowings_tds = document.querySelectorAll('.total_lt_borrowings');
            var lt_borrowings_group_trs = document.querySelectorAll('.lt_borrowings_group');

            for(let i=0; i<total_lt_borrowings_tds.length;i++){

                var total_span = total_lt_borrowings_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<lt_borrowings_group_trs.length;j++){

                    let lt_borrowings_group_tds = lt_borrowings_group_trs[j].querySelectorAll('td');
                    let data_span = lt_borrowings_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 기타 비유동 부채
            var total_other_non_current_liabilities_tds = document.querySelectorAll('.total_other_non_current_liabilities');
            var other_non_current_liabilities_group_trs = document.querySelectorAll('.other_non_current_liabilities_group');

            for(let i=0; i<total_other_non_current_liabilities_tds.length;i++){

                var total_span = total_other_non_current_liabilities_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<other_non_current_liabilities_group_trs.length;j++){

                    let other_non_current_liabilities_group_tds = other_non_current_liabilities_group_trs[j].querySelectorAll('td');
                    let data_span = other_non_current_liabilities_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 총 비유동 부채
            var total_non_current_liabilities_tds = document.querySelectorAll('.total_non_current_liabilities');

            for(let i=0; i<total_non_current_liabilities_tds.length;i++){

                var sum = 0;

                var total_span = total_non_current_liabilities_tds[i].querySelector('span');

                var lt_borrowings = parseFloat(total_lt_borrowings_tds[i].innerText.replace(/,/g, ""));
                var other_non_current_liabilities = parseFloat(total_other_non_current_liabilities_tds[i].innerText.replace(/,/g, ""));

                if(!isNaN(lt_borrowings) && !isNaN(other_non_current_liabilities)){
                    sum += lt_borrowings + other_non_current_liabilities;
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 이익 재투자
            var total_retained_earnings_tds = document.querySelectorAll('.total_retained_earnings');
            var retained_earnings_group_trs = document.querySelectorAll('.retained_earnings_group');

            for(let i=0; i<total_retained_earnings_tds.length;i++){

                var total_span = total_retained_earnings_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<retained_earnings_group_trs.length;j++){

                    let retained_earnings_group_tds = retained_earnings_group_trs[j].querySelectorAll('td');
                    let data_span = retained_earnings_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 지배기업 주주 지분
            var total_shareholders_equity_tds = document.querySelectorAll('.total_shareholders_equity');
            var shareholders_equity_group_trs = document.querySelectorAll('.shareholders_equity_group');

            for(let i=0; i<total_shareholders_equity_tds.length;i++){

                var total_span = total_shareholders_equity_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<shareholders_equity_group_trs.length;j++){

                    let shareholders_equity_group_tds = shareholders_equity_group_trs[j].querySelectorAll('td');
                    let data_span = shareholders_equity_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }
        }
    });
});

function formatNumber(data, type){
    var number = $.fn.dataTable.render.number(',', '.', 0).display(data);

    return number;
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

export {table};