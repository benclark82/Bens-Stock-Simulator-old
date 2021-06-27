import React, { Component } from 'react';
import StockMultiSelect from './StockMultiSelect';
import BuySellSettings from './BuySellSettings';
import DatePicker from 'react-date-picker';
import './StockSimulator.css';

class StockSimulator extends Component {
    handleClick() {};

render() {
    return(
        <stock-simulator>
            <header>
                Date Range:{' '}
                <DatePicker />
                {' '}To{' '}
                <DatePicker />
            </header>

            <section>
                
                <stock-select-list>
                    <h5>Stocks To Simulate:</h5>
                    <StockMultiSelect />
                </stock-select-list>

                <buy-settings>
                    <h5>Buy Settings:</h5>
                    <BuySellSettings />
                </buy-settings>


                <sell-settings>
                    <h5>Sell Settings:</h5>
                    <BuySellSettings />
                </sell-settings>
            </section>

            <footer>
                <button onClick={this.handleClick}>
                    Run
                </button>
            </footer>
        </stock-simulator>

    );

}
}

export default StockSimulator;