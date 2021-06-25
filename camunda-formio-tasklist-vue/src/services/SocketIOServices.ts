import { engine, socketUrl } from "./constants";
import AES from "crypto-js/aes";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

let stompClient: any = null;
let reloadCallback: any = null;
let socket: any = null;
let encryptKey: any = null;
let interval: any = null;
let clientErrorCallback: any=null;

const BPM_BASE_URL_SOCKET_IO = localStorage.getItem("bpmApiUrl")
  ? localStorage.getItem("bpmApiUrl")?.replace(`/${engine}`, `/${socketUrl}`)
  : "";
const token: any = localStorage.getItem("authToken");

const isConnected = () => {
  return stompClient?.connected || null;
};

const clientConnectCallback = () => {
  if (isConnected()) {
    clearTimeout(interval);
    stompClient.subscribe("/topic/task-event", function(output: any) {
      const taskUpdate = JSON.parse(output.body);
      reloadCallback(taskUpdate.id, taskUpdate?.eventName);
    });
  }
};



function connectClient () {
  const accessToken = AES.encrypt(token, encryptKey).toString();
  const socketUrl = `${BPM_BASE_URL_SOCKET_IO}?accesstoken=${accessToken}`;

  socket = new SockJS(socketUrl);
  stompClient = Stomp.over(socket);
  // stompClient.debug = null;
  stompClient.connect({}, clientConnectCallback, clientErrorCallback);
}

clientErrorCallback = (error: string) => {
  /* eslint-disable no-debugger, no-console */
  console.log("error==>>", error);
  stompClient = Stomp.over(socket);
  
  // interval = setInterval(connectClient, 10000);
  interval = setTimeout(connectClient, 5000);
};

const connect = (encryptionKey: any, reloadCallbacks: any) => {
  reloadCallback = reloadCallbacks;
  encryptKey = encryptionKey;
  connectClient();
};

const disconnect = () => {
  clearTimeout(interval);
  stompClient.disconnect();
};

const SocketIOService = {
  connect,
  disconnect,
  isConnected,
};

export default SocketIOService;
