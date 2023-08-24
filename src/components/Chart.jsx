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

// handle different data values
function getValues(snapshot) {
  const dataObj = {
    metric: '',
    value: null,
  };

  // loop through everything the server gives us
  // display only values that are numeric
  for (const [metric, value] of snapshot) {
    // check typeof json.parse(value)
    // add it to the events array at the appropriate place
  }
}

export default function ({ props }) {
  const [events, setEvents] = useState([]);
  const [labels, setLabels] = useState([]);
  const [title, setTitle] = useState('');
  let beginTime;

  useEffect(() => {
    console.log(props);
    client.onConnect = () => {
      let path = '/metric/' + props.metric;
      client.subscribe(path, (message) => addEvent(message));

      client.publish({
        destination: '/app/subscribe',
        body: JSON.stringify({ metric: props.metric }),
      });
    };
    client.activate();

    // on initialization, determine the number of charts to draw

    return () => {
      client.deactivate();
    };
  }, []);

  const dataSet = {
    labels: labels,
    datasets: [{ label: title, data: events, borderColor: 'rgb(255, 0, 0)' }],
  };

  function addEvent(message) {
    const body = JSON.parse(message.body);
    if (!beginTime) {
      beginTime = body.time;
    }

    setTitle(body.metric);
    console.log(body);
    const event = body.snapshot.OneMinuteRate;

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
