import React from 'react';
import ReactDOM from 'react-dom';

// add css here...
import './css/index.css'
import './css/Header.css'
import './css/Colors.css'
import './css/Footer.css'
import './css/Alignment.css'
import { BrowserRouter } from 'react-router-dom'

/* eslint-enable no-unused-vars */
import App from './App';
import registerServiceWorker from './registerServiceWorker';

ReactDOM.render(
    <BrowserRouter>
        <App/>
    </BrowserRouter>
    , document.getElementById('root'));
registerServiceWorker();
