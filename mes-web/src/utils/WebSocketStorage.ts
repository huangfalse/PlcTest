class WebSocketService {
  private static instance: WebSocketService
  private socket: WebSocket 
  private listeners: Set<(data: any) => void> = new Set()
    private url: string = `ws://${window.ipConfig.serverUrl}/glassStorage/webSocket/rawGlassStorage`
  private isManualClose: boolean = false
  private reconnectAttempts: number = 0
  private maxReconnectAttempts: number = 5
  private reconnectDelay: number = 1000 // 1秒
  private constructor() {
    this.connect()
  }
  public static getInstance(): WebSocketService {
    if (!WebSocketService.instance) {
      WebSocketService.instance = new WebSocketService()
    }
    return WebSocketService.instance
  }
  private connect() {
    // 清除现有连接（如果存在）
    if (this.socket) {
      this.socket.onopen 
      this.socket.onmessage 
      this.socket.onclose 
      this.socket.onerror 
    }
    this.socket = new WebSocket(this.url)
    this.socket.onopen = () => {
      console.log('WebSocket connected')
      this.reconnectAttempts = 0 // 重置重连计数
    }
    this.socket.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data)
        this.listeners.forEach(listener => {
          try {
            listener(data)
          } catch (error) {
          }
        })
      } catch (parseError) {
      }
    }
    this.socket.onclose = () => {
      console.log('WebSocket disconnected')
      if (!this.isManualClose && this.reconnectAttempts < this.maxReconnectAttempts) {
        this.reconnectAttempts++
        console.log(`Attempting to reconnect (${this.reconnectAttempts}/${this.maxReconnectAttempts})...`)
        setTimeout(() => this.connect(), this.reconnectDelay)
      } else {
        this.isManualClose = false // 重置标志
      }
    }
    this.socket.onerror = (error) => {
    }
  }
  public addListener(listener: (data: any) => void) {
    this.listeners.add(listener)
    // 返回一个移除监听器的函数，方便调用者清理
    return () => this.removeListener(listener)
  }
  public removeListener(listener: (data: any) => void) {
    this.listeners.delete(listener)
  }
  public send(data: any) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      try {
        this.socket.send(JSON.stringify(data))
      } catch (error) {
      }
    } else {
      console.warn('WebSocket is not connected, message not sent')
    }
  }
  public close() {
    this.isManualClose = true
    if (this.socket) {
      this.socket.close(1000, 'Normal closure')
      this.socket 
    }
    this.listeners.clear()
  }
  // 添加一个方法来检查连接状态
  public isConnected(): boolean {
    return this.socket?.readyState === WebSocket.OPEN
  }
}
export default WebSocketService