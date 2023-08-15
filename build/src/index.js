import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './components/App.jsx';
import store from './redux/store.js';
import { Provider } from 'react-redux';
document.addEventListener('DOMContentLoaded', function () {
    const rootElement = document.getElementById('root');
    if (rootElement) {
        const root = ReactDOM.createRoot(rootElement);
        root.render(React.createElement(Provider, { store: store },
            React.createElement(App, null)));
    }
    else {
        console.log("Error");
    }
});
//# sourceMappingURL=index.js.map