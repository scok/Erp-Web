import React from 'react';
import ReactDOM from 'react-dom';

import './index.css';
import './sidebarJsFile/main.js';
import './sidebarCssFile/sidebar.css';
import './weatherCssFile/css/weather-icons.min.css';
import './weatherCssFile/css/div.css';

import App from './home/Home';
import User from './home/HomeUserInfo';
import MyPage from './home/myPage';
/*import App from './App';*/
import reportWebVitals from './reportWebVitals';


ReactDOM.render(
  <User />,
  document.getElementById('userInfo')
);
ReactDOM.render(
  <MyPage />,
  document.getElementById('myPage')
);
ReactDOM.render(
    <App />,
    document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
