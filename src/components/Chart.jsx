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
import client from '../utils/socket.js';
import {
  friendlyList,
  metricColors,
  preferredMetrics,
} from '../utils/metrics.js';

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

function parseMetricName(metricName) {
  return metricName.replace('/([A-Z])/g', ' $1');
}

export default function ({ props }) {
  const [events, setEvents] = useState([]);
  const [labels, setLabels] = useState([]);
  const [title, setTitle] = useState('');
  let beginTime;

  let data = {
    labels: labels,
    datasets: [],
    colorInd: 0,
  };

  useEffect(() => {
    client.onConnect = () => {
      let path = '/metric/' + props.metric;
      client.subscribe(path, (message) => (data = addEvent(message)));

      options.plugins.title.text = friendlyList[title];

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

  // handle different data values
  function addEvents(message) {
    const body = JSON.parse(message.body);
    // loop through everything the server gives us
    // display only values that are numeric
    for (const metric in body.snapshot) {
      // check typeof json.parse(value)
      const evalValue = Number(body.snapshot[metric]);
      if (typeof evalValue === 'NaN' || typeof evalValue === NaN) {
        continue;
      }

      // this is horrible, and we still have NaN returns
      console.log(`Metric: ${metric} | Value: ${evalValue}`);
      const inData = false;
      for (const set in data.datasets) {
        if (set.metric === metric) {
          inData = true;
          set.events.push(evalValue);

          while (events.length > 30) {
            events.shift();
            labels.shift();
          }
          break;
        }
      }
      if (!inData) {
        data.datasets.push({
          metric: metric,
          data: [evalValue],
          label: parseMetricName(metric),
          borderColor: metricColors[data.colorInd++],
        });
      }

      // add it to the events array at the appropriate place
      for (const set in data.datasets) {
      }
    }
    return data;
  }

  function addEvent(message) {
    const body = JSON.parse(message.body);
    if (!beginTime) {
      beginTime = body.time;
    }

    setTitle(parseMetricName(body.metric));
    console.log(body);
    const metric = preferredMetrics[body.metric];
    const event = body.snapshot.metric;

    labels.push(body.time - beginTime);

    while (events.length > 30) {
      events.shift();
      labels.shift();
    }

    setEvents([...events]);
    setLabels([...labels]);
  }
  console.log(data);

  return (
    <div className='chartCanvas'>
      <Line
        datasetIdKey='id'
        options={options}
        data={data}
      />
    </div>
  );
}
