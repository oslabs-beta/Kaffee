import { jsx as _jsx, jsxs as _jsxs } from "react/jsx-runtime";
import React, { useState, MouseEvent } from 'react';
// import { useAppDispatch, useAppSelector } from '../redux/hooks.ts';
import { useSelector, useDispatch } from 'react-redux';
import { filterCharts } from '../reducers/chartSlice.js';
export default function () {
    const [search, setSearch] = useState('');
    // const dispatch = useAppDispatch();
    const dispatch = useDispatch();
    // const handleChangedText = (e: MouseEvent) => {
    const handleChangedText = (e) => {
        return setSearch(e.target.value);
    };
    const handleBrew = () => {
        alert(search);
        dispatch(filterCharts(search));
    };
    return (_jsx("footer", { children: _jsxs("form", { method: 'POST', children: [_jsx("input", { type: 'text', onChange: handleChangedText, placeholder: 'Input box: number of messages, size of messages, frequency' }), _jsx("button", { onClick: handleBrew, children: "Brew" })] }) }));
}
//# sourceMappingURL=SearchBar.js.map