import request from '@/utils/request';
import { ElMessage } from 'element-plus';
import { de } from 'element-plus/es/locale/index.mjs';

// 格式化时间戳为年月日时间字符串的函数
function formatTimestamp(timestamp: string | number | Date): string {
    const date = new Date(timestamp)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    const seconds = String(date.getSeconds()).padStart(2, '0')
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}
// ==================== 仓储原片历史任务接口 ====================
// 历史任务查询方法
export const fetchHistoricalTasks = async (params: HistoryTaskParams) => {
    try {
        const response = await request.post("/glassStorage/rawGlassStorageHistoryTask/queryRawGlassHistoryTask", {
            pageNo: params.pageNo,
            pageSize: params.pageSize,
            taskState: params.taskState || [],
            taskType: params.taskType || [],
            beginDate: params.beginDate,
            endDate: params.endDate,
        });
        if (response?.code === 200 && response?.data) {
            ElMessage.success(response.message);
            const records = Array.isArray(response.data.records) ? response.data.records : [];
            // 格式化数据，添加非空判断
            const formattedData = records.map(record => ({
                ...record,
                formattedCreateTime: record?.createTime ? formatTimestamp(record.createTime) : '-',
                formattedUpdateTime: record?.updateTime ? formatTimestamp(record.updateTime) : '-',
            }));
            return {
                data: formattedData,
                total: response.data.total || 0
            };
        } else {
            const errorMessage = response?.message || '获取历史任务失败';
            ElMessage.error(errorMessage);
            throw new Error(errorMessage);
        }
    } catch (error) {
        console.error('Error fetching historical tasks:', error);
        ElMessage.error('获取历史任务失败');
        throw error;
    }
};
// 默认查询参数
export const getDefaultHistoryParams = (): HistoryTaskParams => {
    const getglobalDate = window.localStorage.getItem('getglobalDate');
    const defaultEndDate = getglobalDate ? formatTimestamp(new Date(getglobalDate)) : formatTimestamp(new Date());

    return {
        pageNo: 1,
        pageSize: 20,
        taskState: [],
        taskType: [],
        beginDate: window.localStorage.getItem('startTime') || '',
        endDate: defaultEndDate
    };
};

export interface HistoryTaskParams {
    pageNo: number
    pageSize: number
    taskState?: string[]
    taskType?: string[]
    beginDate?: string
    endDate?: string
    startSlot?: string
    targetSlot?: string
}

export interface HistoryTaskRecord {
    id: string
    createTime: string
    updateTime: string
    formattedCreateTime?: string
    formattedUpdateTime?: string
}

// ==================== 仓储原片详情接口 ====================
// 类型定义
export interface deviceIdParams {
    deviceIdList: number[]
}

export interface LeftingStationRawGlassDetailsParams {
    leftingStation: string
}

export interface InboundRequestsParams {
    leftingStation: string
}

export interface OutboundRequestParams {
    leftingStation: string;
    slot: string;
    rawWidth: string;
    rawHeight: string;
    rawThickness: string;
}

export interface BackWarehousingParams {
    leftingStation: string;
}

export interface RawGlassFormData {
    id: string;
    shelf: string;
    deviceId: string;
    slot: string;
    rawWidth: string;
    rawHeight: string;
    rawThickness: string;
    filmsId: string;
    rawMargin: string;
    remainQuantity: string;
    manufacturer: string;
    rawNumber: string;
}

export interface TaskRecord {
    id: string;
    createTime: string;
    updateTime: string;
    [key: string]: any;
}

// 查询所有消息接口
export const queryAllMessage = async () => {
    try {
        const response = await request.get("/glassStorage/rawJob/queryAllMessage");
        if (response.code === 200) {
            // 格式化任务列表时间
            const formattedTasks = response.data.historyTaskList.map((task: TaskRecord) => {
                const formattedTask = { ...task };
                if (formattedTask.createTime) {
                    formattedTask.createTime = formatTimestamp(formattedTask.createTime);
                }
                if (formattedTask.updateTime) {
                    formattedTask.updateTime = formatTimestamp(formattedTask.updateTime);
                }
                return formattedTask;
            });
            return {
                historyTaskList: formattedTasks,
                rawStorageSwitch: response.data.rawStorageSwitch
            };
        } else {
            ElMessage.warning(response.msg);
            throw new Error(response.msg);
        }
    } catch (error) {
        console.error('Error fetching all messages:', error);
        throw error;
    }
};

// 查询架子上原片详情列表
export const queryRawGlassList = async (params: deviceIdParams) => {
    try {
        const response = await request.post('/glassStorage/rawGlassStorageDetails/queryRawGlassList', {
            deviceIdList: params.deviceIdList
        });
        if (response.code === 200) {
            return response.data;
        } else {
            ElMessage.warning(response.data);
            throw new Error(response.data);
        }
    } catch (error) {
        console.error('Error fetching raw glass details:', error);
        throw error;
    }
};

// 查询所有架子状态
export const queryLeftingStation = async () => {
    try {
        const response = await request.post('/glassStorage/rawGlassStorageStation/queryLeftingStation', {});
        if (response.code === 200) {
            return response.data;
        } else {
            return [];
        }
    } catch (error) {
        ElMessage.error('获取架子状态失败');
        return [];
    }
};

// 查询吊装位架子
export const LeftingStationRawGlassDetails = async (params: LeftingStationRawGlassDetailsParams) => {
    try {
        const response = await request.post('/glassStorage/rawGlassStorageDetails/queryLeftingStationRawGlassDetails', {
            leftingStation: params.leftingStation,
        });
        if (response.code === 200) {
            params.leftingStation = '';
            return response.data;
        } else {
            return [];
        }
    } catch (error) {
        ElMessage.error('获取架子状态失败');
        return [];
    }
};

// 空架子到吊装位接口
export const InboundRequests = async (params: InboundRequestsParams) => {
    try {
        const response = await request.post("/glassStorage/rawGlassStorageDetails/warehousingRequest", {
            leftingStation: params.leftingStation,
        })
        if (response.code == 200) {
            ElMessage.success(response.message);
            params.leftingStation = '';
        } else {
            ElMessage.error(response.message);
        }
    }
    catch (error) {
        console.error(error);
    }
}

// 出库请求 - 架子到吊装位
export const outWarehousingRequest = async (params: OutboundRequestParams) => {
    try {
        const response = await request.post("/glassStorage/rawGlassStorageDetails/outWarehousingRequest", {
            leftingStation: params.leftingStation,
            slot: params.slot,
            rawWidth: params.rawWidth,
            rawHeight: params.rawHeight,
            rawThickness: params.rawThickness,
        });
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response;
        } else {
            ElMessage.error(response.message);
            throw new Error(response.message);
        }
    } catch (error) {
        console.error('Error in outbound request:', error);
        throw error;
    }
};

// 回库请求 - 吊装位到架子
export const backWarehousing = async (params: BackWarehousingParams) => {
    try {
        const response = await request.post("/glassStorage/rawGlassStorageDetails/backWarehousing", {
            leftingStation: params.leftingStation,
        });
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response;
        } else {
            ElMessage.error(response.message);
            throw new Error(response.message);
        }
    } catch (error) {
        console.error('Error in back warehousing:', error);
        throw error;
    }
};

// 原片解绑
export const rawGlassUnBind = async (id: string) => {
    try {
        const response = await request.post('/glassStorage/rawGlassStorageDetails/rawGlassUnBind', {
            id: id
        });
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response;
        } else {
            ElMessage.error(response.msg);
            throw new Error(response.msg);
        }
    } catch (error) {
        console.error('Error unbinding raw glass:', error);
        throw error;
    }
};

// 删除原片详情
export const removeRawGlassDetails = async (id: string) => {
    try {
        const response = await request.post('/glassStorage/rawGlassStorageDetails/removeRawGlassDetails', {
            id: id
        });
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response;
        } else {
            ElMessage.error(response.msg);
            throw new Error(response.msg);
        }
    } catch (error) {
        console.error('Error removing raw glass details:', error);
        throw error;
    }
};

// 更新架子状态
export const updateStorageCageState = async (params: { enableState: number; shelf: string }) => {
    try {
        const response = await request.post('/glassStorage/rawGlassStorageStation/updateStorageCageState', {
            enableState: params.enableState,
            shelf: params.shelf  
        });
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response;
        } else {
            ElMessage.error(response.message);
            throw new Error(response.message);
        }
    } catch (error) {
        ElMessage.error('更新架子状态失败');
        throw error;
    }
};

// 查询未绑定原片详情列表
export const queryUnBindRawGlassList = async () => {
    try {
        const response = await request.post("/glassStorage/rawGlassStorageDetails/queryUnBindRawGlassList", {});
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response.data;
        } else {
            ElMessage.error(response.message);
            throw new Error(response.message);
        }
    } catch (error) {
        console.error('Error querying unbound raw glass list:', error);
        throw error;
    }
};

// 添加原片详情
export const insertRawGlassDetails = async (formData: RawGlassFormData) => {
    try {
        const response = await request.post('/glassStorage/rawGlassStorageDetails/insertRawGlassDetails', {
            id: formData.id,
            deviceId: formData.deviceId,
            shelf: formData.shelf,
            slot: formData.slot,
            rawWidth: formData.rawWidth,
            rawHeight: formData.rawHeight,
            rawThickness: formData.rawThickness,
            filmsId: formData.filmsId,
            rawMargin: formData.rawMargin,
            remainQuantity: formData.remainQuantity,
            manufacturer: formData.manufacturer,
            rawNumber: formData.rawNumber,
        });
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response;
        } else {
            ElMessage.error(response.message);
            throw new Error(response.message);
        }
    } catch (error) {
        console.error('Error inserting raw glass details:', error);
        throw error;
    }
};

// 原片入库 
export const glassWarehousing = async (formData: RawGlassFormData) => {
    try {
        const response = await request.post('/glassStorage/rawGlassStorageDetails/glassWarehousing', {
            id: formData.id,
            deviceId: formData.deviceId,
            shelf: formData.shelf,
            slot: formData.slot,
            rawWidth: formData.rawWidth,
            rawHeight: formData.rawHeight,
            rawThickness: formData.rawThickness,
            filmsId: formData.filmsId,
            rawMargin: formData.rawMargin,
            remainQuantity: formData.remainQuantity,
            manufacturer: formData.manufacturer,
            rawNumber: formData.rawNumber,
        });
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response;
        } else {
            ElMessage.error(response.message);
            throw new Error(response.message);
        }
    } catch (error) {
        console.error('Error in glass warehousing:', error);
        throw error;
    }
};

// 更新原片详情
export const updateRawGlassDetails = async (params: {
    id: string;
    remainQuantity: string;
    rawMargin: string;
    rawWidth: string;
    rawHeight: string;
    rawThickness: string;
    filmsId: string;
    deviceId: string;
    shelf: string;
    slot: string;
    manufacturer: string;
    rawNumber: string;
}) => {
    try {
        const response = await request.post('/glassStorage/rawGlassStorageDetails/updateRawGlassDetails', params);
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response;
        } else {
            ElMessage.error(response.message);
            throw new Error(response.message);
        }
    } catch (error) {
        console.error('Error updating raw glass details:', error);
        throw error;
    }
};

// // 任务成功处理
// export const taskSuccess = async (taskId: string) => {
//     try {
//         const url = `/glassStorage/rawGlassStorageTask/taskSuccess?taskId=${taskId}`;
//         const response = await request.post(url);
//         if (response.code === 200) {
//             ElMessage.success(response.message);
//             return response;
//         } else {
//             ElMessage.error(response.msg);
//             throw new Error(response.msg);
//         }
//     } catch (error) {
//         console.error('Error in task success:', error);
//         throw error;
//     }
// };

// // 任务失败处理
// export const taskError = async (taskId: string) => {
//     try {
//         const url = `/glassStorage/rawGlassStorageTask/taskError?taskId=${taskId}`;
//         const response = await request.post(url);
//         if (response.code === 200) {
//             ElMessage.success(response.message);
//             return response;
//         } else {
//             ElMessage.error(response.msg);
//             throw new Error(response.msg);
//         }
//     } catch (error) {
//         console.error('Error in task error:', error);
//         throw error;
//     }
// };

// 任务开关
export const rawStorageSwitch = async (switchValue: any) => {
    try {
        const response = await request.post('/glassStorage/rawJob/rawStorageSwitch', switchValue);
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response.data;
        } else {
            ElMessage.error(response.message);
            throw new Error(response.message);
        }
    } catch (error) {
        console.error('Error in raw storage switch:', error);
        throw error;
    }
};

// 重新下发任务
export const reissueTask = async (taskId: string) => {
    try {
        const response = await request.post(`/glassStorage/rawGlassStorageHistoryTask/updateRawGlassHistoryTask`, { id: taskId, taskState: 0 });
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response;
        } else {
            ElMessage.error(response.message);
            throw new Error(response.message);
        }
    } catch (error) {
        console.error('Error reissuing task:', error);
        throw error;
    }
};
// 删除
export const deleteTask = async (taskId: string) => {
    try {
        const response = await request.post(`/glassStorage/rawGlassStorageHistoryTask/removeRawGlassHistoryTask`, { id: taskId, taskState: 3 });
        if (response.code === 200) {
            ElMessage.success(response.message);
            return response;
        } else {
            ElMessage.error(response.message);
            throw new Error(response.message);
        }
    } catch (error) {
        console.error('Error reissuing task:', error);
        throw error;
    }
};