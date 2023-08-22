import React from 'react';
import { Outlet } from 'react-router-dom';
import NavBar from './containers/NavBar';

function App() {
  return (
    <>
      <NavBar />
      <div id='main'>
        <div id='background'></div>
        <Outlet />
      </div>
    </>
  );
}

export default App;
