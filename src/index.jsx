import React from 'react';
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux';
import store from './redux/store';
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
  RouterProvider,
} from 'react-router-dom';
import './styles/style.scss';

import Main from './containers/Main';
import NavBar from './containers/NavBar';
import Clusters from './containers/Clusters';
import History from './containers/History';
import Settings from './containers/Settings';
import ErrorPage from './error-page';

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route
      path='/'
      element={<NavBar />}
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

// <React.StrictMode>
//   <Provider store={store}>
//     <RouterProvider router={router} />
//   </Provider>
// </React.StrictMode>

// export default function index() {
//   return(
//     <RouterProvider router={router} />
//   )
// };

// -------------

// import * as React from "react";
// import * as ReactDOM from "react-dom/client";
// import {
//   createBrowserRouter,
//   RouterProvider,
// } from "react-router-dom";
// import "./styles/style.scss";

// import { Provider } from "react-redux";
// import store from "./redux/store";

// const router = createBrowserRouter([
//   {
//     path: "/",
//     element: <Main />,
//     errorElement: <ErrorPage />,
//   },

//   {
//     path: "/history",
//     element: <History />,
//   },
//   {
//     path: '/settings',
//     element: <Settings />,
//   }
// ]);

// ReactDOM.createRoot(document.getElementById("root")).render(

//   <React.StrictMode>
//     <Provider store={store}>
//       {/* <NavBar /> */}
//       <RouterProvider router={router} />
//     </Provider>
//   </React.StrictMode>
// );

// document.addEventListener('DOMContentLoaded', function () {
//   const rootElement = document.getElementById('root');

//   console.log(rootElement);
//   if (rootElement) {
//     const root = ReactDOM.createRoot(rootElement);
//     root.render(
//       <Provider store={store}>
//         <React.StrictMode>
//           <BrowserRouter >
//             <App />
//           </BrowserRouter>
//         </React.StrictMode>
//       </Provider>
//     );
//   } else {
//     console.log('Error');
//   }
// });

// import React from 'react';
// import ReactDOM from 'react-dom/client';
// import App from './App.jsx';
// import store from './redux/store.js';
// import { Provider } from 'react-redux';
// import { BrowserRouter, createBrowserRouter, RouterProvider } from 'react-router-dom';
// import './styles/style.scss';
// import Main from './containers/Main.jsx';
// import { Route, Routes } from 'react-router-dom';
// import NavBar from './containers/NavBar.jsx'
// import History from './components/History.jsx';
// import Settings  from './components/Settings.jsx';

// document.addEventListener('DOMContentLoaded', function () {
//   const rootElement = document.getElementById('root');

//   console.log(rootElement);
//   if (rootElement) {
//     const root = ReactDOM.createRoot(rootElement);
//     root.render(
//       <Provider store={store}>
//         <React.StrictMode>
//           <BrowserRouter >
//             <App />
//           </BrowserRouter>
//         </React.StrictMode>
//       </Provider>
//     );
//   } else {
//     console.log('Error');
//   }
// });

// const router = createBrowserRouter([
//   {
//     path: '/',
//     element: <Main />,
//   },
//   // {
//   //   path: '/settings',
//   //   element: <Settings />,
//   // },
//   // {
//   //   path: '/history',
//   //   element: <History />,
//   // },
// ]);

// document.addEventListener('DOMContentLoaded', function () {
//   const rootElement = document.getElementById('root');

//   console.log(rootElement);
//   if (rootElement) {
//     const root = ReactDOM.createRoot(rootElement);
//     root.render(
//       <Provider store={store}>
//         {/* <NavBar />
//         <RouterProvider router={router} /> */}
//         <App />
//       </Provider>
//     );
//   } else {
//     console.log('Error');
//   }
// });
