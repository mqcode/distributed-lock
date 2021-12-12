package com.github.io.dto;

import lombok.Data;

/**
 * 商品库存表
 */
@Data
public class ProductStock {
    /**
     * 商品编码
     */
    private String sku;
    /**
     * 库存数量
     */
    private Long qty;
}
