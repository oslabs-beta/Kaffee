import React, { useState, useEffect, useRef } from 'react';
import Chart from './Chart.jsx';

// TODO:
// import history folder
// parse names of json files by date
// create chartjs from data on expand

async function getData(filename) {
  const res = await fetch(`http://localhost:3030/getData/${filename}`);
  if (res.ok) {
    const data = await res.json();
    console.log(data);

    return data;
  } else {
    return 'The data cannot be read. Please check the logs to verify they are a valid JSON';
  }
}

function parseDate(filename) {
  try {
    const dateRegex = /^(\d{4})-(\d{2})-(\d{2})/;
    const dateArr = filename.match(dateRegex);
    // we need to remove a month from the date as saved in our History folder
    // since months in Date() go from 0-11
    const date = new Date(dateArr[1], dateArr[2] - 1, dateArr[3]);

    const month = date.toLocaleString();
    const day = date.getDate();

    return date.toLocaleString('en-US', {
      month: 'long',
      year: 'numeric',
      day: 'numeric',
    });
  } catch (err) {
    return String(filename);
  }
}

export default function ({ data: filename }) {
  const [expanded, setExpanded] = useState(false);
  const metrics = useRef(null);

  const clickHandler = async () => {
    if (!expanded) {
      metrics.current = await getData(filename);
    }

    setExpanded(!expanded);
  };

  const label = parseDate(filename);
  return (
    <div className='history-item-container'>
      <div className='history-date'>{label}</div>
      <button
        className='expand-button'
        onClick={clickHandler}
      >
        {expanded ? 'Collapse' : 'Expand'}
      </button>
      {expanded && (
        <div className='expanded-metrics'>
          {typeof metrics.current === 'string'
            ? metrics.current
            : Object.entries(metrics.current).map(([metric, metricData]) => {
                return (
                  <Chart
                    key={metric}
                    metric={metric}
                    data={metricData}
                  />
                );
              })}
        </div>
      )}
    </div>
  );
}
