var table;

$(document).ready(function(){

    // 데이터 테이블 세팅
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
            url : '/incomes/dataSetting',
            dataSrc: function(res){
                var data = res.data;
                var dataArray = res.data;
                for(var i of dataArray){
                    year_array.push(i.year);
                }
                data_length = data.length;
                console.log(data);
                return data;
            }
        },
        columns : [
                { data : 'id'},
                { data : 'quarter'},
                { data : 'sales_revenue',
                  render : formatNumber
                },
                { data : 'interest',
                  render : formatNumber
                },
                { data : 'rental',
                  render : formatNumber
                },
                { data : 'investment',
                  render : formatNumber
                },
                { data : 'licensing',
                  render : formatNumber
                },
                { data : 'total_revenue',
                  render : formatNumber
                },
                { data : 'salary',
                  render : formatNumber
                },
                { data : 'bonus',
                  render : formatNumber
                },
                { data : 'plusMoney',
                  render : formatNumber
                },
                { data : 'minusMoney',
                  render : formatNumber
                },
                { data : 'totalMoney',
                  render : formatNumber
                },
                { data : 'manage_expenses',
                  render : formatNumber
                },
                { data : 'advertising',
                  render : formatNumber
                },
                { data : 'office_rent',
                  render : formatNumber
                },
                { data : 'accService_costs',
                  render : formatNumber
                },
                { data : 'consulting_costs',
                  render : formatNumber
                },
                { data : 'fixtures',
                  render : formatNumber
                },
                { data : 'raw_mat_cost',
                  render : formatNumber
                },
                { data : 'components_cost',
                  render : formatNumber
                },
                { data : 'total_expenses',
                  render : formatNumber
                },
                { data : 'operate_revenue',
                  render : formatNumber
                },
                { data : 'operate_expenses',
                  render : formatNumber
                },
                { data : 'operate_income',
                  render : formatNumber
                },
                { data : 'financial_expenses',
                  render : formatNumber
                },
                { data : 'depreciation_expenses',
                  render : formatNumber
                },
                { data : 'other_expenses',
                  render : formatNumber
                },
                { data : 'corporate_tax',
                  render : formatNumber
                },
                { data : 'income_tax',
                  render : formatNumber
                },
                { data : 'localTax',
                  render : formatNumber
                },
                { data : 'tax_expenses',
                  render : formatNumber
                },
                { data : 'netIncome',
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

            var maxYear = Math.max(...year_array);
            this.api().column(33).search(maxYear).draw();
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
            console.log(trs);
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

                if(i == (data_trs.length - 1) || i == 5 || i == 19 || i == 22 || i == 25 || i == 29){

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

            /* 수익 */

            // 기타 수익
            var total_other_revenue_tds = document.querySelectorAll('.total_other_revenue');
            var other_revenue_group_trs = document.querySelectorAll('.other_revenue_group');

            for(let i=0; i<total_other_revenue_tds.length;i++){

                var total_span = total_other_revenue_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<other_revenue_group_trs.length;j++){

                    let other_revenue_group_tds = other_revenue_group_trs[j].querySelectorAll('td');
                    let data_span = other_revenue_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }

            /* 비용 */

            // 인력 비용
            var total_manpower_tds = document.querySelectorAll('.total_manpower');
            var manpower_group_trs = document.querySelectorAll('.manpower_group');

            for(let i=0; i<total_manpower_tds.length;i++){

                var total_span = total_manpower_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<manpower_group_trs.length;j++){

                    let manpower_group_tds = manpower_group_trs[j].querySelectorAll('td');
                    let data_span = manpower_group_tds[i+2].querySelector('span');

                    let data = parseFloat(data_span.innerText.replace(/,/g, ""));

                    if(!isNaN(data)){
                        sum += data;
                    }
                }

                total_span.innerText = sum.toLocaleString();
            }

            // 재료비
            var total_material_expenses_tds = document.querySelectorAll('.total_material_expenses');
            var material_expenses_group_trs = document.querySelectorAll('.material_expenses_group');

            for(let i=0; i<total_material_expenses_tds.length;i++){

                var total_span = total_material_expenses_tds[i].querySelector('span');
                var sum = 0;

                for(let j=0;j<material_expenses_group_trs.length;j++){

                    let material_expenses_group_tds = material_expenses_group_trs[j].querySelectorAll('td');
                    let data_span = material_expenses_group_tds[i+2].querySelector('span');

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

export {table};
