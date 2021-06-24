import { engine, socketUrl } from "./constants";
import AES from "crypto-js/aes";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
/* eslint-disable */
let stompClient: any = null;
const BPM_BASE_URL_SOCKET_IO = localStorage.getItem("bpmApiUrl")
  ? localStorage.getItem("bpmApiUrl")?.replace(`/${engine}`, `/${socketUrl}`)
  : "";
const token: any = localStorage.getItem("authToken");

const isConnected = () => {
  return stompClient?.connected || null;
};

const clientConnectCallback = (reloadCallback: any) => {
  console.log(
    "is connected inside clientConnectCallback========>>>>>",
    isConnected()
  );
  if (isConnected()) {
    console.log("inside connect callback==>>");
    stompClient.subscribe("/topic/task-event", function(output: any) {
      const taskUpdate = JSON.parse(output.body);
      reloadCallback(taskUpdate.id, taskUpdate?.eventName);
    });
  }
};

const clientErrorCallback = (reloadCallback: any, socket: any) => {
  console.log("inside error callback is connected ==>>", isConnected());
  stompClient = Stomp.over(socket);
  // setTimeout(clientConnectCallback, 100, reloadCallback);
  console.log("reconnecting in 1000 ms =>>>>");
  setTimeout(connectClient, 1000, reloadCallback, socket);
};
const connectClient = (reloadCallback: any, socket: any) => {
  stompClient.connect(
    {},
    function() {
      clientConnectCallback(reloadCallback);
    },
    function() {
      console.log("error");
      clientErrorCallback(reloadCallback, socket);
    }
  );
};
const connect = (encryptKey: any, reloadCallback: any) => {
  const accessToken = AES.encrypt(token, encryptKey).toString();
  const socketUrl = `${BPM_BASE_URL_SOCKET_IO}?accesstoken=${accessToken}`;
  const socket = new SockJS(socketUrl);
  /* eslint-disable no-debugger, no-console */
  console.log("socketUrl------>>", socketUrl);
  console.log(" Stomp.over==>>");
  stompClient = Stomp.over(socket);
  console.log("socket-->>", socket);
  stompClient.debug = null;
  connectClient(reloadCallback, socket);
};

const disconnect = () => {
  console.log("===============disconnected========");
  stompClient.disconnect();
};

const SocketIOService = {
  connect,
  disconnect,
  isConnected,
};

export default SocketIOService;
