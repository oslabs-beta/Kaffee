import { isRouteErrorResponse, useRouteError } from 'react-router-dom';

function handleError(err) {
  let cause = '';
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
  let text;

  if (isRouteErrorResponse(err)) {
    text = `${err.status} ${err.statusText}`;
  } else if (err instanceof Error) {
    text = handleError(err);
  } else if (typeof err === 'string') {
    text = err;
  } else {
    text = 'This page does not exist.';
  }

  return (
    <>
      <div id="main">
        <div className="error">{text}</div>
      </div>
    </>
  );
}
