import React, { useState, MouseEvent } from 'react';
// import { useAppDispatch, useAppSelector } from '../hooks/hooks.js';
import { useSelector, useDispatch } from 'react-redux';
import Cluster from './Clusters.jsx';
import SearchBar from '../components/SearchBar.jsx';
import Chart from '../components/Chart.jsx';
import { addChart, removeChart, newChart } from '../reducers/chartSlice.js';

// import type { ChartObj } from '../reducers/chartSlice.js';

export default function () {
  // const charts = useSelector((state: Array<ChartObj> ) => state.charts.list);
  const charts = useSelector((state) => state.charts.list);
  const status = useSelector((state) => state.charts.status);
  console.log(charts);
  console.log(status);

  // const dispatch = useAppDispatch();
  const dispatch = useDispatch();

  const handleAddChart = () => {
    if (status === 'succeeded' || status === 'idle') {
      dispatch(newChart());
    }
  };

  return (
    <div id='main'>
      {/* <Cluster /> */}
      <div id='metrics'>
        <div id='charts'>
          {charts?.map((chart, i) => {
            return (
              <Chart
                key={`Chart_${i}`}
                props={chart}
              />
            );
          })}
          <div
            className='chartCanvas'
            onClick={handleAddChart}
            id='add-chart'
          >
            <span>Add A Chart</span>
          </div>
        </div>
        <SearchBar />
      </div>
    </div>
  );
}
