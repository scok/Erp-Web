import React, { useEffect, useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';

export default function DataTable(props) {

      const [tableData, setTableData] = useState([]);

      // props.data가 변경될 때마다 테이블 데이터 업데이트
      useEffect(() => {
        setTableData(props.content);
      }, [props.content]);

    /*컬럼 설정*/
    const columns = [
      { field: 'id', headerName: '순번', width: 200 },
      { field: 'productionLine', headerName: '조립 라인', width: 200 },
      { field: 'prName', headerName: '제품 명', width: 200 },
      { field: 'count', headerName: '수량', width: 200 }
    ];

    const rows = tableData.map((item,index) => ({
      id : index+1,
      productionLine: item.productionLine,
      prName: item.prName,
      count: item.totalCount,
    }));

    return (
    <div style={{width:'100%',display: 'flex', justifyContent: 'center', alignItems: 'center' , marginTop:'50px'}}>
        <div style={{width: '80%'}}>
          <DataGrid
            rows={rows}
            columns={columns}
            initialState={{
              pagination: {
                paginationModel: { page: 0, pageSize: 5 },
              },
            }}
            pageSizeOptions={[5, 10]}
          />
        </div>
    </div>
    );
}
