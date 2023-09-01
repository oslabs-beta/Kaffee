import React, {
  useState,
  MouseEvent,
  useEffect,
  useRef,
  useContext,
  useMemo,
} from 'react';
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
import {
  friendlyList,
  metricColors,
  preferredMetrics,
  chartOptionsInit as optionsInit,
  parseMetricName,
} from '../utils/metrics.js';
import { useSelector } from 'react-redux';
import { SocketContext } from '../App.jsx';

Chart.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

export default function (props) {
  let beginTime;
  let lastSentTime;

  let chartRef = useRef(null);
  const updatingData = useRef(false);

  // data and labels to display
  const [data, setData] = useState([]);
  const [labels, setLabels] = useState([]);

  // data and labels to store in history
  const [dataToSend, setDataToSend] = useState([]);
  const [labelsToSend, setLabelsToSend] = useState([]);

  const [status, setStatus] = useState('loading');

  // this is the number of data points to display per map
  const metricCount = useSelector((state) => state.charts.metricCount);
  const { client } = useContext(SocketContext);

  const options = useMemo(() => optionsInit, [options]);
  console.log(options);
  useEffect(() => {
    // Set the title based upon the list of friendly metric names
    // stored in '../utils/metrics
    options.plugins.title.text = friendlyList[props.metric];

    // if we have a props.data, we are loading from historical data
    if (Object.hasOwn(props, 'data')) {
      options.updateMode = 'none';

      const modifiedLabels = [];
      props.data.labels.forEach((label) => {
        const timeStamp = new Date(label);
        modifiedLabels.push(timeStamp.toLocaleTimeString('en-US'));
      });

      const modifiedDatasets = [];
      props.data.datasets.forEach((set, i) => {
        const rgb = metricColors[i];
        set['borderColor'] = `rgba(${rgb}, 0.6)`;
        set['backgroundColor'] = `rgba(${rgb}, 0.8)`;
      });

      setLabels(modifiedLabels);
      setData(props.data.datasets);
      setStatus('succeeded');
      return;
    }

    // if we don't have props.data, we should get here
    if (updatingData.current) return;
    updatingData.current = true;

    // subscribe to the socket route for this particular metric
    let path = '/metric/' + props.metric;
    client.subscribe(path, (message) => {
      addEvents(message);
    });

    // send a message to app/subscribe across the socket
    // to begin the scheduled tasks transmitting data for this metric
    client.publish({
      destination: '/app/subscribe',
      body: JSON.stringify({ metric: props.metric }),
    });
  }, []);

  function addEvents(message) {
    // this is used to make each line in the chart different
    // using the colors defined in ../utils.metricColors
    let colorInd = 0;
    console.log('in addEvents');

    const body = JSON.parse(message.body);

    // if the chart doesn't have a starting time,
    // set the time to the first time seen
    // using useState wasn't working for some reason
    // so that's why this is just a variable
    if (!beginTime) {
      beginTime = body.time;
      lastSentTime = Date.now();
    }
    const offsetTime = body.time - beginTime;
    // Add the current time offset to the labels
    labels.push(offsetTime);
    labelsToSend.push(body.time);

    // loop through everything the server gives us
    // display only values that are numeric
    for (const metric in body.snapshot) {
      // check typeof json.parse(value)
      const evalValue = Number(body.snapshot[metric]);
      if (isNaN(evalValue)) {
        // if the value is not numeric, move to the next metric
        continue;
      }

      // a flag to see if this metric is already being tracked
      let inData = false;
      let inDataToSend = false;

      // convert the Pascal cased metric name from Java to a more
      // user friendly variale. At this time, this function just adds
      // a space between lower and upper case adjacent letters
      let metricLabel = parseMetricName(metric);

      // look through the data as it currently exists
      for (const ind in data) {
        // if this object has a label matching the metric we are seeing
        const set = data[ind];
        if (set.label === metricLabel) {
          console.log(`Data at index: ${ind}`);
          console.log(`Is visible: ` + chartRef.current.isDatasetVisible(ind));
          // mark that we have this data
          inData = true;
          // add a new value to the data
          set.data.push(evalValue);
          while (set.data.length > metricCount) {
            set.data.shift();
          }
          if (!chartRef.current.isDatasetVisible(ind)) {
            set.hidden = true;
          }
          break;
        }
      }

      // look through the set of data to store in history
      for (const set of dataToSend) {
        if (!set.label || set.label === metricLabel) {
          inDataToSend = true;
          set.data.push(evalValue);
          break;
        }
      }

      // if the metric wasn't in the data object, let's add it
      if (!inData) {
        const rgb = metricColors[colorInd++];
        const newMetric = {
          data: [evalValue],
          label: metricLabel,
          borderColor: `rgba(${rgb}, 0.6)`,
          backgroundColor: `rgba(${rgb}, 0.8)`,
        };
        data.push(newMetric);
      }

      // if it wasn't in the set of data to store, add it
      if (!inDataToSend) {
        const newMetric = {
          data: [evalValue],
          label: metricLabel,
        };
        dataToSend.push(newMetric);
      }
    }

    // make sure the labels are the correct length
    while (labels.length > metricCount) {
      labels.shift();
    }

    if (status !== 'succeeded') setStatus('succeeded');
    setLabels([...labels]);
    setData([...data]);

    setLabelsToSend([...labelsToSend]);
    setDataToSend([...dataToSend]);

    if (Date.now() - lastSentTime > 1000) {
      pushToLog();
    }
  }

  function pushToLog() {
    const objToSend = {};
    objToSend[props.metric] = {
      labels: labelsToSend,
      datasets: dataToSend,
    };

    lastSentTime = Date.now();
    fetch('http://localhost:3030/addData', {
      method: 'POST',
      body: JSON.stringify(objToSend),
      headers: {
        'Content-type': 'application/json; charset=UTF-8',
      },
    })
      .then((res) => {
        if (res.ok) {
          setDataToSend([]);
          setLabelsToSend([]);
        }
      })
      .catch((err) => {
        // what do we do if the fetch fails?
        console.log(err);
      });
  }

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
          ref={chartRef}
        />
      )}
    </div>
  );
}

/*
  data object from Java server
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
  
  we want to store the data looking like:
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
    "isr-shrinks": {
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
