// 파일 변환
function convertFile(event){

    var target_id = event.target.id;

    var table = document.getElementById('data-table');

    // 각 행 버튼 임시로 빈 값으로 설정
    const buttons = table.getElementsByTagName('button');
    const buttonTexts = Array.from(buttons).map(button => button.innerText);

    for(var bean of buttons){
        bean.innerText = '';
    }

    // 선택된 옵션 값 외에 옵션 값들 임시로 빈 값으로 설정
    const year_select = document.getElementById('year_select');

    const value_array = [];

    const selectedValue = year_select.value;
    const options = year_select.querySelectorAll("option");
    options.forEach(option => {

      value_array.push(option.value);
      if (option.value !== selectedValue) {
        option.value = '';
        option.textContent = '';
      }
    });

    if(target_id === 'tableToPDF'){ // PDF 변환

        var doc = new jspdf.jsPDF('p', 'mm', 'a4');

        doc.addFileToVFS("malgun.ttf", _fonts);
        doc.addFont("malgun.ttf", "malgun", "normal");
        doc.setFont("malgun");

        var tableStyle = {
            font: "malgun",
            fontColor: '#000000',
            fontStyle: "normal"
        };

        doc.autoTable({
            html:table,
            includeHiddenHtml: true,
            styles: tableStyle,
            headStyles:{
                fillColor: [66, 68, 78]
            },
            bodyStyles:{
                lineColor: [198, 201, 204],
                lineWidth: 0.5
            },
            columnStyles:{
                0:{
                    fillColor: [255, 255, 255]
                },
                1:{
                    fillColor: [255, 255, 255]
                },
                2:{
                    fillColor: [255, 255, 255]
                },
                3:{
                    fillColor: [255, 255, 255]
                },
                4:{
                    fillColor: [255, 255, 255]
                },
                5:{
                    fillColor: [255, 255, 255]
                },
                6:{
                    fillColor: [255, 255, 255]
                },
                7:{
                    fillColor: [255, 255, 255]
                },
                8:{
                    fillColor: [255, 255, 255]
                }
            }
        });

        doc.save('data_table.pdf');
    }

    // 버튼 및 셀렉트 값들 복원
    for(let i=0; i< buttons.length; i++){
        buttons[i].innerText = buttonTexts[i];
    }

    options.forEach((option, index) => {

      option.value = value_array[index];
      option.textContent = value_array[index];
    });
}