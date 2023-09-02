import { jsx as _jsx, jsxs as _jsxs } from "react/jsx-runtime";
import React from 'react';
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux';
import store from './redux/store.ts';
import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider, } from 'react-router-dom';
import './styles/style.scss';
import App from './App';
import Main from './containers/Main';
import Clusters from './containers/Clusters';
import History from './containers/History';
import Settings from './containers/Settings';
import ErrorPage from './containers/ErrorPage';
const router = createBrowserRouter(createRoutesFromElements(_jsxs(Route, { path: '/', element: _jsx(App, {}), errorElement: _jsx(ErrorPage, {}), children: [_jsx(Route, { index: true, element: _jsx(Main, {}) }), _jsx(Route, { path: 'clusters', element: _jsx(Clusters, {}) }), _jsx(Route, { path: 'history', element: _jsx(History, {}) }), _jsx(Route, { path: 'settings', element: _jsx(Settings, {}) }), _jsx(Route, { path: '*', element: _jsx(ErrorPage, {}) })] })));
ReactDOM.createRoot(document.getElementById('root')).render(_jsx(React.StrictMode, { children: _jsx(Provider, { store: store, children: _jsx(RouterProvider, { router: router }) }) }));
//# sourceMappingURL=index.js.map