import React, { useState, MouseEvent, useEffect, useRef } from 'react';
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
import { useSelector } from 'react-redux';

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

// since I can't seem to assign these in the CSS file
// these are colors imported from the CSS. If they change there,
// we probably want to update these
const gridColor = '192, 152, 106, .6';
const toolTipColor = `222, 215, 217`;
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

export default function ({ props }) {
  let beginTime;

  const [data, setData] = useState([]);
  const [labels, setLabels] = useState([]);
  const [options, setOptions] = useState(optionsInit);
  const [status, setStatus] = useState('loading');

  // this is the number of data points to display per map
  const metricCount = useSelector((state) => state.charts.metricCount);

  useEffect(() => {
    const whenConnected = () => {
      // subscribe to the socket route for this particular metric
      let path = '/metric/' + props.metric;
      client.subscribe(path, (message) => addEvents(message));

      // Set the title based upon the list of friendly metric names
      // stored in '../utils/metrics
      options.plugins.title.text = friendlyList[props.metric];
      setOptions(options);

      // send a message to app/subscribe across the socket
      // to begin the scheduled tasks transmitting data for this metric
      client.publish({
        destination: '/app/subscribe',
        body: JSON.stringify({ metric: props.metric }),
      });
    };

    if (client.active) {
      // if there is already an active connection
      // invoke the above function
      whenConnected();
    } else {
      // if there is no active connection, we add the above function
      // to the callback to occur after connecting
      // and then connect
      client.onConnect(whenConnected);
      client.activate();
    }
  }, []);

  // handle different data values

  // this should be doable with a pointer
  // while we don't have keys to check, I believe the data will come in
  // in the same order every time. We should verify this
  function addEvents(message) {
    let colorInd = 0;
    const body = JSON.parse(message.body);

    // if the chart doesn't have a starting time,
    // set the time to the first time seen
    // using useState wasn't working for some reason
    // so that's why this is just a variable
    if (!beginTime) {
      beginTime = body.time;
    }
    // Add the current time offset to the labels
    labels.push(body.time - beginTime);

    // loop through everything the server gives us
    // display only values that are numeric
    for (const metric in body.snapshot) {
      // Parse the value into an integer
      const evalValue = parseInt(body.snapshot[metric]);
      if (isNaN(evalValue)) {
        // if the value is not numeric, move to the next metric
        continue;
      }

      // a flag to see if this metric is already being tracked
      let inData = false;

      // convert the Pascal cased metric name from Java to a more
      // user friendly variale. At this time, this function just adds
      // a space between lower and upper case adjacent letters
      let metricLabel = parseMetricName(metric);

      // look through the data as it currently exists
      for (const set of data) {
        // console.log(set);
        // if the data has an object without a label (which should only be when data is first streaming)
        // or if this object has a label matching the metric we are seeing
        if (set.label === metricLabel) {
          // mark that we have this data
          inData = true;
          // add a new value to the data
          set.data.push(evalValue);

          while (set.data.length > metricCount) {
            set.data.shift();
          }
          break;
        }
      }

      // if we didn't find matching data
      // add a new data metric object
      if (!inData) {
        const newMetric = {
          data: [evalValue],
          label: metricLabel,
          borderColor: `rgba(${metricColors[colorInd++]}, 0.6)`,
          backgroundColor: `rgba(${metricColors[colorInd++]}, 0.8)`,
        };
        data.push(newMetric);
      }
    }

    while (labels.length > metricCount) {
      labels.shift();
    }

    if (status !== 'succeeded') setStatus('succeeded');
    setLabels([...labels]);
    setData([...data]);
  }

  // console.log(data);
  // console.log(labels);
  // console.log(options);

  return (
    <div className='chartCanvas'>
      {status === 'loading' ? (
        <span>Loading Chart</span>
      ) : (
        <Line
          datasetIdKey={props.metric}
          options={options}
          data={{
            labels: labels,
            datasets: data,
          }}
          id={props.metric}
        />
      )}
    </div>
  );
}
