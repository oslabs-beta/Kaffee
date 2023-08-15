import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './components/App.jsx';
import store from './redux/store.js';
import { Provider } from 'react-redux';


document.addEventListener('DOMContentLoaded', function () {
  const rootElement = document.getElementById('root');
  
  
  if (rootElement) {
    const root = ReactDOM.createRoot(rootElement);
    root.render(
      <Provider store={store}>
        <App />
      </Provider>
    )
  } else {
    console.log("Error")
  }
  
  
})



