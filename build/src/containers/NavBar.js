import { jsx as _jsx, jsxs as _jsxs, Fragment as _Fragment } from "react/jsx-runtime";
import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { NavLink, Outlet, useLocation } from 'react-router-dom';
import { addChart, removeChart } from '../reducers/chartSlice.js';
import client from '../utils/socket.js';
import { metricListFriendly } from '../utils/metrics.js';
async function getMetricsList() {
    const res = await fetch('http://localhost:8080/available-server-metrics');
    const metricListRaw = await res.json();
    const metricList = metricListFriendly(metricListRaw);
    return metricList;
}
export default function NavBar({ props }) {
    const [metrics, setMetrics] = useState([]);
    const [run, setRun] = useState(false);
    const chartList = useSelector((state) => state.charts.list);
    const metricCount = useSelector((state) => state.charts.metricCount);
    const location = useLocation();
    const dispatch = useDispatch();
    useEffect(() => {
        async function getMetrics() {
            const metrics = await getMetricsList();
            setMetrics(metrics);
        }
        getMetrics();
    }, []);
    function handleToggleChart(metricId) {
        const checkbox = document.querySelector(`#${metricId}`);
        // console.log(checkbox);
        if (checkbox.checked) {
            dispatch(addChart(metricId));
        }
        else {
            dispatch(removeChart(metricId));
            client.publish({
                destination: '/app/unsubscribe',
                body: JSON.stringify({ metric: metricId }),
            });
        }
    }
    function startProducers() {
        if (!run) {
            fetch('http://localhost:3030/test', { mode: 'no-cors' });
            setRun(true);
        }
        else {
            fetch('http://localhost:3030/stopTest', { mode: 'no-cors' });
            setRun(false);
        }
    }
    function showMetrics() {
        const metricsBox = document.getElementById('metric-list');
        let currDisplay = metricsBox.style.display;
        if (!currDisplay) {
            currDisplay = 'none';
        }
        metricsBox.style.display = currDisplay === 'none' ? 'block' : 'none';
    }
    return (_jsx("div", { className: 'nav-bar', children: _jsxs("header", { children: [_jsxs("nav", { className: 'left', children: [_jsx(NavLink, { to: '/', children: _jsx("button", { children: "Home" }) }), _jsx(NavLink, { to: '/history', children: _jsx("button", { children: "History" }) }), _jsx(NavLink, { to: '/settings', children: _jsx("button", { children: "Settings" }) }), location.pathname === '/' ? (_jsxs("div", { id: 'metric-picker', children: [_jsx("div", { children: _jsx("button", { onClick: showMetrics, children: "Choose Metrics" }) }), _jsx("nav", { id: 'metric-list', onMouseLeave: showMetrics, children: metrics?.map((metric) => {
                                        return (_jsxs("label", { className: 'chart-selector', children: [_jsx("input", { type: 'checkbox', id: metric.name, name: metric.name, defaultValue: chartList[metric], onClick: () => handleToggleChart(metric.name) }), metric.display] }, metric.name));
                                    }) })] })) : (_jsx(_Fragment, {}))] }), _jsx("nav", { className: 'right', children: _jsxs("button", { onClick: startProducers, children: [run ? 'Stop' : 'Start', " Producers"] }) })] }) }));
}
//# sourceMappingURL=NavBar.js.map