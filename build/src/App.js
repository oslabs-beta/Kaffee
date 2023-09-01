import { jsx as _jsx, Fragment as _Fragment, jsxs as _jsxs } from "react/jsx-runtime";
import React, { useEffect } from 'react';
import { Outlet } from 'react-router-dom';
import NavBar from './containers/NavBar';
import { setClient } from './reducers/socketSlice';
import client from './utils/socket.js';
import { useDispatch, useSelector } from 'react-redux';
export default function App() {
    const socketClient = useSelector((state) => state.sockets.client);
    const dispatch = useDispatch();
    useEffect(() => {
        client.activate();
        return () => {
            client.deactivate();
        };
    }, []);
    return (_jsxs(_Fragment, { children: [_jsx(NavBar, {}), _jsx("div", { id: 'main', children: _jsx(Outlet, {}) })] }));
}
//# sourceMappingURL=App.js.map