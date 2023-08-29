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

import App, { loader as appLoader } from './App.tsx';
import ErrorPage from './containers/ErrorPage.tsx';
import Main from './containers/Main.jsx';
import Settings, { loader as settingLoader } from './containers/Settings.jsx';
import History from './containers/History.jsx';
import SocketTest from './components/SocketTest.jsx';
import { loader as navLoader } from './containers/NavBar.jsx';
import { json } from 'stream/consumers';

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    loader: async () => {
      const settings = await appLoader();
      const metrics = await navLoader();
      return { metrics, settings };
    },
    errorElement: <ErrorPage />,
    children: [
      {
        path: '/',
        element: <Main />,
      },
      {
        path: '/settings',
        loader: settingLoader,
        element: <Settings />,
      },
      {
        path: 'history',
        element: <History />,
      },
      {
        path: 'socket',
        element: <SocketTest />,
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
