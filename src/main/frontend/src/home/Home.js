import PrTable from "./content/ProductionTable";
import PrChart from "./content/ProductionChart";
import Checkbox from "./content/ChartCheck";
import DateSetting from "./content/ChartDate";

import {useState} from 'react';
import {useEffect} from 'react';
import axios from 'axios';

import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';

import * as React from 'react';
import Card from '@mui/material/Card';
import BarChartIcon from '@mui/icons-material/BarChart';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

function App() {
    /*수신 받은 데이터*/
    const [receivedData ,setReceivedData] = useState(null);

    /*데이터 로딩시 true로 변경*/
    const [loading ,setLoading] = useState(false);

    /*오류 발생시 정보가 들어 있는 예외 객체*/
    const [error ,setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try{
                /*State 초기화*/
                setReceivedData(null);
                setError(null);
                setLoading(true);

                const url = '/react/productionForm';

                const response = await axios.get(url);

                setReceivedData(response.data);

                console.log('response.data');
                console.log(response.data);
            }catch(err){
                setError(err);

            }/*end try...catch*/

            setLoading(false);

        };/*end fetchData*/

        fetchData();/*called fetchData function*/

        /*command가 변경 되면, 화면을 다시 그려 주도록 합니다*/
    },[]);/*end useEffect*/

    if(loading == true){
        return <div>데이터 로딩 중입니다.</div>;
    }
    if(error == true){
        return <div>오류가 발생했습니다.</div>;
    }
    if(!receivedData){
        return null;
    }

    //버튼의 콜백 함수. 기존 데이터를 업데이트 해줍니다.
    const onClickButton = (data) => {
        setReceivedData(data);
    };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh',marginTop:"20px"}}>
        <Card sx={{ width: '85%' , height: '100%' }}>
         <CardHeader
                avatar={
                  <BarChartIcon  sx={{fontSize: 50}}/>
                }
                title="월간 생산 현황"
                titleTypographyProps={{
                    fontWeight: 1000,
                    sx: {
                      fontSize: "h4.fontSize",
                      width:"250px"
                    },
                }}
              />
        <CardContent>
            <DateSetting filterData={onClickButton} />
            <PrChart content={receivedData}/>
            <PrTable content={receivedData}/>
        </CardContent>
        </Card>
	</div>
  );
}
export default App;



