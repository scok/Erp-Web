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
    const [receivedWeather, setReceivedWeather] = useState(null);
    const [weatherImage, setWeatherImage] = useState(null);
    const [weatherCity, setWeatherCity] = useState(null);
    const [wind, setWind] = useState(null);
    const [humidity, setHumidity] = useState(null);
    const [cloud, setCloud] = useState(null);

    /*데이터 로딩시 true로 변경*/
    const [loading ,setLoading] = useState(false);

    /*오류 발생시 정보가 들어 있는 예외 객체*/
    const [error ,setError] = useState(null);

    useEffect(() => {
        axios.get('https://api.openweathermap.org/data/2.5/weather',{
            params:{
                q:'Seoul',
                appid:'5e3d585848cc5ef63405ab101b0f863a'
            }
        })
        .then(response =>{
            console.log(response.data);
            setReceivedWeather(Math.floor(response.data.main.temp - 273));
            setWeatherImage("http://openweathermap.org/img/wn/" + response.data.weather[0].icon + "@2x.png")
            setWeatherCity(response.data.name)
            setWind(response.data.wind.speed)
            setHumidity(response.data.main.humidity)
            setCloud(response.data.clouds.all)
        })
        .catch(error => {
            console.log(error);
        });
    }, []);

  return (
    <>
        <div style={{float: 'right'}}>
            <div className="card shadow">
                <div className="left" style={{backgroundColor:'#73685d', padding:'10px'}}>
                    <div className="left-left">
                        <div className="img-container">
                            <img src={weatherImage}/>
                        </div>
                    </div>
                    <div className="text-column" style={{color:'white'}}>
                        <p>{receivedWeather} °C</p>
                        <p>{weatherCity}</p>
                    </div>
                </div>
                <div className="right">
                    <div className="row">
                        <div className="section" >
                            <div className="content"><i className="wi wi-strong-wind wi-fw"></i></div>
                        </div>
                        <div className="section">
                            <div className="content"><i className="wi wi-humidity wi-fw"></i></div>
                        </div>
                        <div className="section">
                            <div className="content"><i className="wi wi-cloud wi-fw"></i></div>
                        </div>
                    </div>
                    <div className="row">
                        <div className="section">
                            <div className="content">{wind} m/s</div>
                        </div>
                        <div className="section">
                            <div className="content">{humidity} %</div>
                        </div>
                        <div className="section">
                            <div className="content">{cloud} %</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </>
  );
}
export default App;



