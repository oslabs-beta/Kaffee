import React, { useEffect, useState } from 'react';
import HistoryItem from '../components/HistoryItem.jsx';


async function getLogFiles() {
  const res = await fetch('http://localhost:3030/getLogFiles');
  const data = await res.json();

  return data;
}

const History = () => {
  const [logs, setLogs] = useState([]);

  useEffect(() => {
    const getLogs = async () => {
      const logs = await getLogFiles();
      setLogs(logs);
    };
    getLogs();
  }, []);

  return (
    <div className='history'>
      {logs.length ? (
        logs.map((log, i) => (
          <HistoryItem
            data={log}
            key={i}
          />
        ))
      ) : (
        <></>
      )}
    </div>
  );
};

export default History;
