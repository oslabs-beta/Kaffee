import React, { useEffect, useState } from "react";

const History = () => {
const [kafka, setKafka] = useState();
const [zookeeper, setZookeeper] = useState();
const [JMX, setJMX] = useState();
const [filepath, setFilepath] = useState();
const [kInput, setkInput] = useState('');
const [zInput, setzInput] = useState('');
const [jInput, setjInput] = useState('');
const [fInput, setfInput] = useState('');
const [update, setUpdate] = useState(false);

const fetchSettings = () => {
  fetch('http://localhost:3030/getSettings')
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  })
  .then(data => {
    console.log(data, 'this is the fetch call');
    setKafka(data['kafka-port']);
    setZookeeper(data['zookeeper-port']);
    setJMX(data['JMX-port']);
    setFilepath(data['log-filepath']);
    console.log(filepath)
  })
  .catch(error => {
    console.error('Fetch error:', error);
  });
}

  const updateSettings = (param, val) => {
    if (param === 'kafka-port' || param === 'zookeeper-port' || param === 'JMX-port') {
      val = Number(val);
    }
    fetch('http://localhost:3030/updateSettings', {
      method: 'POST',
      body: JSON.stringify({
        "settingName": param,
        "newValue": val,
      }),
      headers: {
        "Content-type": "application/json; charset=UTF-8",
    }
    })
    .then(response => response.json())
    .then(data => console.log(data))
    .then(fetchSettings)
    .catch(error => console.error('Error:', error));
  }

  fetchSettings()

  const handleEnterPress = (e, param, val) => {
    if (e.key === 'Enter') {
      updateSettings(param, val);
      if (param === 'kafka-port') setkInput('');
      if (param === 'zookeeper-port') setzInput('');
      if (param === 'JMX-port') setjInput('');
      if (param === 'log-filepath') setfInput('');
    }
  }

  return (
    <div>

      <div className="settings-container">
        <div className="setting">
          <label htmlFor="kafka-port">Kafka Port </label>
          <input id="kafka-port" type="text" name="kafka-port-num" value={kInput} onKeyDown={(e) => handleEnterPress(e, 'kafka-port', kInput)} onChange={(e) => setkInput(e.target.value)}/>
          <label htmlFor="kafka-port"> {kafka} </label>
        </div>
        <div className="setting">
          <label htmlFor="zookeeper-port">Zookeeper Port </label>
          <input id="zookeeper-port" type="text" name="zookeeper-port-num" value={zInput} onKeyDown={(e) => handleEnterPress(e, 'zookeeper-port', zInput)} onChange={(e) => setzInput(e.target.value)}/>
          <label htmlFor="kafka-port"> {zookeeper} </label>
        </div>
        <div className="setting">
          <label htmlFor="JMX-port">JMX Port </label>
          <input id="JMX-port" type="text" name="JMX-port-num" value={jInput} onKeyDown={(e) => handleEnterPress(e, 'JMX-port', jInput)} onChange={(e) => setjInput(e.target.value)}/>
          <label htmlFor="kafka-port"> {JMX} </label>
        </div>
        <div className="setting">
          <label htmlFor="log-filepath">Log Filepath </label>
          <input id="log-filepath" type="text" name="log-filepath-string" value={fInput} onKeyDown={(e) => handleEnterPress(e, 'log-filepath', fInput)} onChange={(e) => setfInput(e.target.value)}/>
          <label htmlFor="kafka-port">{filepath} </label>
        </div>
      </div>

    </div>
  )
};

export default History;
