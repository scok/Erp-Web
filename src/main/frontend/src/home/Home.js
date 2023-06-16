import * as React from 'react';

import Tabs from './tabs/HomeTabs';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';

function App() {

    return (
    <>
        <div style={{ width:'100%',paddingRight:'10px'}} >
            <Grid container spacing={1}>
                <Grid item xs={12} style={{textAlign: 'center' , paddingLeft:"70px"}}>
                    <img alt="iPhone_01" src="sidebarImg/logo.png" style={{ width: '100px',height: '100px'}} />
                    날씨정보
                </Grid>
            </Grid>
        </div>
        <div style={{ width:'100%', height: '100vh',paddingRight:'10px'}}>
            <Tabs/>
        </div>
    </>
    );
}
export default App;



