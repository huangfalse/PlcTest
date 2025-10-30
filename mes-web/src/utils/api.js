import request from '../utils/request';
import { ElMessage } from 'element-plus';
const BASE_URL = 'glassStorage/api/rawUsage';
const taskBASE_URL = 'glassStorage/api/storageTask';
export const findTasks = () => {
  const url = `glassStorage/api/storageTask/findTasks`;
  const options = {
    method: 'get',
    url,
    json: true,
  };
  return new Promise((resolve, reject) => {
    request(options, (error, response, body) => {
      if (error) {
        reject(new Error(error.message));
      } else {
        resolve(body);
      }
    });
  });
};
export const findList = (params) => {
  const url = `${BASE_URL}/findList`;
  const options = {
    method: 'post',
    url,
    json: true,
    body: params
  };
  return new Promise((resolve, reject) => {
    request(options, (error, response, body) => {
      if (error) {
        reject(new Error(error.message));
      } else {
        resolve(body);
      }
    });
  });
};
export const findById = (id) => {
  const url = `${BASE_URL}/${id}`;
  const options = {
    method: 'get',
    url,
    json: true
  };
  return new Promise((resolve, reject) => {
    request(options, (error, response, body) => {
      if (error) {
        reject(new Error(error.message));
      } else {
        resolve(body);
      }
    });
  });
};
export const insertRawUsage = (rawUsage) => {
  const url = `${BASE_URL}`;
  const options = {
    method: 'post',
    url,
    json: true,
    body: rawUsage
  };

  return new Promise((resolve, reject) => {
    request(options, (error, response, body) => {
      if (error) {
        reject(new Error(error.message));
      } else {
        resolve(body);
      }
    });
  });
};
export const updateRawUsage = async (rawUsage) => {
  try {
    const response = await request.post(`${BASE_URL}/updaterawUsage`, rawUsage);
    ElMessage.success('修改成功');
    return response.data;
  } catch (error) {
    ElMessage.error('修改失败');
    throw error;
  }
};
export const deleteRawUsage = async (id) => {
  try {
    const response = await request.post(`${BASE_URL}/id`, id);
    ElMessage.success('删除原始使用数据成功');
    return response.data;
  } catch (error) {
    ElMessage.error('删除原始使用数据失败');
    throw error;
  }
};
export const inStorage = async (RawUsageAndShelfRack) => {
  try {
    const response = await request.post(`${BASE_URL}/inStorage`, RawUsageAndShelfRack);
    ElMessage.success('入库操作成功');
    return response.data;
  } catch (error) {
    ElMessage.error('入库操作失败');
    throw error;
  }
};
export const outStorage = async (RawUsageAndShelfRack) => {
  try {
    const response = await request.post(`${BASE_URL}/outStorage`, RawUsageAndShelfRack);
    ElMessage.success('出库操作成功');
    return response.data;
  } catch (error) {
    ElMessage.error('出库操作失败');
    throw error;
  }
};
export const taskUpdate = async (task) => {
  try {
    const response = await request.post(`${taskBASE_URL}/taskUpdate`, task);
    return response.data;
  } catch (error) {
    throw error;
  }
};
const API_URL = 'glassStorage/api/shelfRack';
export const shelfRackfindList = (params) => {
  return request.post(`${API_URL}/findList`, params)
    .then(response => response.data)
    .catch(error => {
      throw error;
    });
};
export const shelfRackfindById = (id) => {
  return request.get(`${API_URL}/${id}`)
    .then(response => response.data)
    .catch(error => {
      throw error;
    });
};
export const insertShelfRack = (shelfRack) => {
  return request.post(`${API_URL}`, shelfRack)
    .then(response => response.data)
    .catch(error => {
      throw error;
    });
};
export const updateShelfRack = (shelfRack) => {
  return request.put(`${API_URL}`, shelfRack)
    .then(response => response.data)
    .catch(error => {
      throw error;
    });
};
export const shelfRackdeleteById = (id) => {
  return request.delete(`${API_URL}/${id}`)
    .then(response => response.data)
    .catch(error => {
      throw error;
    });
};
export const findShelfRack = () => {
  return request.get(`${API_URL}/findshelfrack`)
    .then(response => response.data)
    .catch(error => {
      throw error;
    });
};