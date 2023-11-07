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
    if (
      param === 'KAFKA_PORT' ||
      param === 'JMX_PORT' ||
      param === 'metric-count' ||
      param === 'producers' ||
      param === 'consumers'
    ) {
      val = Number(val);
    }
    fetch('http://localhost:8080/updateSettings', {
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
      .catch((error) => {
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
          readOnly={true}
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
          readOnly={true}
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
          readOnly={true}
          defaultValue={jInput}
          placeholder={JMX}
          onKeyDown={(e) => handleEnterPress(e, 'JMX_PORT', jInput)}
          onChange={(e) => setjInput(e.target.value)}
        />
      </div>

      <div className="setting">
        <label htmlFor="producers">Producers </label>
        <input
          id="producers"
          type="number"
          name="producers"
          readOnly={true}
          defaultValue={pInput}
          placeholder={producers}
          onKeyDown={(e) => handleEnterPress(e, 'producers', pInput)}
          onChange={(e) => setPInput(e.target.value)}
        />
      </div>

      <div className="setting">
        <label htmlFor="consumers">Consumers</label>
        <input
          id="consumers"
          type="number"
          name="consumers"
          readOnly={true}
          defaultValue={cInput}
          placeholder={consumers}
          onKeyDown={(e) => handleEnterPress(e, 'consumers', cInput)}
          onChange={(e) => setCInput(e.target.value)}
        />
      </div>

      <div className="setting">
        <label htmlFor="metric-count">Seconds of Data Displayed</label>
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
    </div>
  );
};

export default Settings;
