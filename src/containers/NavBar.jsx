import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { NavLink, useLocation } from 'react-router-dom';
import { addChart, removeChart } from '../reducers/chartSlice.js';
import client from '../utils/socket.js';
import { metricListFriendly } from '../utils/metrics.js';

export async function loader() {
  try {
    const res = await fetch('http://localhost:8080/available-server-metrics');

    const metricListRaw = await res.json();

    const metricList = metricListFriendly(metricListRaw);
    return metricList;
  } catch (err) {
    throw new Error(
      `Could not connect to the Java server. 
      Please verify that the Java server is running.`,
      // @ts-ignore
      {
        cause: err,
      },
    );
  }
}

export default function NavBar() {
  const [run, setRun] = useState(false);
  const [runningCharts, setRunningCharts] = useState({});

  const chartList = useSelector((state) => state.charts.list);
  const location = useLocation();

  const dispatch = useDispatch();

  useEffect(() => {
    const currentlyRunning = {};
    chartList.forEach((chart) => {
      const metric = chart.metric;
      currentlyRunning[metric] = true;
    });
    setRunningCharts(currentlyRunning);
  }, [chartList]);

  const [metrics, setMetrics] = useState(null);
  useEffect(() => {
    async function getMetricsList() {
      const metrics = await loader();
      setMetrics(metrics);
    }

    getMetricsList();
  }, []);

  function handleToggleChart(metricId) {
    const checkbox = document.querySelector(`#${metricId}`);
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
      fetch('http://localhost:8080/test/runTest', { mode: 'no-cors' });
      setRun(true);
    } else {
      fetch('http://localhost:8080/test/stopTest', { mode: 'no-cors' });
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
    <div className="nav-bar">
      <header>
        <nav className="left">
          <NavLink to="/">
            <button>Home</button>
          </NavLink>
          <NavLink to="/history">
            <button>History</button>
          </NavLink>
          <NavLink to="/settings">
            <button>Settings</button>
          </NavLink>
          {location.pathname === '/' ? (
            <div id="metric-picker">
              <div>
                <button onClick={showMetrics}>Choose Metrics</button>
              </div>
              <nav id="metric-list" onMouseLeave={showMetrics}>
                {metrics?.map((metric) => {
                  return (
                    <label className="chart-selector" key={metric.name}>
                      <input
                        type="checkbox"
                        id={metric.name}
                        name={metric.name}
                        checked={runningCharts[metric.name]}
                        onClick={() => handleToggleChart(metric.name)}
                      />
                      {metric.display}
                    </label>
                  );
                })}
              </nav>
            </div>
          ) : (
            <></>
          )}
        </nav>
        <nav className="right">
          <NavLink to="/about">
            <button>About</button>
          </NavLink>
          <button data-testid="startStopProducers" onClick={startProducers}>
            {run ? 'Stop' : 'Start'} Test Producers
          </button>
        </nav>
      </header>
    </div>
  );
}
