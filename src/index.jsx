import React from 'react';
// import store from './redux/store.ts';
import store from './redux/store.js';
import { Provider } from 'react-redux';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import './styles/style.scss';
import ReactDOM from 'react-dom/client';

import Main from './containers/Main.jsx';
import NavBar from './containers/NavBar.jsx';

const router = createBrowserRouter([
  {
    path: '/',
    element: <Main />,
  },
  // {
  //   path: '/settings',
  //   element: <Settings />,
  // },
  // {
  //   path: '/history',
  //   element: <History />,
  // },
]);

// document.addEventListener('DOMContentLoaded', function () {
const rootElement = document.getElementById('root');

// if (rootElement) {
const root = ReactDOM.createRoot(rootElement);
root.render(
  <Provider store={store}>
    <NavBar />
    <RouterProvider router={router} />
  </Provider>
);
// } else {
//   console.log('Error');
// }
// });
