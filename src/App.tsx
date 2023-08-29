import React, { useEffect } from 'react';
import { Outlet, useLoaderData } from 'react-router-dom';
import NavBar from './containers/NavBar.jsx';
import client from './utils/socket.js';
import { changeMetricCount } from './reducers/chartSlice.js';
import { useDispatch } from 'react-redux';

export async function loader() {
  try {
    const res = await fetch('http://localhost:3030/getSettings');
    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error);
    throw new Error('Error fetching settings', { cause: error });
  }
}

export default function App() {
  const { settings } = useLoaderData();
  console.log(settings);
  const dispatch = useDispatch();

  useEffect(() => {
    if (typeof settings === 'object') {
      dispatch(changeMetricCount(settings['metric-count']));
    }
  }, []);

  return (
    <>
      <NavBar />
      <div id='main'>
        <Outlet />
      </div>
    </>
  );
}
