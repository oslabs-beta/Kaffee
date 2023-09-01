import { Client, Message } from '@stomp/stompjs';
const brokerURL = 'ws://localhost:8080/socket';
const options = {
    brokerURL,
    debug: function (message) {
        // console.log(message);
    },
    reconnectDelay: 500,
    heartbeatIncoming: 100,
    heartbeatOutgoing: 100,
};
let client;
export default client = new Client(options);
//# sourceMappingURL=socket.js.map