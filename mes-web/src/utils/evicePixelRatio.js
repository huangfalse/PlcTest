class DevicePixelRatio {
	constructor() {
	  this.bodyElement = document.getElementsByTagName('body')[0];
	}
	isWindowsSystem() {
	  const agent = navigator.userAgent.toLowerCase();
	  return agent.indexOf("windows") >= 0;
	}
	// 通用事件绑定工具
	addHandler(element, type, handler) {
	  if (element.addEventListener) {
		element.addEventListener(type, handler, false);
	  } else if (element.attachEvent) {
		element.attachEvent("on" + type, handler);
	  } else {
		element["on" + type] = handler;
	  }
	}
	// 校正浏览器缩放比例
	correctZoom() {
	  if (this.bodyElement) {
		this.bodyElement.style.zoom = 1 / window.devicePixelRatio;
	  }
	}
	// 监听页面缩放（使用防抖优化）
	watchResize() {
	  let timer = null;
	  this.addHandler(window, 'resize', () => {
		if (timer) clearTimeout(timer);
		timer = setTimeout(() => {
		  this.correctZoom();
		}, 200);
	  });
	}
	// 初始化页面比例校正
	init() {
	  if (this.isWindowsSystem()) {
		this.correctZoom();
		this.watchResize();
	  }
	}
  }
  export default DevicePixelRatio;