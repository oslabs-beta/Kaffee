import React from 'react';
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux';
import store from './redux/store.js';
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
  RouterProvider,
} from 'react-router-dom';
import './styles/style.css';

import App from './App.jsx';
import Main from './containers/Main.jsx';
import Clusters from './containers/Clusters.jsx';
import History from './containers/History.jsx';
import Settings from './containers/Settings.jsx';
import ErrorPage from './containers/ErrorPage.jsx';
import About from './containers/About.jsx';

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/" element={<App />} errorElement={<ErrorPage />}>
      <Route index element={<Main />} />
      <Route path="clusters" element={<Clusters />} />
      <Route path="history" element={<History />} />
      <Route path="settings" element={<Settings />} />
      <Route path="about" element={<About />} />
      <Route path="*" element={<ErrorPage />} />{' '}
      <Route path="*" element={<ErrorPage />} />
      <Route path="*" element={<ErrorPage />} />
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
