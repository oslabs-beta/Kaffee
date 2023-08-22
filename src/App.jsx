import React from 'react';
import { Outlet } from 'react-router-dom';
import NavBar from './containers/NavBar';

export default function App() {
  return (
    <>
      <NavBar />
      <div id='main'>
        <Outlet />
      </div>
    </>
  );
}
