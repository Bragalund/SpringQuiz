import React, {Component} from 'react';
import './css/App.css';
import Header from "./components/Util/Header";
import MyRouter from "./components/MyRouter";
import Footer from "./components/Util/Footer";

class App extends Component {
  render() {
    return (
        <div className="gray-light-light">
            <div className="container">
                <Header/>
                <MyRouter />
            </div>
            <Footer/>
        </div>
    );
  }
}

export default App;
