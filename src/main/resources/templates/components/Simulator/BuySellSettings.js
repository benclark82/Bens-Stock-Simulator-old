import React, { Component } from 'react';
import "./BuySellSettings.css";

class BuySettings extends Component {

    constructor(props) {
        super(props);
        this.state = {value: ''};
    
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({value: event.target.value});
    }

    handleSubmit(event) {
        alert('A name was submitted: ' + this.state.value);
        event.preventDefault();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    <div className="buy-sell-line">
                        <input
                            name="previousPeriod"
                            type="checkbox"
                            checked={this.state.previousPeriod}
                            onChange={this.handleInputChange}>
                        </input>
                        {' '}Previous{' '} 
                        <select>
                            <option value="day">Day</option>
                            <option value="week">Week</option>
                            <option value="month">Up</option>
                        </select>
                        {' '}was{' '}
                        <select>
                            <option value="up">Up</option>
                            <option value="down">down</option>
                        </select>
                    </div>

                    <div className="buy-sell-line">
                        <input
                            name="harami"
                            type="checkbox"
                            checked={this.state.harami}
                            onChange={this.handleInputChange}>
                        </input>
                        <select>
                            <option value="bearishHarami">Bearish</option>
                            <option value="bullishHarami">Bullish</option>
                        </select>
                        {' '}harami{' '}
                    </div>

                    <div className="buy-sell-line">
                        <input
                            name="harami"
                            type="checkbox"
                            checked={this.state.harami}
                            onChange={this.handleInputChange}>
                        </input>
                        <select>
                            <option value="bearishHarami">Bearish</option>
                            <option value="bullishHarami">Bullish</option>
                        </select>
                        {' '}Engulfing
                    </div>
                </label>
            </form>
        );
    }
}

export default BuySettings;