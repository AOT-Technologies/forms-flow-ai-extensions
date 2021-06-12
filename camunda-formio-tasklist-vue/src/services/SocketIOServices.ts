import { engine, socketUrl } from "../services/constants";
import AES from "crypto-js/aes";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

let stompClient: any = null;
const BPM_BASE_URL_SOCKET_IO = localStorage.getItem("bpmApiUrl")
  ? localStorage.getItem("bpmApiUrl")?.replace(`/${engine}`, `/${socketUrl}`)
  : "";
const token: any = localStorage.getItem("authToken");

const isConnected = () => {
  return stompClient?.connected || null;
};

const connect = (encryptKey: any, reloadCallback: any) => {
  const accessToken = AES.encrypt(token, encryptKey).toString();
  const socketUrl = `${BPM_BASE_URL_SOCKET_IO}?accesstoken=${accessToken}`;
  const socket = new SockJS(socketUrl);
  stompClient = Stomp.over(socket);
  stompClient.debug = null;
  stompClient.connect({}, function () {
    if (isConnected()) {
      stompClient.subscribe("/topic/task-event", function (output: any) {
        const taskUpdate = JSON.parse(output.body);
        reloadCallback(taskUpdate.id, taskUpdate?.eventName);
      });
    }
  });
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
