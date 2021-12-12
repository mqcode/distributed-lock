package com.github.io.redis;

import com.github.io.dto.ProductStock;

public interface IRedisDistributedService {
    /**
     * 初始化库存
     *
     * @param request 请求
     */
    void initStock(ProductStock request);

    /**
     * 减库存
     *
     * @param request 请求
     */
    String reduceStock(ProductStock request);
}
