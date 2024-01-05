import React from 'react';
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux';
import store from './redux/store.js';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './styles/style.css';

import App from './App.jsx';
import Main from './containers/Main.jsx';
import History from './containers/History.jsx';
import Settings from './containers/Settings.jsx';
import ErrorPage from './containers/ErrorPage.jsx';
import About from './containers/About.jsx';
import Feedback from './containers/Feedback.jsx';

const MainRoot = () => {
  return (
    <React.StrictMode>
      <Provider store={store}>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<App />} errorElement={<ErrorPage />}>
              <Route index element={<Main />} />
              <Route path="history" element={<History />} />
              <Route path="settings" element={<Settings />} />
              <Route path="feedback" element={<Feedback />} />
              <Route path="about" element={<About />} />
              <Route path="*" element={<ErrorPage />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </Provider>
    </React.StrictMode>
  );
};

export default MainRoot;
