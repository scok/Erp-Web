import * as React from 'react';
import { ResponsiveBar } from '@nivo/bar'

import {useState} from 'react';
import {useEffect} from 'react';
import axios from 'axios';

export default function ChartTable(props) {

    const ChartUrl = props.ChartUrl;

    /*수신 받은 데이터*/
    const [ChartDataList ,setChartDataList] = useState(null);

    /*데이터 로딩시 true로 변경*/
    const [loading ,setLoading] = useState(false);

    /*오류 발생시 정보가 들어 있는 예외 객체*/
    const [error ,setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try{
                /*State 초기화*/
                setChartDataList(null);
                setError(null);
                setLoading(true);

                const url = ChartUrl;

                const response = await axios.get(url);

                setChartDataList(response.data);

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
    if(!ChartDataList){
        return null;
    }

    const prName = ChartDataList.map((item) => item.prName);
    const keys = [...new Set(prName)];

    console.log(keys);

    const data = [];
    ChartDataList.forEach((item) => {
    const existingData = data.find((obj) => obj.secName === item.secName);
    if (existingData) {
        existingData[item.prName] = item.inQuantity;
    } else {
        const newData = {
            secName: item.secName,
            [item.prName]: item.inQuantity
        };
        data.push(newData);
    }
    });

    console.log(data);

    return(
    <div style={{ width: '800px', height: '500px', margin: '0 auto' }}>
        <ResponsiveBar
            /* chart에 사용될 데이터*/
            data = {data}
            /*chart에 보여질 데이터 key (측정되는 값) */
            keys= {keys}
            /*keys들을 그룹화하는 index key (분류하는 값)*/
            indexBy="secName"
            groupMode="grouped"
            margin={{ top: 50, right: 130, bottom: 50, left: 60 }}
            padding={0.3}
            valueScale={{ type: 'linear' }}
            indexScale={{ type: 'band', round: true }}
            colors={{ scheme: 'nivo' }}
            // colors={{ scheme: 'nivo' }} // nivo에서 제공해주는 색상 조합 사용할 때
            /* color 적용 방식*/
            colorBy="id" // 색상을 keys 요소들에 각각 적용  // colorBy="indexValue" // indexBy로 묵인 인덱스별로 각각 적용
            defs={[
                {
                    id: 'dots',
                    type: 'patternDots',
                    background: 'inherit',
                    color: '#38bcb2',
                    size: 4,
                    padding: 1,
                    stagger: true
                },
                {
                    id: 'lines',
                    type: 'patternLines',
                    background: 'inherit',
                    color: '#eed312',
                    rotation: -45,
                    lineWidth: 6,
                    spacing: 10
                }
            ]}
            fill={[
                {
                    match: {
                        id: 'fries'
                    },
                    id: 'dots'
                },
                {
                    match: {
                        id: 'sandwich'
                    },
                    id: 'lines'
                }
            ]}
            borderRadius={4}
            borderColor={{
                from: 'color',
                modifiers: [
                    [
                        'darker',
                        1.6
                    ]
                ]
            }}
            axisTop={null}
            axisRight={null}
            axisBottom={{
                tickSize: 5,
                tickPadding: 5,
                tickRotation: 0,
                legend: '창고 구역별',
                legendPosition: 'middle',
                legendOffset: 32
            }}
            axisLeft={{
                tickSize: 5,
                tickPadding: 5,
                tickRotation: 0,
                legend: '수량',
                legendPosition: 'middle',
                legendOffset: -55
            }}
            labelSkipWidth={12}
            labelSkipHeight={12}
            labelTextColor={{
                from: 'color',
                modifiers: [
                    [
                        'darker',
                        '3'
                    ]
                ]
            }}
            legends={[
                {
                    dataFrom: 'keys',
                    anchor: 'bottom-right',
                    direction: 'column',
                    justify: false,
                    translateX: 120,
                    translateY: 0,
                    itemsSpacing: 2,
                    itemWidth: 100,
                    itemHeight: 20,
                    itemDirection: 'left-to-right',
                    itemOpacity: 0.85,
                    symbolSize: 20,
                    effects: [
                        {
                            on: 'hover',
                            style: {
                                itemOpacity: 1
                            }
                        }
                    ]
                }
            ]}
            role="application"
            ariaLabel="Nivo bar chart demo"
            barAriaLabel={e=>e.id+": "+e.formattedValue+" in country: "+e.indexValue}
        />
    </div>
    );
}