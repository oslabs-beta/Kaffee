import { jsx as _jsx, jsxs as _jsxs } from "react/jsx-runtime";
import React, { useEffect, useState } from 'react';
import path from 'path';
import { useDispatch, useSelector } from 'react-redux';
import { changeMetricCount } from '../reducers/chartSlice.js';
async function getDirectory() {
    const directory = await window.showDirectoryPicker();
    return directory;
}
const Settings = () => {
    const [kafka, setKafka] = useState();
    const [zookeeper, setZookeeper] = useState();
    const [JMX, setJMX] = useState();
    const [filepath, setFilepath] = useState();
    const [kInput, setkInput] = useState('');
    const [zInput, setzInput] = useState('');
    const [jInput, setjInput] = useState('');
    const [fInput, setfInput] = useState('');
    const metricCount = useSelector((state) => state.charts.metricCount);
    const dispatch = useDispatch();
    const fetchSettings = () => {
        fetch('http://localhost:3030/getSettings')
            .then((response) => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
            .then((data) => {
            // console.log(data, 'this is the fetch call');
            setKafka(data['kafka-port']);
            setZookeeper(data['zookeeper-port']);
            setJMX(data['JMX-port']);
            setFilepath(data['log-filepath']);
            // console.log(filepath);
        })
            .catch((error) => {
            console.error('Fetch error:', error);
        });
    };
    const updateSettings = (param, val) => {
        if (param === 'KAFKA_PORT' ||
            param === 'ZOOKEEPER_PORT' ||
            param === 'JMX_PORT' ||
            param === 'metric-count') {
            val = Number(val);
        }
        fetch('http://localhost:3030/updateSettings', {
            method: 'POST',
            body: JSON.stringify({
                settingName: param,
                newValue: val,
            }),
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
            },
        })
            .then((response) => console.log(response))
            .then(fetchSettings())
            .catch((error) => console.error('Error:', error));
    };
    fetchSettings();
    const handleEnterPress = (e, param, val) => {
        if (e.key === 'Enter') {
            console.log(val, param);
            updateSettings(param, val);
            if (param === 'KAFKA_PORT')
                setkInput('');
            if (param === 'ZOOKEEPER_PORT')
                setzInput('');
            if (param === 'JMX_PORT')
                setjInput('');
            if (param === 'log-filepath')
                setfInput('');
        }
    };
    // I'm leaving this here in case we can use something similar when we
    // are integrating with some JS -> App program, like Electron
    const handleDirectorySelector = async () => {
        const directory = await getDirectory();
        if (!directory) {
            // user closed the window or otherwise failed to open the file
            return;
        }
        // See: https://developer.chrome.com/articles/file-system-access/
        const sep = path.sep;
        const dirArray = [];
        for await (const entry of directory.values()) {
            console.log(entry.kind, entry.name);
        }
        // console.log(dirArray);
        // const resolvedPath = dirArray.join(sep);
        // const field = document.querySelector('#log-filepath');
        // field.value = directory;
    };
    function setInput(e) {
        // console.log(e);
        dispatch(changeMetricCount(e.target.value));
        updateSettings(e.target.id, e.target.value);
    }
    return (_jsxs("div", { className: 'settings-container', children: [_jsxs("div", { className: 'setting', children: [_jsx("label", { htmlFor: 'kafka-port', children: "Kafka Port " }), _jsx("input", { id: 'kafka-port', type: 'text', name: 'kafka-port-num', defaultValue: kInput, onKeyDown: (e) => handleEnterPress(e, 'KAFKA_PORT', kInput), onChange: (e) => setkInput(e.target.value) }), _jsxs("label", { htmlFor: 'kafka-port', children: [" ", kafka, " "] })] }), _jsxs("div", { className: 'setting', children: [_jsx("label", { htmlFor: 'zookeeper-port', children: "Zookeeper Port " }), _jsx("input", { id: 'zookeeper-port', type: 'text', name: 'zookeeper-port-num', defaultValue: zInput, onKeyDown: (e) => handleEnterPress(e, 'ZOOKEEPER_PORT', zInput), onChange: (e) => setzInput(e.target.value) }), _jsxs("label", { htmlFor: 'kafka-port', children: [" ", zookeeper, " "] })] }), _jsxs("div", { className: 'setting', children: [_jsx("label", { htmlFor: 'JMX-port', children: "JMX Port " }), _jsx("input", { id: 'JMX-port', type: 'text', name: 'JMX-port-num', defaultValue: jInput, onKeyDown: (e) => handleEnterPress(e, 'JMX_PORT', jInput), onChange: (e) => setjInput(e.target.value) }), _jsxs("label", { htmlFor: 'kafka-port', children: [" ", JMX, " "] })] }), _jsxs("div", { className: 'setting', children: [_jsx("label", { htmlFor: 'log-filepath', children: "Log Filepath " }), _jsx("input", { id: 'log-filepath', type: 'text', name: 'log-filepath-string', defaultValue: fInput, onKeyDown: (e) => handleEnterPress(e, 'log-filepath', fInput), onChange: (e) => setfInput(e.target.value) }), _jsxs("label", { htmlFor: 'kafka-port', children: [filepath, " "] })] }), _jsxs("div", { className: 'setting', children: [_jsx("label", { htmlFor: 'metric-count', children: "Metric Count" }), _jsx("input", { id: 'metric-count', name: 'metric-count', type: 'range', min: '10', max: '500', step: '10', defaultValue: metricCount, onChange: (e) => setInput(e) }), _jsx("label", { htmlFor: 'kafka-port', children: metricCount })] })] }));
};
export default Settings;
//# sourceMappingURL=Settings.js.map