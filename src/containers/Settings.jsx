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
  // const [zookeeper, setZookeeper] = useState();
  const [JMX, setJMX] = useState();
  const [filepath, setFilepath] = useState();

  const [kInput, setkInput] = useState('');
  // const [zInput, setzInput] = useState('');
  const [jInput, setjInput] = useState('');
  const [fInput, setfInput] = useState('');

  const [producers, setProducers] = useState();
  const [consumers, setConsumers] = useState();
  const [kafkaURL, setKafkaURL] = useState();

  const [kURLInput, setkURLInput] = useState('');
  const [cInput, setCInput] = useState('');
  const [pInput, setPInput] = useState('');


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
        console.log(data, 'this is the fetch call');
        setKafka(data['KAFKA_PORT']);
        // setZookeeper(data['zookeeper-port']);
        setJMX(data['JMX_PORT']); // Changed from 'JMX-port'
        setFilepath(data['log-filepath']);
        setConsumers(data['consumers']);
        setProducers(data['producers']);
        setKafkaURL(data['KAFKA_URL']);
      })
      .catch((error) => {
        console.error('Fetch error:', error);
      });
  };

  const updateSettings = (param, val) => {
    if (
      // applies only to params that are numbers
      param === 'KAFKA_PORT' ||
      // param === 'ZOOKEEPER_PORT' ||
      param === 'JMX_PORT' ||
      param === 'metric-count' ||
      param === 'producers' ||
      param === 'consumers' 
      // param === 'KAFKA_URL' 
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
      .then((response) => console.log(response))
      .then(fetchSettings())
      .catch((error) => console.error('Error:', error));
  };

  fetchSettings();

  const handleEnterPress = (e, param, val) => {
 
    if (e.key === 'Enter') {
      updateSettings(param, val);
      if (param === 'KAFKA_PORT') setkInput('');
      // if (param === 'ZOOKEEPER_PORT') setzInput('');
      if (param === 'JMX_PORT') setjInput('');
      if (param === 'log-filepath') setfInput('');
      if (param === 'producers') setPInput('');
      if (param === 'consumers') setCInput('');
      if (param === 'KAFKA_URL') setkURLInput('');
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
    dispatch(changeMetricCount(e.target.value));
    updateSettings(e.target.id, e.target.value);
  }

  return (
    <div className='settings-container'>
      <div className='setting'>
        <label htmlFor='kafka-port'>Kafka Port </label>
        <input
          id='kafka-port'
          type='number'
          name='kafka-port-num'
          defaultValue={kInput}
          placeholder={kafka}
          onKeyDown={(e) => handleEnterPress(e, 'KAFKA_PORT', kInput)}
          onChange={(e) => setkInput(e.target.value)}
        />
        {/* <label htmlFor='kafka-port'> {kafka} </label> */}
      </div>


      <div className="setting">
        <label htmlFor='kafkaURL'>Kafka URL</label>
        <input 
          id='kafkaURL'
          type="text" 
          name='kafkaURL'
          defaultValue={kURLInput}
          placeholder={kafkaURL}
          onKeyDown= {(e) => handleEnterPress(e, 'KAFKA_URL', kURLInput)}
          onChange={(e) => setkURLInput(e.target.value)}
        />
        {/* <label htmlFor='kafkaURL'> {kafkaURL} </label> */}
      </div>


      <div className='setting'>
        <label htmlFor='JMX-port'>JMX Port </label>
        <input
          id='JMX-port'
          type='number'
          name='JMX-port-num'
          defaultValue={jInput}
          placeholder={JMX}
          onKeyDown={(e) => handleEnterPress(e, 'JMX_PORT', jInput)}
          onChange={(e) => setjInput(e.target.value)}
        />
        {/* <label htmlFor='JMX-port'> {JMX} </label> */}
      </div>
    
      <div className='setting'>
        <label htmlFor='log-filepath'>Log Filepath </label>
        <input
          id='log-filepath'
          type='text'
          name='log-filepath-string'
          defaultValue={fInput}
          placeholder={filepath}
          onKeyDown={(e) => handleEnterPress(e, 'log-filepath', fInput)}
          onChange={(e) => setfInput(e.target.value)}
          // onClick={handleDirectorySelector}
        />
        {/* <label htmlFor='log-filepath'>{filepath} </label> */}
      </div>

      <div className='setting'>
        <label htmlFor='producers'>Producers </label>
        <input
          id='producers'
          type='number'
          name='producers'
          defaultValue={pInput}
          placeholder={producers}
          onKeyDown={(e) => handleEnterPress(e, 'producers', pInput)}
          onChange={(e) => setPInput(e.target.value)}
        />
        {/* <label htmlFor='producers'> {producers} </label> */}
      </div>

      <div className="setting">
        <label htmlFor='consumers'>Consumers</label>
        <input 
          id="consumers"
          type="number" 
          name="consumers"
          defaultValue={cInput}
          placeholder={consumers}
          onKeyDown={(e) => handleEnterPress(e, 'consumers', cInput)}
          onChange={(e) => setCInput(e.target.value)}
        />
        {/* <label htmlFor="consumers">{consumers}</label> */}
      </div>


      <div className='setting'>
        <label htmlFor='metric-count'>Metric Count</label>
        <input
          id='metric-count'
          name='metric-count'
          type='range'
          min='10'
          max='500'
          step='10'
          defaultValue={metricCount}
          onChange={(e) => setInput(e)}
        ></input>
        <label htmlFor='metric-count'>{metricCount}</label>
      </div>

     
    </div>
  );
};

export default Settings;


{/* <div className='setting'>
<label htmlFor='zookeeper-port'>Zookeeper Port </label>
<input
  // id='zookeeper-port'
  type='text'
  // name='zookeeper-port-num'
  defaultValue={zInput}
  // onKeyDown={(e) => handleEnterPress(e, 'ZOOKEEPER_PORT', zInput)}
  onChange={(e) => setzInput(e.target.value)}
/>
<label htmlFor='kafka-port'> {zookeeper} </label>
</div> */}