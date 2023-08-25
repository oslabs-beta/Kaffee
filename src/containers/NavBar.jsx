import React, { useState } from 'react';
import { NavLink, Outlet } from 'react-router-dom';

export default function NavBar() {
  const [run, setRun] = useState(false);
  function startProducers() {
    if (!run) {
      fetch('http://localhost:3030/test', { mode: 'no-cors' });
      setRun(true);
    } else {
      fetch('http://localhost:3030/stopTest', { mode: 'no-cors' });
      setRun(false);
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
          {/* <NavLink to='/metrics'></NavLink> */}
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
