package com.trendshop.service;

import com.trendshop.common.BusinessException;
import com.trendshop.entity.Product;
import com.trendshop.mapper.ProductMapper;
import com.trendshop.mapper.WishlistMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistMapper wishlistMapper;
    private final ProductMapper productMapper;

    public WishlistService(WishlistMapper wishlistMapper, ProductMapper productMapper) {
        this.wishlistMapper = wishlistMapper;
        this.productMapper = productMapper;
    }

    public List<Product> getWishlist(Long userId) {
        return wishlistMapper.selectWishlistProducts(userId);
    }

    public List<Product> addToWishlist(Long userId, Long productId) {
        if (productMapper.selectById(productId) == null) {
            throw new BusinessException(400, "商品不存在");
        }
        wishlistMapper.insertIgnore(userId, productId);
        return getWishlist(userId);
    }

    public List<Product> removeFromWishlist(Long userId, Long productId) {
        wishlistMapper.deleteByUserAndProduct(userId, productId);
        return getWishlist(userId);
    }
}
