package com.trendshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("wishlists")
public class Wishlist {
    private Long userId;
    private Long productId;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}
