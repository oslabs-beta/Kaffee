import React, { useState, MouseEvent } from 'react';
import { Line } from 'react-chartjs-2';
import {
  Chart,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
// import { useAppDispatch, useAppSelector } from '../hooks/hooks.js';
import { useSelector, useDispatch } from 'react-redux';
import chartSlice from '../reducers/chartSlice.js';

/*
const DATA_LENGTH = 100;
const labels = Array.from({ length: DATA_LENGTH }, (_, i) => (i + 1) * 100);

const generateData = () => {
  return Math.floor(Math.random() * 1000);
};

const mockData = {
  labels,
  datasets: [
    {
      id: 1,
      label: 'Metric 1',
      data: labels.map(generateData),
      borderColor: 'rgb(255, 0, 0)',
      backgroundColor: 'rgba(255, 0, 0)',
    },
    {
      id: 2,
      label: 'Metric 2',
      data: labels.map(generateData),
      borderColor: 'rgb(0, 255, 0)',
      backgroundColor: 'rgb(0, 255, 0)',
    },
    {
      id: 3,
      label: 'Metric 3',
      data: labels.map(generateData),
      borderColor: 'rgb(0, 0, 255)',
      backgroundColor: 'rgb(0, 0, 255)',
    },
  ],
};
*/

Chart.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

export const options = {
  responsive: true,
  plugins: {
    legend: {
      position: 'top',
    },
    title: {
      display: true,
      text: 'Mock Data',
    },
  },
  updateMode: 'active',
};

export const loader = () => {};

export default function ({ props }) {
  const dataSet = {
    labels: Array.from({ length: props.data.length }, (_, i) => (i + 1) * 100),
    datasets: [
      { label: props.name, data: props.data, borderColor: 'rgb(255, 0, 0)' },
    ],
  };

  return (
    <div className='chartCanvas'>
      <Line
        datasetIdKey='id'
        options={options}
        data={dataSet}
      />
    </div>
  );
}
