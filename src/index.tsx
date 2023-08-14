import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './components/App';
import store from './redux/store';
import { Provider } from 'react-redux';
// import styles

const rootElement = document.getElementById('root');
const root = ReactDOM.createRoot(rootElement);

root.render(
  <Provider store={store}>
    <App />
  </Provider>
)
