import { engine, socketUrl } from "./constants";
import AES from "crypto-js/aes";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
/* eslint-disable */
let stompClient: any = null;
let reloadCallback: any = null;
let socket: any = null;
let interval: any = null;

const BPM_BASE_URL_SOCKET_IO = localStorage.getItem("bpmApiUrl")
  ? localStorage.getItem("bpmApiUrl")?.replace(`/${engine}`, `/${socketUrl}`)
  : "";
const token: any = localStorage.getItem("authToken");

const isConnected = () => {
  return stompClient?.connected || null;
};

const clientConnectCallback = () => {
  console.log(
    "is connected inside clientConnectCallback========>>>>>",
    isConnected()
  );
  if (isConnected()) {
    clearInterval(interval);
    console.log("inside connect callback==>>");
    stompClient.subscribe("/topic/task-event", function(output: any) {
      const taskUpdate = JSON.parse(output.body);
      reloadCallback(taskUpdate.id, taskUpdate?.eventName);
    });
  }
};

const clientErrorCallback = (error: string) => {
  console.log("error==>>", error);
  console.log("inside error callback is connected ==>>", isConnected());
  stompClient = Stomp.over(socket);
  // setTimeout(clientConnectCallback, 100, reloadCallback);
  console.log("reconnecting in 5000 ms =>>>>");
  interval = setInterval(connectClient, 5000);
};

const connectClient = () => {
  console.log("STOMP: Attempting connection");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, clientConnectCallback, clientErrorCallback);
};
const connect = (encryptKey: any, reloadCallbacks: any) => {
  reloadCallback = reloadCallbacks;
  const accessToken = AES.encrypt(token, encryptKey).toString();
  const socketUrl = `${BPM_BASE_URL_SOCKET_IO}?accesstoken=${accessToken}`;
  socket = new SockJS(socketUrl);

  /* eslint-disable no-debugger, no-console */
  console.log("socketUrl------>>", socketUrl);
  console.log(" Stomp.over==>>");
  stompClient = Stomp.over(socket);
  console.log("socket-->>", socket);
  stompClient.debug = null;
  // stompClient.connect({}, function() {
  //   if (isConnected()) {
  //     stompClient.subscribe("/topic/task-event", function(output: any) {
  //       const taskUpdate = JSON.parse(output.body);
  //       reloadCallback(taskUpdate.id, taskUpdate?.eventName);
  //     });
  //   }
  // });
  connectClient();
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
