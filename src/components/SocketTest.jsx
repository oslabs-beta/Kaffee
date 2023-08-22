import React, { useEffect, useState, useRef, useMemo } from 'react';
import client from '../socket.js';
import { StompSocketState } from '@stomp/stompjs';
import { useDispatch, useSelector } from 'react-redux';
import { connected, disconnected, setClient } from '../reducers/socketSlice.js';

export default function SocketTest() {
  const socketStatus = useSelector((state) => state.sockets.status);
  const socketClient = useSelector((state) => state.sockets.client);

  const [subs, setSubs] = useState([]);
  const [events, setEvents] = useState([]);
  const [subId, setSubId] = useState(null);

  const dispatch = useDispatch();
  const subStr = '/metric/messages';

  useEffect(() => {
    client.activate();
    client.onConnect = () => {
      handleSubscription('/metric/messages');
    };
  }, []);

  function handleClick() {
    client.publish({
      destination: '/app/sendTest',
      body: JSON.stringify({ name: 'Darren' }),
    });
  }

  function addEvent(message) {
    const body = JSON.parse(message.body);
    events.push(body);
    setEvents([...events]);
  }

  function handleSubscription(subStr = '/metric/messages') {
    for (const sub of subs) {
      if (sub.destination == subStr) {
        return;
      }
    }

    const subId = client.subscribe(subStr, (message) => addEvent(message));

    subs.push(subId);
    setSubs([...subs]);
  }

  function handleSub() {
    if (!subId) {
      handleSubscription('/metric/chuck');
      const currSub = subs[subs.length - 1].id;
      setSubId(currSub);
    } else {
      setEvents([]);
      client.unsubscribe(subId);
      setSubId(null);
    }
  }

  return (
    <div id='charts'>
      <button onClick={handleClick}>Send Message</button>
      <button onClick={handleSub}>Subscribe</button>
      <ul>
        {events?.map((event, i) => {
          return <li key={i}>{event.content}</li>;
        })}
      </ul>
    </div>
  );
}
