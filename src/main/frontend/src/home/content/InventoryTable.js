import React, { useEffect, useState } from 'react';
import { DataGrid,
        GridToolbar,
        gridPageCountSelector,
        gridPageSelector,
        useGridApiContext,
        useGridSelector
} from '@mui/x-data-grid';

import Pagination from '@mui/material/Pagination';
import PaginationItem from '@mui/material/PaginationItem';

export default function DataTable(props) {

      const [tableData, setTableData] = useState([]);

      // props.data가 변경될 때마다 테이블 데이터 업데이트
      useEffect(() => {
        setTableData(props.content);
      }, [props.content]);

    /*컬럼 설정*/
    const columns = [
      { field: 'id', headerName: 'NO', width: 10 },
      { field: 'secName', headerName: '창고 명', width: 200 },
      { field: 'stackAreaCategory', headerName: '구역 명', width: 200 },
      { field: 'prName', headerName: '자재 명', width: 200 },
      { field: 'count', headerName: '수량', width: 200 }
    ];

    const rows = tableData.map((item,index) => ({
      id : index+1,
      secName: item.secName,
      stackAreaCategory: item.stackAreaCategory,
      prName: item.prName,
      count: item.arTotalCount,
    }));

    function CustomPagination() {
      const apiRef = useGridApiContext();
      const page = useGridSelector(apiRef, gridPageSelector);
      const pageCount = useGridSelector(apiRef, gridPageCountSelector);

        return (
        <Pagination
        color="primary"
        variant="outlined"
        shape="rounded"
        page={page + 1}
        count={pageCount}
        // @ts-expect-error
        renderItem={(props2) => <PaginationItem {...props2} disableRipple />}
        onChange={(event, value) => apiRef.current.setPage(value - 1)}
        />
        );
    }

    return (
    <div style={{width:'100%',display: 'flex', justifyContent: 'center', alignItems: 'center' , marginTop:'65px'}}>
        <div style={{width: '80%'}}>
            <DataGrid
                rows={rows}
                columns={columns}
                initialState={{
                    pagination: {
                    paginationModel: { page: 0, pageSize: 10 },
                    },
                }}
                sx={{
                    boxShadow: 2,
                    border: 2,
                    borderColor: 'primary.light',
                    '& .MuiDataGrid-cell:hover': {
                    color: 'primary.main',
                    },
                }}
                disableColumnFilter
                disableColumnSelector
                disableDensitySelector
                slots={{ toolbar: GridToolbar ,pagination: CustomPagination,}}
                slotProps={{
                    toolbar: {
                        showQuickFilter: true,
                        quickFilterProps: { debounceMs: 500 },
                    },
                }}
            />
        </div>
    </div>
    );
}
