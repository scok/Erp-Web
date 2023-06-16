import * as React from 'react';
import { ResponsiveBar } from '@nivo/bar'

export default function ChartTable(props) {
    const ChartDataList = props.content;    //가져온 데이터
    const result = [];

    const data = ChartDataList.map((item) => {
        const name = "name";
        const prName = item.prName
        const secName = item.secName

        if(!result[name]){
            result[name] = secName;
        }
        result[name] = result[name].concat({ [prName] : item.arTotalCount});

        return { [prName]:item.arTotalCount ,"창고" : secName};
    })

    console.log(data);
    console.log(result);

    return(
    <div style={{ width: '800px', height: '500px', margin: '0 auto' }}>
        <ResponsiveBar
            /* chart에 사용될 데이터*/
            data = {data}
            /*chart에 보여질 데이터 key (측정되는 값) */
            keys= {["LCD 패널","LCD메인보드"]}
            /*keys들을 그룹화하는 index key (분류하는 값)*/
            indexBy="창고"
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
                legend: 'food',
                legendPosition: 'middle',
                legendOffset: -40
            }}
            labelSkipWidth={12}
            labelSkipHeight={12}
            labelTextColor={{
                from: 'color',
                modifiers: [
                    [
                        'darker',
                        1.6
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