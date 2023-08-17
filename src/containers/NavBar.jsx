import React from 'react';

const NavBar = () => {
  return (
    <nav id='top-nav'>
      <div id='left'>
        <div id='logo'></div>
        <button id='home'>Home</button>
        <button id='history'>History</button>
        <button id='settings'>Settings</button>
        <label htmlFor='metrics'>Choose Metric</label>
        <select
          name='metrics'
          id='metrics selector'
        >
          <option value=''>--Choose a metric--</option>
          <option value='bytes'>Bytes in/out</option>
          <option value='messages'>Messages in/out</option>
          <option value='else'>Else</option>
        </select>
      </div>

      <div id='right'>
        <div className='adjuster'></div>
        <button id='report'>Grind Report</button>
        <button id='mode'>Toggle Dark Mode</button>
      </div>
    </nav>
  );
};

export default NavBar;
