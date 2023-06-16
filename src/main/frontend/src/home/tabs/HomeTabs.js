import * as React from 'react';

import CardHeader from './../card/CardHeader';
import CardContent from './../card/CardContent';

/*탭*/
import PropTypes from 'prop-types';
import SwipeableViews from 'react-swipeable-views';
import { useTheme } from '@mui/material/styles';
import AppBar from '@mui/material/AppBar';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

import Card from '@mui/material/Card';

function TabPanel(props) {
    const { children, value, index, ...other } = props;

    return (
        <div
        role="tabpanel"
        hidden={value !== index}
        id={`full-width-tabpanel-${index}`}
        aria-labelledby={`full-width-tab-${index}`}
        {...other}
        >
        {value === index && (
            <Card sx={{ marginTop:'25px', width: '100%' , height: '90%', borderStyle: 'ridge' }}>
                {children}
            </Card>
        )}
        </div>
    );
}

TabPanel.propTypes = {
    children: PropTypes.node,
    index: PropTypes.number.isRequired,
    value: PropTypes.number.isRequired,
};

function a11yProps(index) {
    return {
            id: `full-width-tab-${index}`,
            'aria-controls': `full-width-tabpanel-${index}`,
        };
    }

export default function FullWidthTabs() {

    const theme = useTheme();
    const [value, setValue] = React.useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    const handleChangeIndex = (index) => {
        setValue(index);
    };

    return (
    <Box sx={{ bgcolor: 'background.paper', width: '100%'}}>
        <AppBar position="static">
            <Tabs
                value={value}
                onChange={handleChange}
                indicatorColor="secondary"
                textColor="inherit"
                variant="fullWidth"
                aria-label="full width tabs example"
            >
                <Tab label="생산 실적" {...a11yProps(0)} />
                <Tab label="자재 재고" {...a11yProps(1)} />
                <Tab label="제품 재고" {...a11yProps(2)} />
            </Tabs>
        </AppBar>
        <SwipeableViews
            axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
            index={value}
            onChangeIndex={handleChangeIndex}
        >
            <TabPanel value={value} index={0} dir={theme.direction}>
                <CardHeader command={'월간 생산 실적'}/>
                <CardContent Search={true} Url={'/react/productionForm'} />
            </TabPanel>
            <TabPanel value={value} index={1} dir={theme.direction}>
                <CardHeader command={'자재 재고 현황'}/>
                <CardContent Search={false} Url={'/react/materialInventory'} />
            </TabPanel>
            <TabPanel value={value} index={2} dir={theme.direction}>
                <CardHeader command={'제품 재고 현황'}/>
                <CardContent Search={false} Url={'/react/productInventory'}/>
            </TabPanel>
        </SwipeableViews>
    </Box>
    );
}