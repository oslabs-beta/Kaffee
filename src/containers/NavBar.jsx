import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';

export default function NavBar() {
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
          {/* <NavLink to='/socket'>
            <button>Socket Example</button>
          </NavLink> */}
        </nav>
      </header>
    </div>
  );
}
