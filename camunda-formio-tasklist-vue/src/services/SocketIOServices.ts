import { engine, socketUrl } from "./constants";
import AES from "crypto-js/aes";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const BPM_BASE_URL_SOCKET_IO = localStorage.getItem("bpmApiUrl")
  ? localStorage.getItem("bpmApiUrl")?.replace(`/${engine}`, `/${socketUrl}`)
  : "";
let websocketUrl: string = "";
const token: string = localStorage.getItem("authToken") ?? "";

const prepareUrl = (url: string) => {
  const a = document.createElement("a");
  a.href = url;
  /* eslint-disable no-debugger, no-console */
  if (a.protocol === "https:") return url.replace("https://", "wss://");
  else return url.replace("http://", "ws://");
};
const stompClient = () => {
  return new Client({
    brokerURL: websocketUrl,
    connectHeaders: {},
    debug: function(str: string) {
      console.log(str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  });
};

let client = stompClient();

const isConnected = () => {
  return client.connected;
};

const connect = (encryptKey: string, reloadCallback: any) => {
  const accessToken = AES.encrypt(token, encryptKey).toString();
  const socketUrl = `${BPM_BASE_URL_SOCKET_IO}?accesstoken=${accessToken}`;
  const socket = new SockJS(socketUrl);
  console.log(socket.url, "++sockett");
  websocketUrl = prepareUrl(socket.url);
  console.log(
    new WebSocket(websocketUrl),
    "+++++++++++++++++++++@@@@@@@@@@@@@@@@@"
  );
  client = stompClient();
  client.activate();
  client.onConnect = function(frame: any) {
    // Do something, all subscribes must be done is this callback
    // This is needed because this will be executed after a (re)connect
    client.subscribe("/topic/task-event", function(output: any) {
      const taskUpdate = JSON.parse(output.body);
      reloadCallback(taskUpdate.id, taskUpdate?.eventName);
    });
  };

  client.onStompError = function(frame) {
    // Will be invoked in case of error encountered at Broker
    // Bad login/passcode typically will cause an error
    // Complaint brokers will set `message` header with a brief message. Body may contain details.
    // Compliant brokers will terminate the connection after any error
    console.log("Broker reported error: " + frame.headers["message"]);
    console.log("Additional details: " + frame.body);
  };
};

const disconnect = () => {
  client.deactivate();
};

const SocketIOService = {
  connect,
  disconnect,
  isConnected,
};

export default SocketIOService;
