import React, { useState, MouseEvent, useEffect } from 'react';
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
// import { useAppDispatch, useAppSelector } from '../redux/hooks.ts';
import { useSelector, useDispatch } from 'react-redux';
import chartSlice from '../reducers/chartSlice.js';
import client from '../socket.js';

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
      text: '',
    },
  },
  updateMode: 'active',
};

export const loader = () => {};

export default function ({ props }) {
  const [events, setEvents] = useState([]);
  const [labels, setLabels] = useState([]);
  const [title, setTitle] = useState('');
  const [startTime, setStartTime] = useState(0);
  let beginTime;

  useEffect(() => {
    console.log(props);
    client.onConnect = () => {
      // let path = '/metric/chuck';
      let path = '/metric/' + props.metric;
      console.log(path);
      console.log(client);
      client.subscribe(path, (message) => addEvent(message))
    };
    client.activate();
    
    return () => {
      client.deactivate();
    }
  }, []);

  const dataSet = {
    labels: labels,
    datasets: [
      { label: title, data: events, borderColor: 'rgb(255, 0, 0)' },
    ],
  };

  function addEvent(message) {
    const body = JSON.parse(message.body);
    if (!beginTime) {
      beginTime = body.time;
    }

    setTitle(body.metric);
    console.log(body);
    const event = body.snapshot.MeanRate;
    events.push(event);

    labels.push(body.time - beginTime);

    while (events.length > 30) {
      events.shift();
      labels.shift();
    }

    setEvents([...events]);
    setLabels([...labels]);
  }


  options.plugins.title.text = title;

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
