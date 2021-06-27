import React, { Component } from 'react';
import FilteredMultiSelect from 'react-filtered-multiselect';
import './StockMultiSelect.css';
import 'bootstrap/dist/css/bootstrap.min.css'

const CULTURE_SHIPS = [
    {id: 1, name: 'AAPL (Apple)'},
    {id: 2, name: 'FB (Facebook)'},
  // ...
    {id: 249, name: 'MSFT (Microsoft)'},
    {id: 250, name: 'PINS (Pinterest)'}
  ]

  const BOOTSTRAP_CLASSES = {
    filter: 'form-control',
    select: 'form-control',
    button: 'btn btn btn-block btn-default',
    buttonActive: 'btn btn btn-block btn-primary',
  }

class StockMultiSelect extends Component {
    state = {
        selectedOptions: []
      }
    
      handleDeselect = (deselectedOptions) => {
        var selectedOptions = this.state.selectedOptions.slice()
        deselectedOptions.forEach(option => {
          selectedOptions.splice(selectedOptions.indexOf(option), 1)
        })
        this.setState({selectedOptions})
      }
      handleSelect = (selectedOptions) => {
        selectedOptions.sort((a, b) => a.id - b.id)
        this.setState({selectedOptions})
      }
    
      render() {
        var {selectedOptions} = this.state
        return <div className="row">
          <div className="col-md-5 no-gutters">
            <FilteredMultiSelect
              buttonText="Add"
              classNames={BOOTSTRAP_CLASSES}
              onChange={this.handleSelect}
              options={CULTURE_SHIPS}
              selectedOptions={selectedOptions}
              textProp="name"
              valueProp="id"
            />
          </div>
          <div className="col-md-5 no-gutters">
            <FilteredMultiSelect
              buttonText="Remove"
              classNames={{
                filter: 'form-control',
                select: 'form-control',
                button: 'btn btn btn-block btn-default',
                buttonActive: 'btn btn btn-block btn-danger'
              }}
              onChange={this.handleDeselect}
              options={selectedOptions}
              textProp="name"
              valueProp="id"
            />
          </div>
        </div>
      }
}

export default StockMultiSelect;