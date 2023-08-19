import React, { useState, MouseEvent } from 'react';
// import { useAppDispatch, useAppSelector } from '../hooks/hooks.js';
import { useSelector, useDispatch } from 'react-redux';
import Cluster from './Clusters.jsx';
import SearchBar from '../components/SearchBar.jsx';
import { addChart, removeChart } from '../reducers/chartSlice.js';

// import type { ChartObj } from '../reducers/chartSlice.js';

export default function () {
  const [search, setSearch] = useState('');

  // const charts = useSelector((state: Array<ChartObj> ) => state.charts.list);
  const charts = useSelector((state) => state.charts.list);

  // const dispatch = useAppDispatch();
  const dispatch = useDispatch();

  return (
    <div id='main'>

      <Cluster />
      <div id='metrics'>
        <div id='charts'>
          {charts.length ? (
            charts.map((chart) => {
              // do something with chart.js
            })
          ) : (
            <></>
          )}
          <div class='canvas'>Add A Chart</div>
        </div>
        <SearchBar />
      </div>
    </div>
  );
}
