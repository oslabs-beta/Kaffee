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
  Filler,
} from 'chart.js';
// import { useAppDispatch, useAppSelector } from '../redux/hooks.ts';
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

function parseMetricName(metricName) {
  return metricName.replace(/([a-z])([A-Z])/g, '$1 $2');
}

export default function ({ props }) {
  let colorInd = 0;
  let beginTime;

  const optionsInit = {
    responsive: true,
    type: 'line',
    plugins: {
      legend: {
        position: 'bottom',
      },
      title: {
        display: true,
        text: '',
      },
    },
    updateMode: 'active',
  };

  const [data, setData] = useState([]);
  const [labels, setLabels] = useState([]);
  const [options, setOptions] = useState(optionsInit);

  useEffect(() => {
    const whenConnected = () => {
      let path = '/metric/' + props.metric;
      client.subscribe(path, (message) => addEvents(message));

      const newOptions = Object.assign({}, options);
      newOptions.plugins.title.text = friendlyList[props.metric];
      setOptions(newOptions);

      client.publish({
        destination: '/app/subscribe',
        body: JSON.stringify({ metric: props.metric }),
      });
    };

    if (client.active) {
      whenConnected();
    } else {
      client.onConnect(whenConnected);
      client.activate();
    }
  }, []);

  // handle different data values
  // TODO: Refactor and clean this up
  function addEvents(message) {
    const body = JSON.parse(message.body);
    // console.log(data);
    // const data = data.slice();
    // const labels = labels.slice();

    if (!beginTime) {
      beginTime = body.time;
    }
    labels.push(body.time - beginTime);
    // console.log(body);

    // loop through everything the server gives us
    // display only values that are numeric
    for (const metric in body.snapshot) {
      // check typeof json.parse(value)
      const evalValue = parseInt(body.snapshot[metric]);
      if (isNaN(evalValue)) {
        continue;
      }

      // this is horribly messy
      let inData = false;
      // console.log(metric);
      let metricLabel = parseMetricName(metric);
      // console.log(data);
      for (const set of data) {
        if (!set.label || set.label === metricLabel) {
          inData = true;
          set.data.push(evalValue);
          console.log(set)
          while (set.data.length > 10) {
            set.data.shift();
          }
          break;
        }
      }
      while (labels.length > 10) {
        labels.shift()
      }
      if (!inData) {
        const newMetric = {
          data: [evalValue],
          label: metricLabel,
          borderColor: metricColors[colorInd++],
        };
        data.push(newMetric);
        // console.log(data);
      }
    }
    setLabels([...labels]);
    setData([...data]);
  }

  // console.log(data);
  // console.log(labels);
  // console.log(options);

  return (
    <div className='chartCanvas'>
      {labels.length ? (
        <Line
          datasetIdKey={props.metric}
          options={options}
          data={{
            labels: labels,
            datasets: data,
          }}
          id={props.metric}
        />
      ) : (
        <></>
      )}
    </div>
  );
}
