package com.mes.base.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/5/28 15:36
 * @Description:
 */
@Data
public class BaseRequest {

    private String key;

    private Page page;
}
