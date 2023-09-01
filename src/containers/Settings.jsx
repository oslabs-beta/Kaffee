import React, { useEffect, useState } from 'react';
import path from 'path';
import { useDispatch, useSelector } from 'react-redux';
import { changeMetricCount } from '../reducers/chartSlice.js';
import {
  setJmxPort,
  setKafkaPort,
  setLogFilepath,
  setZookeeperPort,
} from '../reducers/settingSlice.js';
import { useLoaderData } from 'react-router';

async function getDirectory() {
  const directory = await window.showDirectoryPicker();
  return directory;
}

export async function loader() {
  try {
    const res = await fetch('http://localhost:3030/getSettings');
    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error);
    throw new Error('Error fetching settings', { cause: error });
  }
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
  const [metricTimeout, setMetricTimeout] = useState(null);

  const metricCount = useSelector((state) => state.charts.metricCount);

  // we load this data when we first boot the app. We shouldn't make 2 calls.
  // saving for later.
  // const filepath = useSelector((state) => state.settings.logfilePath);
  // const JMX = useSelector((state) => state.settings.jmxPort);
  // const zookeeper = useSelector((state) => state.settings.zookeeperPort);
  // const kafka = useSelector((state) => state.settings.kafkaPort);

  const dispatch = useDispatch();
  const data = useLoaderData();

  useEffect(() => {
    setKafka(data['kafka-port']);
    setZookeeper(data['zookeeper-port']);
    setJMX(data['JMX-port']);
    setFilepath(data['log-filepath']);
    dispatch(changeMetricCount(data['metric-count']));
  }, []);

  const updateSettings = (param, val) => {
    if (
      param === 'kafka-port' ||
      param === 'zookeeper-port' ||
      param === 'JMX-port' ||
      param === 'metric-count'
    ) {
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
      .then((response) => response.json())
      .then((data) => console.log(data))
      .then(loader)
      .catch((error) => {
        throw new Error('Error saving updated settings.', { cause: error });
      });
  };

  const handleEnterPress = (e, param, val) => {
    if (e.key === 'Enter') {
      updateSettings(param, val);
      if (param === 'kafka-port') setkInput('');
      if (param === 'zookeeper-port') setzInput('');
      if (param === 'JMX-port') setjInput('');
      if (param === 'log-filepath') setfInput('');
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
    dispatch(changeMetricCount(e.target.value * 10));

    if (metricTimeout) {
      clearTimeout(metricTimeout);
    }
    setMetricTimeout(
      setTimeout(() => updateSettings(e.target.id, e.target.value), 500)
    );
  }

  return (
    <div className='settings-container'>
      <div className='setting'>
        <label htmlFor='kafka-port'>Kafka Port </label>
        <input
          id='kafka-port'
          type='text'
          name='kafka-port-num'
          defaultValue={kInput}
          onKeyDown={(e) => handleEnterPress(e, 'kafka-port', kInput)}
          onChange={(e) => setkInput(e.target.value)}
        />
        <label htmlFor='kafka-port'> {kafka} </label>
      </div>
      <div className='setting'>
        <label htmlFor='zookeeper-port'>Zookeeper Port </label>
        <input
          id='zookeeper-port'
          type='text'
          name='zookeeper-port-num'
          defaultValue={zInput}
          onKeyDown={(e) => handleEnterPress(e, 'zookeeper-port', zInput)}
          onChange={(e) => setzInput(e.target.value)}
        />
        <label htmlFor='kafka-port'> {zookeeper} </label>
      </div>
      <div className='setting'>
        <label htmlFor='JMX-port'>JMX Port </label>
        <input
          id='JMX-port'
          type='text'
          name='JMX-port-num'
          defaultValue={jInput}
          onKeyDown={(e) => handleEnterPress(e, 'JMX-port', jInput)}
          onChange={(e) => setjInput(e.target.value)}
        />
        <label htmlFor='kafka-port'> {JMX} </label>
      </div>
      <div className='setting'>
        <label htmlFor='log-filepath'>Log Filepath </label>
        <input
          id='log-filepath'
          type='text'
          name='log-filepath-string'
          defaultValue={fInput}
          onKeyDown={(e) => handleEnterPress(e, 'log-filepath', fInput)}
          onChange={(e) => setfInput(e.target.value)}
          // onClick={handleDirectorySelector}
        />
        <label htmlFor='kafka-port'>{filepath} </label>
      </div>
      <div className='setting'>
        <label htmlFor='metric-count'>Seconds of Data Displayed</label>
        <input
          id='metric-count'
          name='metric-count'
          type='range'
          min='1'
          max='60'
          step='5'
          value={metricCount}
          onChange={(e) => setInput(e)}
        ></input>
        <label htmlFor='kafka-port'>{metricCount}</label>
      </div>
    </div>
  );
};

export default Settings;
