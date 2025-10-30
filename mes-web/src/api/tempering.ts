// src/api/temperingGlass.ts
import request from '@/utils/request'

export interface TemperingGlassItem {
  // 定义你的数据项接口属性
    deviceId: number;
    glassId: number;
    engineerId: number;
    temperingLayoutId: number;
  id: number;
  width: number;
  height: number;
  angle: number;
  xcoordinate: number;
  ycoordinate: number;
  process_id: string;
  project_no: string;
  sort: string;
}
export interface TemperingGlassGroup {
  groupName: string;
  items: TemperingGlassItem[];
}
// 点击查询钢化版图
export const fetchTemperingGlassData = async (engineerId: string): Promise<TemperingGlassGroup[]> => {
  try {
    const response = await request.get('/temperingGlass/temperingGlassQueueInfo/queryTempLayoutByEngineerId?engineerId='+engineerId);
    if (response.code === 200 && response.data) {
      // 处理返回的数据，将每组数据转换为包含组名和项的对象数组
      return Object.entries(response.data).map(([groupName, items]) => ({
        groupName,
        items: items.map(item => ({
          ...item,
          x_axis: 0,
          y_axis: 0
        }))
      }));
    }
    throw new Error(response.message || 'Unknown error');
  } catch (error) {
    console.error('API请求失败:', error);
    throw error;
  }
};
// 调用钢化前中后版图
export const fetchAllMessage = async () => {
  try {
    const response = await request.post('/temperingGlass/TempJob/queryAllMessage', {
      deviceId: 1
    });
    if (response.code === 200 && response.data) {
         return {
        data: response.data
      };
    }
    throw new Error(response.message || 'Unknown error');
  } catch (error) {
    throw error;
  }
};
// 洛阳钢化前中版图
export const fetchLyBeMessage = async () => {
  try {
    const response = await request.post('/temperingGlass/TempJob/queryAllMessage', {
      deviceId: 1,
      temperingStage: 1
    });
    if (response.code === 200 && response.data) {
         return {
        data: response.data
      };
    }
    throw new Error(response.message || 'Unknown error');
  } catch (error) {
    throw error;
  }
};
// 洛阳钢化后版图
export const fetchLyMessage = async () => {
  try {
    const response = await request.post('/temperingGlass/TempJob/queryAllMessage', {
      deviceId: 1,
      temperingStage: 3
    });
    if (response.code === 200 && response.data) {
         return {
        data: response.data
      };
    }
    throw new Error(response.message || 'Unknown error');
  } catch (error) {
    throw error;
  }
};
export const updateTempQueueState = async (id: string | number, state: number) => {
  try {
    const response = await request.post('/temperingGlass/temperingGlassQueueInfo/updateTempQueueState', {
      id,
      state
    });
    if (response.code !== 200) {
      throw new Error(response.message || '更新队列状态失败');
    }
    return response.data;
  } catch (error) {
      console.error('更新队列状态API请求失败:', error);
      throw error;
  }
};
// 洛阳钢化破损
export const updateState = async (deviceId: string | number, glassId: number, engineerId: number, temperingLayoutId: number) => {
    try {
        const response = await request.post('/temperingGlass/temperingGlassQueueInfo/updateTempQueueState', {
            deviceId,
            glassId,
            engineerId,
            temperingLayoutId,
            state: 8
        });
        if (response.code !== 200) {
            throw new Error(response.message || '更新队列状态失败');
        }
        return response.data;
    } catch (error) {
        console.error('更新队列状态API请求失败:', error);
        throw error;
    }
};
// 洛阳下片完成
export const confirmPieceComplete = async (engineerId: string | number, temperingLayoutId: number) => {
    try {
        const response = await request.post('/temperingGlass/temperingGlassQueueInfo/updateTempQueueState', {
            deviceId: 1,
            state: 4,
            engineerId,
            temperingLayoutId
        });
        if (response.code !== 200) {
            throw new Error(response.message || '更新队列状态失败');
        }
        return response.data;
  } catch (error) {
    console.error('更新队列状态API请求失败:', error);
    throw error;
  }
};