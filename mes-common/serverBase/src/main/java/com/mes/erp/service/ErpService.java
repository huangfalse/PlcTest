package com.mes.erp.service;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/6/14 20:06
 * @Description:
 */
public interface ErpService {
    /**
     * 按条件查询破损数据。
     *
     * @return 膜系集合
     */
    List<String> queryFilmsList();
}
