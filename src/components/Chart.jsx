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


import path from 'path';



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
  const [numCalls, setNumCalls] = useState(0);

  const [dataToSend, setDataToSend] = useState([]);
  const [labelsToSend, setLabelsToSend] = useState([]);
  let updating = false;
  // let dataToSend = [];
  // let labelsToSend = [];
  let lastSentTime = 0;

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
      lastSentTime = Date.now();
    }
    labels.push(body.time - beginTime);
    // console.log(body);

    // loop through everything the server gives us
    // display only values that are numeric
    for (const metric in body.snapshot) {
      // check typeof json.parse(value)
      const evalValue = Number(body.snapshot[metric]);
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
          // console.log(set)
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
    // TODO: set count, every 10 calls, call pushToLog

    if (!updating) {
      addDataToSend(body);
    }
    if (Date.now() - lastSentTime > 1000) {
      pushToLog()
    }
  }

  function addDataToSend(data) {
    updating = true;
    labelsToSend.push(data.time);
    setLabelsToSend([...labelsToSend]);

    for (const metric in data.snapshot) {
      // check typeof json.parse(value)
      const evalValue = Number(data.snapshot[metric]);
      if (isNaN(evalValue)) {
        continue;
      }

      // this is horribly messy
      let inData = false;
      // console.log(metric);
      let metricLabel = parseMetricName(metric);
      // console.log(data);
      for (const set of dataToSend) {
        if (!set.label || set.label === metricLabel) {
          inData = true;
          set.data.push(evalValue);
          // console.log(set)
          break;
        }
      }
      if (!inData) {
        const newMetric = {
          data: [evalValue],
          label: metricLabel,
        };
        // console.log(data);
        dataToSend.push(newMetric);
      }
    }
    // console.log(dataToSend);
    setDataToSend([...dataToSend]);
    
    updating = false;
  }

  /*
  data object argument in addDataToSend
  {
    "metric": "bytes-in",
    "time": 1693251471538,
    "snapshot": {
      "RateUnit": "SECONDS",
      "OneMinuteRate": "5.646250107634674E-43",
      "EventType": "bytes",
      "FifteenMinuteRate": "0.035843277946316754",
      "Count": "32300",
      "FiveMinuteRate": "1.0746448344638806E-7",
      "MeanRate": "4.6865081791319945"
    }
  }
  
  we want to send it looking like:
  {
    "bytes-in": {
      labels: [time1, time2, time3 ]
      datasets: [
        {
          label: OneMinuteRate,
          data[data1, data2, data3]
        }
      ]
    }
  }

  chartjs dataset object looks like:
  data={
    labels: [timestamps],
    datasets: [
      {
        data: Array<Numbers>,
        label: "metric",
        borderColor: something
      }
    ]      
  }
  
  */
  // console.log(data);
  function pushToLog() {
    const objToSend = {};
    objToSend[props.metric] = {
      labels: labelsToSend,
      datasets: dataToSend      
    }
    console.log(objToSend);

    if (!updating) {
      updating = true;
      lastSentTime = Date.now();
    fetch('http://localhost:3030/addData',
    { 
      method: 'POST',
      body: JSON.stringify(objToSend),
      headers: {
      'Content-type': 'application/json; charset=UTF-8',
    },})
    .then(response => {
      response.json();
      setDataToSend([]);
      setLabelsToSend([]);
    })
    .then(data => {
      console.log(data);
      updating = false;
    })
  }
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
