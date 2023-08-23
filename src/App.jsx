import React, {useEffect} from 'react';
import { Outlet } from 'react-router-dom';
import NavBar from './containers/NavBar';
import { setClient } from './reducers/socketSlice';
import client from './socket.js';
import { useDispatch, useSelector} from 'react-redux';

export default function App() {
  const socketClient = useSelector(state => state.sockets.client);

  const dispatch = useDispatch();

  useEffect(() => {  

    client.activate();

    return () => {
      client.deactivate();
    }
  }, []);

  return (
    <>
      <NavBar />
      <div id='main'>
        <Outlet />
      </div>
    </>
  );
}
