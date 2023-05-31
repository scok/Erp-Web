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

    if( target_id === 'tableToExcel'){ // 엑셀 변환

        const headerCells = table.querySelectorAll("thead th");
        const headers = Array.from(headerCells).map(cell => cell.textContent.trim());

        const bodyRows = table.querySelectorAll("tbody tr");
        const data = Array.from(bodyRows).map(row => {
            const computedStyle = getComputedStyle(row);
            if(computedStyle.display === 'none'){
                row.style.display = '';
            }
            const rowData = Array.from(row.querySelectorAll("td")).map(cell => cell.textContent.trim());
            return rowData;
        });
        const lastData = data[data.length - 1];
        const indexToInsert = 1;
        lastData.splice(indexToInsert, 0, '');
        data[data.length - 1] = lastData;

        const workbook = XLSX.utils.book_new();
        const worksheet = XLSX.utils.aoa_to_sheet([headers, ...data]);

        XLSX.utils.book_append_sheet(workbook, worksheet, "Sheet 1");
        XLSX.writeFile(workbook, "table_data.xlsx");
    }else if(target_id === 'tableToPDF'){ // PDF 변환

        var doc = new jspdf.jsPDF('p', 'mm', 'a4');

        doc.addFileToVFS("malgun.ttf", _fonts);
        doc.addFont("malgun.ttf", "malgun", "normal");
        doc.setFont("malgun");

        doc.autoTable({html:table, styles: { font: "malgun", fontStyle: "normal"}, includeHiddenHtml: true});

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