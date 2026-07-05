package com.trendshop.service;

import com.trendshop.common.BusinessException;
import com.trendshop.dto.CartRequest;
import com.trendshop.entity.CartItem;
import com.trendshop.entity.Product;
import com.trendshop.mapper.CartItemMapper;
import com.trendshop.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;

    public CartService(CartItemMapper cartItemMapper, ProductMapper productMapper) {
        this.cartItemMapper = cartItemMapper;
        this.productMapper = productMapper;
    }

    public Map<String, Object> getCart(Long userId) {
        List<CartItem> items = cartItemMapper.selectCartDetail(userId);
        List<Map<String, Object>> itemMaps = items.stream().map(i -> {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", i.getProductId());
            map.put("name", i.getProductName());
            map.put("image", i.getProductImage());
            map.put("price", i.getProductPrice());
            map.put("quantity", i.getQuantity());
            map.put("subtotal", i.getProductPrice() != null ? i.getProductPrice().multiply(BigDecimal.valueOf(i.getQuantity())) : BigDecimal.ZERO);
            return map;
        }).collect(Collectors.toList());

        BigDecimal total = itemMaps.stream()
                .map(m -> (BigDecimal) m.get("subtotal"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new HashMap<>();
        result.put("items", itemMaps);
        result.put("total", total);
        result.put("count", items.size());
        return result;
    }

    @Transactional
    public Map<String, Object> addToCart(Long userId, CartRequest request) {
        Product product = productMapper.selectById(request.getProductId());
        if (product == null) {
            throw new BusinessException(400, "商品不存在");
        }

        int qty = request.getQuantity() != null ? request.getQuantity() : 1;

        CartItem existing = cartItemMapper.selectByUserAndProduct(userId, request.getProductId());
        if (existing != null) {
            cartItemMapper.increaseQuantity(userId, request.getProductId(), qty);
        } else {
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setProductId(request.getProductId());
            item.setQuantity(qty);
            cartItemMapper.insert(item);
        }

        return getCart(userId);
    }

    @Transactional
    public Map<String, Object> updateCartItem(Long userId, Long productId, Integer quantity) {
        CartItem existing = cartItemMapper.selectByUserAndProduct(userId, productId);
        if (existing == null) {
            throw new BusinessException(400, "购物车中无此商品");
        }
        if (quantity <= 0) {
            cartItemMapper.deleteByUserAndProduct(userId, productId);
        } else {
            existing.setQuantity(quantity);
            cartItemMapper.updateById(existing);
        }
        return getCart(userId);
    }

    @Transactional
    public Map<String, Object> removeCartItem(Long userId, Long productId) {
        cartItemMapper.deleteByUserAndProduct(userId, productId);
        return getCart(userId);
    }

    @Transactional
    public Map<String, Object> clearCart(Long userId) {
        cartItemMapper.deleteByUser(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("items", List.of());
        result.put("total", 0);
        result.put("count", 0);
        return result;
    }
}
