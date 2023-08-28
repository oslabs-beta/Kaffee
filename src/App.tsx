import React, { useEffect } from 'react';
import { Outlet } from 'react-router-dom';
import NavBar from './containers/NavBar.jsx';
import client from './utils/socket.js';

export default function App() {
  useEffect(() => {
    client.activate();

    return () => {
      client.deactivate();
    };
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
