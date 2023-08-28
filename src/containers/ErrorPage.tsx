import React from 'react';
import NavBar from './NavBar.jsx';
import { Link } from 'react-router-dom';

export default function ErrorPage() {
  return (
    <>
      <NavBar />
      <div id='main'>
        <h1>Oops!</h1>
        <p>Sorry, an unexpected error has occurred.</p>
        <p>
          <i>
            Page doesn't exist. Go to the <Link to='/'>Home</Link> page.
          </i>
          {/* <i>{error.statusText || error.message}</i> */}
        </p>
      </div>
    </>
  );
}
