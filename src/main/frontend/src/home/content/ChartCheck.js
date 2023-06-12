import React, { useState } from 'react';

import Checkbox from '@mui/material/Checkbox';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';

export default function CheckBox(props) {
    const checkBoxData = props.content;

    const checkBoxChange = (event) => {
        console.log(event.target.id);
    }

    const FormControlLabelList = checkBoxData.map((item,index) => (
      <FormControlLabel
        key={item.productionLine}
        value={item.productionLine}
        control={<Checkbox id={item.productionLine} onChange={checkBoxChange} />}
        label={item.productionLine}
        labelPlacement="end"
      />
    ));


    return (
    <div>
         <FormControl component="fieldset">
             <FormLabel component="legend" sx={{fontSize: "h6.fontSize",fontWeight:800,}}>생산 라인 선택</FormLabel>
             <FormGroup aria-label="position" row>
               {FormControlLabelList}
             </FormGroup>
           </FormControl>
    </div>
    );
}
