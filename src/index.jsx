import React from 'react';
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux';
import store from './redux/store.ts';
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
  RouterProvider,
} from 'react-router-dom';
import './styles/style.scss';

import App from './App';
import Main from './containers/Main';
import Clusters from './containers/Clusters';
import History from './containers/History';
import Settings from './containers/Settings';
import ErrorPage from './containers/ErrorPage';

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route
      path='/'
      element={<App />}
      errorElement={<ErrorPage />}
    >
      <Route
        index
        element={<Main />}
      />
      <Route
        path='clusters'
        element={<Clusters />}
      />
      <Route
        path='history'
        element={<History />}
      />
      <Route
        path='settings'
        element={<Settings />}
      />
      {/* <Route path="metrics" element={<Metrics />} /> */}
      <Route
        path='*'
        element={<ErrorPage />}
      />
    </Route>
  )
);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <Provider store={store}>
      <RouterProvider router={router} />
    </Provider>
  </React.StrictMode>
);
