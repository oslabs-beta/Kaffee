import { jsx as _jsx } from "react/jsx-runtime";
import React from 'react';
import HistoryItem from '../components/HistoryItem.jsx';
import dummyData from './dummyData.json';
const History = () => {
    return (_jsx("div", { className: 'history', children: _jsx(HistoryItem, { data: dummyData.data }) }));
};
export default History;
//# sourceMappingURL=History.js.map