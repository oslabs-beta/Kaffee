import React from 'react';
import { Link, Route, Routes, Outlet } from 'react-router-dom';
import NavBar from './containers/NavBar';
import History from './containers/History';
import Main from './containers/Main';
import Settings from './containers/Settings';

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
