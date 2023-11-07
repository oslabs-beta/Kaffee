import React, { useEffect, useState, useRef, useMemo } from 'react';
import client from '../utils/socket.js';
import { StompSocketState } from '@stomp/stompjs';
import { useDispatch, useSelector } from 'react-redux';

export default function SocketTest() {
  const [subs, setSubs] = useState([]);
  const [events, setEvents] = useState([]);
  const [subId, setSubId] = useState(null);

  useEffect(() => {
    client.activate();
    client.onConnect = () => {
      handleSubscription('/metric/subscriptions');
    };
  }, []);

  function handleClick() {
    const input = document.querySelector('#metricName');
    client.publish({
      destination: '/app/subscribe',
      body: JSON.stringify({ metric: input.value }),
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
    <div id="charts">
      <input type="text" id="metricName" name="metricName"></input>
      <button onClick={handleClick}>Send Message</button>
      <ul>
        {events?.map((event, i) => {
          return <li key={i}>{event.body}</li>;
        })}
      </ul>
    </div>
  );
}
