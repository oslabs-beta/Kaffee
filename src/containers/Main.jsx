import React, { useState, MouseEvent, useEffect } from 'react';
// import { useAppDispatch, useAppSelector } from '../redux/hooks.ts';
import { useSelector, useDispatch } from 'react-redux';
import Cluster from './Clusters.jsx';
import SearchBar from '../components/SearchBar.jsx';
import Chart from '../components/Chart.jsx';

// import type { ChartObj } from '../reducers/chartSlice.js';

export default function () {
  // const charts = useSelector((state: Array<ChartObj> ) => state.charts.list);
  const charts = useSelector((state) => state.charts.list);
  const status = useSelector((state) => state.charts.status);

  let spanText = 'Add A Chart';
  if (status === 'loading') {
    spanText = 'Loading New Chart';
  } else if (status === 'failed') {
    spanText = 'Failed to load chart, please check console';
  }

  return (
    <>
      {/* <Cluster /> */}
      <div id='metrics'>
        <div id='charts'>
          {charts?.map((chart, i) => {
            // console.log(chart);
            return (
              <Chart
                key={chart.metric}
                props={chart}
                id={chart.metric}
              />
            );
          })}
        </div>
      </div>
      <SearchBar />
    </>
  );
}
