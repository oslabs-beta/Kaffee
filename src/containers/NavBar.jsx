import React from 'react';


const NavBar =  () => {
  return (
    <div id="top-nav">

      <div id="left">
        <img  id="logo" />
        <button id="home" >Home</button>
        <button id="history">History</button>
        <button id="settings">Settings</button>
        <label for="metrics">Choose Metric</label>
        <select name="metrics" id="metrics selector">
          <option value="">--Choose a metric--</option>
          <option value="bytes">Bytes in/out</option>
          <option value="messages">Messages in/out</option>
          <option value="else">Else</option>
        </select>
      </div>

      <div id="right">
        <button id="report">Grind Report</button>
        <button id="mode">Toggle Dark Mode</button>

      </div>
      
    </div>
  )
};

export default NavBar;
