import * as React from 'react';

import CardHeader from '@mui/material/CardHeader';
import BarChartIcon from '@mui/icons-material/BarChart';

export default function CardHeaderOption(props) {
    const command = props.command;
    return (
        <CardHeader
            avatar={
                <BarChartIcon  sx={{fontSize: 50}}/>
            }
            title={command}
            titleTypographyProps={{
                fontWeight: 1000,
                sx: {
                    fontSize: "h4.fontSize",
                    width:"250px"
                },
            }}
        >
        </CardHeader>
    );
}