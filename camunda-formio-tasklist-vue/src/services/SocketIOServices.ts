import { engine, socketUrl } from "../services/constants";
import AES from "crypto-js/aes";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

let stompClient: any = null;
let reloadCallback: any = null;
let socket: any = null;
let encryptKey: any = null;
let interval: any = null;
let clientErrorCallback: any = null;
let disconnect: any = null;

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
const getBpmUrl = () => {
  return localStorage.getItem("bpmApiUrly");
};
function connectClient() {
  if (getBpmUrl()) {
    const BPM_BASE_URL_SOCKET_IO = getBpmUrl()?.replace(
      `/${engine}`,
      `/${socketUrl}`
    );
    const accessToken = AES.encrypt(token, encryptKey).toString();
    // const serviceBCURL = "https://test-sbc-ffa-bpm.apps.silver.devops.gov.bc.ca/camunda/forms-flow-bpm-socket"
    const websocketUrl = `${BPM_BASE_URL_SOCKET_IO}?accesstoken=${accessToken}`;
    // const websocketUrl = `${serviceBCURL}?accesstoken=${accessToken}`;

    socket = new SockJS(websocketUrl);
    stompClient = Stomp.over(socket);
    // stompClient.debug = null;
    stompClient.connect({}, clientConnectCallback, clientErrorCallback);
  } else {
    console.log("bpmApiUrl not set");
    reloadCallback(null, null, true);
  }
}

clientErrorCallback = (error: string) => {
  /* eslint-disable no-debugger, no-console */
  console.log("error==>>", error);
  stompClient = Stomp.over(socket);

  // interval = setInterval(connectClient, 10000);
  interval = setTimeout(connectClient, 10000);
};

const connect = (encryptionKey: any, reloadCallbacks: any) => {
  reloadCallback = reloadCallbacks;
  encryptKey = encryptionKey;
  connectClient();
};

disconnect = () => {
  clearTimeout(interval);
  stompClient.disconnect();
};

const SocketIOService = {
  connect,
  disconnect,
  isConnected,
};

export default SocketIOService;
