import * as React from 'react';

import PrChartDate from './../content/ChartDate';
import PrChartData from './../content/ProductionChart';
import PrTableData from './../content/ProductionTable';
import InChartData from './../content/InventoryChart';
import InTableData from './../content/InventoryTable';

import {useState} from 'react';
import {useEffect} from 'react';
import axios from 'axios';

import Grid from '@mui/material/Grid';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';

export default function CardContentOption(props) {
    const ChartUrl = props.ChartUrl;


    const SearchBool = props.Search;
    const Url = props.Url;

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

                const url = Url;

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

    },[]);/*end useEffect*/

    if(loading == true){
        return <div>데이터 로딩 중입니다.</div>;
    }
    if(error){
        return <div></div>;
    }
    if(!receivedData){
        return null;
    }

    const onClickButton = (content,event) => {
        setReceivedData(content);
    }

    return (
    <CardContent>
         <Grid container spacing={2}>
            <Grid item xs={12}>
                {SearchBool && <PrChartDate filterData={onClickButton} />}
            </Grid>
            <Grid item xs={6}>
                {SearchBool ? <PrChartData content={receivedData}/> : <InChartData ChartUrl={ChartUrl}/>}
            </Grid>
            <Grid item xs={6}>
                {SearchBool ? <PrTableData content={receivedData}/> : <InTableData content={receivedData}/> }
            </Grid>
        </Grid>
    </CardContent>
    );
}