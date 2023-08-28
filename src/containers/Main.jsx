import React, { useState, MouseEvent, useEffect } from 'react';
// import { useAppDispatch, useAppSelector } from '../redux/hooks.ts';
import { useSelector, useDispatch } from 'react-redux';
import Cluster from './Clusters.jsx';
import SearchBar from '../components/SearchBar.jsx';
import Chart from '../components/Chart.jsx';

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
  const status = useSelector((state) => state.charts.status);

  // used in creating a test chart
  const [data, setData] = useState([]);
  const [labels, setLabels] = useState([]);

  let spanText = 'Add A Chart';
  if (status === 'loading') {
    spanText = 'Loading New Chart';
  } else if (status === 'failed') {
    spanText = 'Failed to load chart, please check console';
  }

  const gridColor = '192, 152, 106, .6';
  const toolTipColor = `222, 215, 217`;
  const options = {
    responsive: true,
    type: 'line',
    plugins: {
      legend: {
        position: 'bottom',
      },
      title: {
        display: true,
        text: 'Test',
      },
      tooltip: {
        titleColor: `rgba(${toolTipColor}, .8)`,
        bodyColor: `rgba(${toolTipColor}, .6)`,
      },
    },
    scales: {
      x: {
        grid: {
          color: `rgba(${gridColor})`,
        },
        border: {
          color: `rgba(${gridColor})`,
        },
      },
      y: {
        grid: {
          color: `rgba(${gridColor})`,
        },
        border: {
          color: `rgba(${gridColor})`,
        },
      },
    },
    updateMode: 'active',
  };

  // used in creating a test chart
  // useEffect(() => {
  //   const getData = async () => {
  //     const defaultData = {
  //       backgroundColor: 'rgba(255, 0, 0, .8)',
  //       borderColor: 'rgba(255, 0, 0, .6)',
  //       fill: true,
  //     };

  //     const res = await fetch('http://localhost:3030/dummy/10');
  //     const data = await res.json();
  //     setData(Object.assign(defaultData, data));

  //     const labels = data.data?.map((_, i) => (i + 1) * 100);
  //     setLabels(labels);
  //   };
  //   getData();
  // }, []);

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
          {/* <div className='chartCanvas'>
            <Line
              options={options}
              data={{
                labels: labels,
                datasets: [data],
              }}
            />
          </div> */}
        </div>
      </div>
      <SearchBar />
    </>
  );
}
