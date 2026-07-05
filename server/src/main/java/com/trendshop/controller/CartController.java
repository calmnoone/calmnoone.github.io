package com.trendshop.controller;

import com.trendshop.common.BusinessException;
import com.trendshop.common.Result;
import com.trendshop.dto.CartRequest;
import com.trendshop.security.JwtUserDetails;
import com.trendshop.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "购物车")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Result<Map<String, Object>> getCart(@AuthenticationPrincipal JwtUserDetails user) {
        return Result.ok(cartService.getCart(user.getId()));
    }

    @PostMapping
    public Result<Map<String, Object>> addToCart(@AuthenticationPrincipal JwtUserDetails user,
                                                  @Valid @RequestBody CartRequest request) {
        Map<String, Object> data = cartService.addToCart(user.getId(), request);
        return Result.ok("已添加到购物车", data);
    }

    @PutMapping("/{productId}")
    public Result<Map<String, Object>> updateCartItem(@AuthenticationPrincipal JwtUserDetails user,
                                                       @PathVariable Long productId,
                                                       @RequestBody Map<String, Integer> body) {
        Integer quantity = body.get("quantity");
        if (quantity == null) {
            throw new BusinessException(400, "数量不能为空");
        }
        Map<String, Object> data = cartService.updateCartItem(user.getId(), productId, quantity);
        return Result.ok("已更新", data);
    }

    @DeleteMapping("/{productId}")
    public Result<Map<String, Object>> removeCartItem(@AuthenticationPrincipal JwtUserDetails user,
                                                       @PathVariable Long productId) {
        Map<String, Object> data = cartService.removeCartItem(user.getId(), productId);
        return Result.ok("已移除", data);
    }

    @DeleteMapping
    public Result<Map<String, Object>> clearCart(@AuthenticationPrincipal JwtUserDetails user) {
        Map<String, Object> data = cartService.clearCart(user.getId());
        return Result.ok("购物车已清空", data);
    }
}
