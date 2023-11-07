import { Link, useRouteError } from "react-router-dom";
import React from "react";

export default function ErrorPage() {
  const error = useRouteError();
  console.error(error);

  return (
    <div id="error-page">
      <h1>Oops!</h1>
      <p>Sorry, an unexpected error has occurred.</p>
      <p>
        <i>Page doesn't exist. Go to the <Link to="/">Home</Link> page.</i>
        {/* <i>{error.statusText || error.message}</i> */}
      </p>
    </div>
  );
}