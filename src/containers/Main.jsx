import React, { useState, MouseEvent, useEffect } from 'react';
// import { useAppDispatch, useAppSelector } from '../redux/hooks.ts';
import { useSelector, useDispatch } from 'react-redux';
import Cluster from './Clusters.jsx';
import SearchBar from '../components/SearchBar.jsx';
import Chart from '../components/Chart.jsx';
import client from '../utils/socket.js';

// used in creating a test chart
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler,
  registerables,
} from 'chart.js';

// import type { ChartObj } from '../reducers/chartSlice.js';

export default function () {
  // const charts = useSelector((state: Array<ChartObj> ) => state.charts.list);
  const charts = useSelector((state) => state.charts.list);

  useEffect(() => {
    try {
      client.activate();
    } catch (error) {
      throw error;
    }

    return () => {
      client.deactivate();
    };
  }, []);

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
                metric={chart.metric}
                id={chart.metric}
              />
            );
          })}
        </div>
      </div>
      {/* <SearchBar /> */}
    </>
  );
}
