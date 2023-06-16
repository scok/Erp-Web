import * as React from 'react';
import  { useState } from 'react';

import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import {DatePicker} from '@mui/x-date-pickers/DatePicker';

import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';

import SendIcon from '@mui/icons-material/Send';
import Button from '@mui/material/Button';
import axios from 'axios';

export default function CommonlyUsedComponents(props) {

    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);


    //날짜를 이용하여 데이터 필터링 합니다.
    const handleSearch = () => {
        var sendData = JSON.stringify({
            "startDate": startDate,
            "endDate": endDate
        });
        axios.post('/react/filterDate', sendData,{
            headers: {
                'Access-Control-Allow-Origin': 'http://localhost:3000',
                'Content-Type': 'application/json',
                 Authorization: 'Bearer your-auth-token',
              },
        })
        .then(response => {
        console.log('POST success');
        props.filterData(response.data);    //콜백 함수에 값을 던져 줍니다.
        })
        .catch(error => {
        console.error('POST error:', error.response.data);
        });
    };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
        <Grid container spacing={1} style={{alignItems:"center"}}>
            <Grid item xs={2}>
              <DatePicker
                label={"시작 날짜"}
                value={startDate}
                onChange={(date) => setStartDate(date)}
              />
            </Grid>
            <Grid item xs={2}>
              <DatePicker
                label={"종료 날짜"}
                value={endDate}
                onChange={(date) => setEndDate(date)}
              />
            </Grid>
            <Grid item xs={2}>
                <Button variant="contained" endIcon={<SendIcon />} onClick={handleSearch}>
                    search
                </Button>
            </Grid>
          </Grid>
    </LocalizationProvider>
  );
}