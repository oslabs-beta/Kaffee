import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { NavLink, Outlet } from 'react-router-dom';
import { addChart, removeChart } from '../reducers/chartSlice.js';
import client from '../utils/socket.js';
import { metricListFriendly } from '../utils/metrics.js';

async function getMetricsList() {
  const res = await fetch('http://localhost:8080/available-server-metrics');
  const metricListRaw = await res.json();

  const metricList = metricListFriendly(metricListRaw);
  return metricList;
}

export default function NavBar() {
  const [metrics, setMetrics] = useState([]);
  const [run, setRun] = useState(false);

  const chartList = useSelector((state) => state.charts.list);
  const metricCount = useSelector((state) => state.charts.metricCount);

  const dispatch = useDispatch();

  useEffect(() => {
    async function getMetrics() {
      const metrics = await getMetricsList();
      setMetrics(metrics);
    }

    getMetrics();
  }, []);

  function handleToggleChart(metricId) {
    const checkbox = document.querySelector(`#${metricId}`);
    // console.log(checkbox);
    if (checkbox.checked) {
      dispatch(addChart(metricId));
    } else {
      dispatch(removeChart(metricId));
      client.publish({
        destination: '/app/unsubscribe',
        body: JSON.stringify({ metric: metricId }),
      });
    }
  }

  function startProducers() {
    if (!run) {
      fetch('http://localhost:3030/test', { mode: 'no-cors' });
      setRun(true);
    } else {
      fetch('http://localhost:3030/stopTest', { mode: 'no-cors' });
      setRun(false);
    }
  }

  function showMetrics() {
    const metricsBox = document.getElementById('metric-list');
    let currDisplay = metricsBox.style.display;
    if (!currDisplay) {
      currDisplay = 'none';
    }
    metricsBox.style.display = currDisplay === 'none' ? 'block' : 'none';
  }

  return (
    <div className='nav-bar'>
      <header>
        <nav className='left'>
          <NavLink to='/'>
            <button>Home</button>
          </NavLink>
          <NavLink to='/history'>
            <button>History</button>
          </NavLink>
          <NavLink to='/settings'>
            <button>Settings</button>
          </NavLink>
          <div id='metric-picker'>
            <div>
              <button onClick={showMetrics}>Choose Metrics</button>
            </div>
            <nav
              id='metric-list'
              onMouseLeave={showMetrics}
            >
              {metrics?.map((metric) => {
                return (
                  <label
                    className='chart-selector'
                    key={metric.name}
                  >
                    <input
                      type='checkbox'
                      id={metric.name}
                      name={metric.name}
                      defaultValue={chartList[metric]}
                      onClick={() => handleToggleChart(metric.name)}
                    />
                    {metric.display}
                  </label>
                );
              })}
            </nav>
          </div>
        </nav>
        <nav className='right'>
          <button onClick={startProducers}>
            {run ? 'Stop' : 'Start'} Producers
          </button>
        </nav>
      </header>
    </div>
  );
}
