import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { changeMetricCount } from '../reducers/chartSlice.js';

export async function loader() {
  try {
    const res = await fetch('http://localhost:8080/getSettings');
    const data = await res.json();
    return data;
  } catch (error) {
    throw new Error('Error fetching settings', { cause: error });
  }
}
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
    // console.log(entry.kind, entry.name);
  }
  // console.log(dirArray);
  // const resolvedPath = dirArray.join(sep);

  // const field = document.querySelector('#log-filepath');
  // field.value = directory;
};
const Settings = () => {
  const [kafka, setKafka] = useState();
  const [JMX, setJMX] = useState();
  const [filepath, setFilepath] = useState();

  const [kInput, setkInput] = useState('');
  const [jInput, setjInput] = useState('');
  const [fInput, setfInput] = useState('');
  const [metricTimeout, setMetricTimeout] = useState(null);

  const [producers, setProducers] = useState();
  const [consumers, setConsumers] = useState();
  const [kafkaURL, setKafkaURL] = useState();

  const [kURLInput, setkURLInput] = useState('');
  const [cInput, setCInput] = useState('');
  const [pInput, setPInput] = useState('');

  const metricCount = useSelector((state) => state.charts.metricCount / 10);

  const dispatch = useDispatch();

  useEffect(() => {
    async function setSettings() {
      const data = await loader();
      setKafka(data['KAFKA_PORT']);
      setJMX(data['JMX_PORT']);
      setFilepath(data['log-filepath']);
      setConsumers(data['consumers']);
      setProducers(data['producers']);
      setKafkaURL(data['KAFKA_URL']);
      dispatch(changeMetricCount(data['metric-count'] * 10));
    }
    setSettings();
  }, []);

  const updateSettings = (param, val) => {
    fetch('http://localhost:8080/updateSettings', {
      method: 'POST',
      body: JSON.stringify({
        settingName: param,
        newValue: val,
      }),
      headers: {
        'Content-type': 'application/json; charset=UTF-8',
      },
    }).catch((error) => {
      throw new Error('Error saving updated settings.', { cause: error });
    });
  };

  const handleEnterPress = (e, param, val) => {
    if (e.key === 'Enter') {
      updateSettings(param, val);
      if (param === 'KAFKA_PORT') setkInput('');
      if (param === 'JMX_PORT') setjInput('');
      if (param === 'log-filepath') setfInput('');
      if (param === 'producers') setPInput('');
      if (param === 'consumers') setCInput('');
      if (param === 'KAFKA_URL') setkURLInput('');
    }
  };

  // const handleDirectorySelector = async () => {
  //   const directory = await getDirectory();
  //   if (!directory) {
  //     // user closed the window or otherwise failed to open the file
  //     return;
  //   }

  //   // See: https://developer.chrome.com/articles/file-system-access/
  //   const sep = path.sep;
  //   const dirArray = [];
  //   for await (const entry of directory.values()) {
  //     // console.log(entry.kind, entry.name);
  //   }
  //   // console.log(dirArray);
  //   // const resolvedPath = dirArray.join(sep);

  //   // const field = document.querySelector('#log-filepath');
  //   // field.value = directory;
  // };

  function setInput(e) {
    if (e.target.value < 1) {
      e.target.value = 1;
    }
    dispatch(changeMetricCount(e.target.value * 10));

    if (metricTimeout) {
      clearTimeout(metricTimeout);
    }
    setMetricTimeout(
      setTimeout(() => updateSettings(e.target.id, e.target.value), 500)
    );
  }

  return (
    <div className="settings-container">
      <div className="setting">
        <label htmlFor="kafka-port">Kafka Port </label>
        <input
          id="kafka-port"
          type="number"
          name="kafka-port-num"
          defaultValue={kInput}
          placeholder={kafka}
          onKeyDown={(e) => handleEnterPress(e, 'KAFKA_PORT', kInput)}
          onChange={(e) => setkInput(e.target.value)}
        />
      </div>

      <div className="setting">
        <label htmlFor="kafkaURL">Kafka URL</label>
        <input
          id="kafkaURL"
          type="text"
          name="kafkaURL"
          defaultValue={kURLInput}
          placeholder={kafkaURL}
          onKeyDown={(e) => handleEnterPress(e, 'KAFKA_URL', kURLInput)}
          onChange={(e) => setkURLInput(e.target.value)}
        />
      </div>

      <div className="setting">
        <label htmlFor="JMX-port">JMX Port </label>
        <input
          id="JMX-port"
          type="number"
          name="JMX-port-num"
          defaultValue={jInput}
          placeholder={JMX}
          onKeyDown={(e) => handleEnterPress(e, 'JMX_PORT', jInput)}
          onChange={(e) => setjInput(e.target.value)}
        />
      </div>

      {/*COMMENTED OUT SINCE WE ARE NOT SETTING LOG FILE PATH ANYMORE 
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
          onClick={handleDirectorySelector}
        /> */}
      {/* <label htmlFor='log-filepath'>{filepath} </label> */}
      {/* </div> */}

      <div className="setting">
        <label htmlFor="metric-count">Chart Interval</label>
        <div className="range">
          <input
            id="metric-count"
            name="metric-count"
            type="range"
            min="0"
            max="60"
            step="5"
            value={metricCount}
            onChange={(e) => setInput(e)}
          ></input>

          <label className="range-label">{metricCount} Seconds</label>
        </div>
      </div>

<hr />

      <div className="setting">
        <label htmlFor="producers"># Test Producers</label>
        <input
          id="producers"
          type="number"
          name="producers"
          defaultValue={pInput}
          placeholder={producers}
          onKeyDown={(e) => handleEnterPress(e, 'producers', pInput)}
          onChange={(e) => setPInput(e.target.value)}
        />
      </div>

      <div className="setting">
        <label htmlFor="consumers"># Test Consumers</label>
        <input
          id="consumers"
          type="number"
          name="consumers"
          defaultValue={cInput}
          placeholder={consumers}
          onKeyDown={(e) => handleEnterPress(e, 'consumers', cInput)}
          onChange={(e) => setCInput(e.target.value)}
        />
      </div>
    </div>
  );
};

export default Settings;
