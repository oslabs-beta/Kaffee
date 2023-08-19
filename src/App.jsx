import React from 'react';
import { Link, Route, Routes } from 'react-router-dom'
import NavBar from './containers/NavBar';
import History from './containers/History';
import Main from './containers/Main';
import Settings from './containers/Settings';



function App() {
  // return <></>;
  return (
    <>
      {/* <nav>
        <ul>
          <li>
            <Link to="/history">History</Link>
            <Link to="/settings">Settings</Link>
            <Link to="/">Main</Link>
          </li>
        </ul>
      </nav> */}


      <NavBar />
      <Routes>
        {/* <Route path='/'  element={<Home />} /> */}
        <Route path="/navbar" element={<NavBar />} />
        <Route path="/" element={<Main />} />
        <Route path="/history" element={<History />} />
        <Route path="/settings" element={<Settings />} />
      
      </Routes>

   </>  
    )
  
};

export default App;
