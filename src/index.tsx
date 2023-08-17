import React from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { store } from './redux/store.ts';
import {
  BrowserRouter,
  createBrowserRouter,
  RouterProvider,
} from 'react-router-dom';

import './styles/style.scss';

import Main from './containers/Main.jsx';
import NavBar from './containers/NavBar.jsx';
import Settings from './components/Settings.jsx';
import History from './components/History.jsx';

const router = createBrowserRouter([
  {
    path: '/',
    element: <Main />,
  },
  {
    path: '/settings',
    element: <Settings />,
  },
  {
    path: '/history',
    element: <History />,
  },
]);

// note the bang operator (!) at the end to ensure container isn't null
const container = document.getElementById('root')!;
const root = createRoot(container);

root.render(
  <React.StrictMode>
    <Provider store={store}>
      <NavBar />
      <RouterProvider router={router} />
    </Provider>
  </React.StrictMode>
);
