import { jsx as _jsx, jsxs as _jsxs } from "react/jsx-runtime";
import React, { useState, useEffect } from 'react';
import History from '../containers/History.jsx';
// TODO:
// import history folder
// parse names of json files by date
// create chartjs from data on expand
export default function ({ data }) {
    const items = [];
    data.forEach(el => {
        const [expanded, setExpanded] = useState(false);
        const clickHandler = () => {
            setExpanded(!expanded);
        };
        items.push(_jsxs("div", { className: 'history-item-container', children: [_jsxs("div", { className: "history-preview", children: [_jsx("div", { className: 'history-date', children: el.Date }), _jsx("button", { className: 'expand-button', onClick: clickHandler, children: expanded ? 'Collapse' : 'Expand' })] }), expanded && _jsx("div", { className: 'expanded-metrics', children: JSON.stringify(el.Metrics) })] }, el.id));
    });
    return (items);
}
//# sourceMappingURL=HistoryItem.js.map