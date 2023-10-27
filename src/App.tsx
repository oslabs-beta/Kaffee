import './App.css';

import { useEffect, useMemo, useState, createContext } from 'react';
import { Outlet } from 'react-router-dom';
import NavBar from './containers/NavBar.jsx';
import { changeMetricCount } from './reducers/chartSlice.js';
import { useDispatch } from 'react-redux';
import { Client } from '@stomp/stompjs';
import {
  setJmxPort,
  setKafkaPort,
  setLogFilepath,
  setZookeeperPort,
} from './reducers/settingSlice.js';

import data from './dev-settings/settings.json';

// OLD NODE ROUTE - UNCOMMENT AFTER JAVA ROUTE IS MADE

// export async function loader() {
//   try {
//     const res = await fetch('http://localhost:3030/getSettings');
//     const data = await res.json();
//     return data;
//   } catch (error) {
//     throw new Error('Error fetching settings', { cause: error });
//   }
// }

export const SocketContext = createContext<{ client: Client | null } | null>(
  null
);

const App = () => {
  // this is a holdover from using the new react-router methods in 6.4
  // const data = useLoaderData();
  const dispatch = useDispatch();

  const [client, setClient] = useState<Client | null>(null);

  async function socketConnect() {
    const brokerURL = 'ws://localhost:8080/socket';
    const options = {
      brokerURL,
      reconnectDelay: 500,
      heartbeatIncoming: 100,
      heartbeatOutgoing: 100,
    };

    const client = new Client(options);
    client.activate();
    setClient(client);
  }

  // check that we have metric-count and set it in the app
  async function setData() {
    //UNCOMMENT LATER
    // const data = { settings: null };
    // data.settings = await loader();

    // CHANGE LATER

    if (data.settings['metric-count']) {
      dispatch(changeMetricCount(data.settings['metric-count'] * 10));
    }
    if (data.settings['kafka-port']) {
      dispatch(setKafkaPort(data.settings['kafka-port']));
    }
    if (data.settings['zookeeper-port']) {
      dispatch(setZookeeperPort(data.settings['zookeeper-port']));
    }
    if (data.settings['JMX-port']) {
      dispatch(setJmxPort(data.settings['JMX-port']));
    }
    if (data.settings['log-filepath']) {
      dispatch(setLogFilepath(data.settings['log-filepath']));
    }
  }

  useEffect(() => {
    setData();

    socketConnect();
  }, []);

  const contextValue = useMemo(() => ({ client }), [client]);

  return (
    <>
      <NavBar />
      <SocketContext.Provider value={contextValue}>
        <div id="main">
          <Outlet />
        </div>
      </SocketContext.Provider>
    </>
  );
};

export default App;
