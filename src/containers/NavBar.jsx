import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { NavLink, Outlet } from 'react-router-dom';
import { addChart, removeChart } from '../reducers/chartSlice.js';
import client from '../socket.js';

async function getMetricsList() {
  const res = await fetch('http://localhost:8080/available-server-metrics');
  const metricListRaw = await res.json();

  const metricList = metricListFriendly(metricListRaw);
  return metricList;
}

// Generalize this here, in case we want to use it elsewhere.
// This may be better moved to a common place where we can use it
// across the whole application
function metricListFriendly(metricList) {
  const returnList = [];
  const friendlyList = {
    'bytes-in': 'Server Bytes In',
    'isr-shrinks': 'Rate of In Sync Replica Shrinking',
    'offline-partitions-count': 'Number of Offline Partitions',
    'isr-expands': 'Rate of In Sync Replica Expanding',
    'leader-election-rate': 'Leader Election Rate',
    'bytes-out': 'Server Bytes Out',
    'active-controller-count': 'Number of Active Controllers',
    'under-replicated-partitions': 'Number of Under Replicated Partitions',
    'unclean-leader-selection': 'Number of Unclean Leader Elections per Second',
  };

  // here we loop through the metrics list that we get from the java server
  // if we have a friendly name for it, we add it to the list
  // this way we don't add anything to the list that isn't ready to be displayed
  // in a user friendly manner
  for (const metric of metricList) {
    if (Object.hasOwn(friendlyList, metric)) {
      returnList.push({
        name: metric,
        display: friendlyList[metric],
      });
    }
  }

  return returnList;
}

export default function NavBar() {
  const [metrics, setMetrics] = useState([]);

  const chartList = useSelector((state) => state.charts.list);

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
          <button>Choose Metrics</button>
          <div id='metric-picker'>
            <form>
              {metrics?.map((metric) => {
                return (
                  <label className='chart-selector'>
                    <input
                      type='checkbox'
                      id={metric.name}
                      name={metric.name}
                      defaultValue={chartList[metric]}
                      key={metric.name}
                      onClick={() => handleToggleChart(metric.name)}
                    />
                    {metric.display}
                  </label>
                );
              })}
            </form>
          </div>
        </nav>
        <nav className='right'>
          {/* <NavLink to='/socket'>
            <button>Socket Example</button>
          </NavLink> */}
        </nav>
      </header>
    </div>
  );
}
