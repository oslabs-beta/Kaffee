import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';

export default function NavBar() {
  return (
    <div class='nav-bar'>
      <header>
        <nav>
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
          <label for='metrics'>Choose Metric</label>
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
      </header>
    </div>
  );
}

// import React from 'react';
// import { Link, Route, Router, Routes} from 'react-router-dom';
// import History from '../components/History';

// const NavBar =  () => {
//   return (
//     // <Router>
//       <div id="top-nav">

//       <div id="left">
//         <img  id="logo" />
//         <button id="home" >Home</button>

//         {/* <Link to="/history">  */}
//           <button type="button" id="history">
//             History
//           </button>
//         {/* </Link> */}

//           {/* <Link to="/history">
//             hist
//           </Link> */}

//         <button id="settings">Settings</button>
//         <label for="metrics">Choose Metric</label>
//         <select name="metrics" id="metrics selector">
//           <option value="">--Choose a metric--</option>
//           <option value="bytes">Bytes in/out</option>
//           <option value="messages">Messages in/out</option>
//           <option value="else">Else</option>
//         </select>
//       </div>

//       <div id="right">
//         <button id="report">Grind Report</button>
//         <button id="mode">Toggle Dark Mode</button>

//       </div>

//     {/* <Routes>

//     <Route path="/history" element={<History />} />
//     </Routes> */}

// {/* </Router> */}
//     </div>

//   )
// };

// export default NavBar;
