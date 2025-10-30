let socket = null;
export const initializeWebSocket = (socketUrl, messageHandler) => {
  let messages='' ;
    if (typeof WebSocket === "undefined") {
      return null;
    }
    const socket = new WebSocket(socketUrl);
    socket.onopen = function () {
    };
    socket.onmessage = (msg) => {
      if (!msg.data) {
        return;
      }
      const isLastChunk = msg.data.endsWith('<END>');
      //是否为最后一块消息
      messages += msg.data;
      if(isLastChunk||msg.data.length<50000) {
        messages= messages.replace('<END>', '');
        const obj = JSON.parse(messages);
        messages='';
      // 调用消息处理函数，将数据传递给 Vue 组件
      if (messageHandler) {
        messageHandler(obj);
      }
      }
    };
    return socket;
  };
  export const closeWebSocket = (socket) => {
    if (socket && socket.readyState === WebSocket.OPEN) {
        socket.close();
    }
};

