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
  if (isConnected()) {
    stompClient.subscribe("/topic/task-event", function(output: any) {
      const taskUpdate = JSON.parse(output.body);
      reloadCallback(taskUpdate.id, taskUpdate?.eventName);
    });
  }
};

const clientErrorCallback = (reloadCallback: any, socket: any) => {
  // stompClient = Stomp.overTCP(socket, 61612);
  console.log("herrrrrrrrrrrrr erorrrr");
  stompClient = Stomp.over(socket);
  // setTimeout(clientConnectCallback, 100, reloadCallback);
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
  console.log(socket.url, "++++++++++++");
  stompClient = Stomp.over(socket);
  console.log(stompClient, "++++++++3333333333333333333333++++");
  stompClient.debug = null;
  connectClient(reloadCallback, socket);
};

const disconnect = () => {
  stompClient.disconnect();
};

const SocketIOService = {
  connect,
  disconnect,
  isConnected,
};

export default SocketIOService;
