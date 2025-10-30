// src/api/breakPiece.ts
import request from '@/utils/request'

export interface TemperingGlassItem {
    // 定义你的数据项接口属性
    glassId: number;
    line: number;
}

export interface TemperingGlassGroup {
    groupName: string;
    items: TemperingGlassItem[];
}

// 破损
export const updateTempQueueState = async (glassId: string | number, line: number) => {
    try {
        const response = await request.post('/cutting/damage/saveDamage', {
            remark: '掰片',
            workingProcedure: '切割',
            glassId,
            line
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