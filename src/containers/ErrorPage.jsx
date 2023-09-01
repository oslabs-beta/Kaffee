import React, { ReactElement, createElement } from 'react';
import NavBar from './NavBar.jsx';
import { Link, isRouteErrorResponse, useRouteError } from 'react-router-dom';

// function handleError(err: Error): ReactElement {
function handleError(err) {
  let cause = '';
  // let cause: string = '';
  if (err.stack) {
    cause = err.stack;
  }

  return (
    <>
      {err.message}
      <br />
      <i>{cause}</i>
    </>
  );
}

export default function ErrorPage() {
  const err = useRouteError();
  // let text: String | ReactElement;
  let text;

  if (isRouteErrorResponse(err)) {
    text = `${err.status} ${err.statusText}`;
  } else if (err instanceof Error) {
    text = handleError(err);
  } else if (typeof err === 'string') {
    text = err;
  } else {
    text = 'Unknown error.';
  }

  return (
    <>
      <NavBar />
      <div id='main'>
        <div className='error'>{text}</div>
      </div>
    </>
  );
}
