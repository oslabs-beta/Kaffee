import React, { useState, MouseEvent } from 'react';
// import { useAppDispatch, useAppSelector } from '../redux/hooks.ts';
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

  // const dispatch = useAppDispatch();
  const dispatch = useDispatch();

  const handleAddChart = () => {
    if (status === 'succeeded' || status === 'idle') {
      dispatch(newChart());
    }
  };

  let spanText = 'Add A Chart';
  if (status === 'loading') {
    spanText = 'Loading New Chart';
  } else if (status === 'failed') {
    spanText = 'Failed to load chart, please check console';
  }

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
            <span>{spanText}</span>
          </div>
        </div>
      </div>
      <SearchBar />
    </div>
  );
}
