import React, { Component } from 'react';
import StockSimulator from '../Simulator/StockSimulator';
import './MainContent.css';

class MainContent extends Component {

    render() {
        return(
            <main-content-items>
                <StockSimulator />
            </main-content-items>
        )
    }
}

export default MainContent;