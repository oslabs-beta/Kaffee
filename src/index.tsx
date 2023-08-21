import React from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { store } from './redux/store.ts';
import {
  BrowserRouter,
  createBrowserRouter,
  RouterProvider,
  Outlet,
} from 'react-router-dom';

import './styles/style.scss';

import App from './App.jsx';
import Main from './containers/Main.jsx';
import NavBar from './containers/NavBar.jsx';
import Settings from './containers/Settings.jsx';
import History from './containers/History.jsx';

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        path: '/',
        element: <Main />,
      },
      {
        path: '/settings',
        element: <Settings />,
      },
      {
        path: 'history',
        element: <History />,
      },
    ],
  },
]);

// note the bang operator (!) at the end to ensure container isn't null
const container = document.getElementById('root')!;
const root = createRoot(container);

root.render(
  <React.StrictMode>
    <Provider store={store}>
      <RouterProvider router={router} />
    </Provider>
  </React.StrictMode>
);
