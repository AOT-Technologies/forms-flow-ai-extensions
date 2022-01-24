import {
  engine, socketUrl 
} from "./Constants";
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

const getBpmUrl = () => {
  return localStorage.getItem("bpmApiUrl");
};

const isConnected = () => {
  return stompClient?.connected || null;
};

const clientConnectCallback = () => {
  if (isConnected()) {
    clearTimeout(interval);
    stompClient.subscribe("/topic/task-event", function (output: any) {
      const taskUpdate = JSON.parse(output.body);
      /* taskUpdate format
           {id:"taskId", eventName:"complete/update/create"}
           On Complete/ create the pagination can change so Would need a refresh
           For update can assume with current filter only if the same list has the taskId available of the updated one then only Refresh.
           (Would fail in filter/Search on the Same params)
        */
      reloadCallback(taskUpdate.id, taskUpdate?.eventName);
    });
  }
};

/*main connect function which is being getting called by a component*/
function connectClient () {
  if (getBpmUrl() && localStorage.getItem("authToken")) {
    const BPM_BASE_URL_SOCKET_IO = getBpmUrl()?.replace(
      `/${engine}`,
      `/${socketUrl}`
    );
    const accessToken = AES.encrypt(localStorage.getItem("authToken")!, encryptKey).toString();
    const websocketUrl = `${BPM_BASE_URL_SOCKET_IO}?accesstoken=${accessToken}`;

    socket = new SockJS(websocketUrl);
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect({
    }, clientConnectCallback, clientErrorCallback);
  } else {
    clientErrorCallback("bpmApiUrl not set", true);
    reloadCallback(null, null, true);
  }
}

clientErrorCallback = (error: string, flag?: boolean) => {
  if (!flag) stompClient = Stomp.over(socket);
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

export const SocketIOService = {
  connect,
  disconnect,
  isConnected,
};

