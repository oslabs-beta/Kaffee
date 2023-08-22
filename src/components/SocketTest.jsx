import React, { useEffect, useState } from 'react';
import client from '../socket.js';

export default function SocketTest() {
  const [isConnected, setIsConnected] = useState(false);
  const [events, setEvents] = useState([]);

  useEffect(() => {
    // client.onConnect = (frame) => {
    //   client.subscribe('/app/messages', (message) => {
    //     console.log('Events: ', events);
    //     const allEvents = events.slice();
    //     console.log('All Events: ', allEvents);
    //     setEvents(allEvents.push(message));
    //   });
    // };

    // client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  function handleClick() {
    alert('clicked');
    // client.publish({
    //   destination: '/app/test',
    //   body: JSON.stringify({ name: 'Darren' }),
    // });
  }

  return (
    <>
      <button onClick={handleClick}>Send Message</button>
      <ul>
        {events?.map((event, i) => {
          <li key={i}>{event}</li>;
        })}
      </ul>
    </>
  );
}
