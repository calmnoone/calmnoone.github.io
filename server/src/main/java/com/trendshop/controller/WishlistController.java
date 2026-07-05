package com.trendshop.controller;

import com.trendshop.common.BusinessException;
import com.trendshop.common.Result;
import com.trendshop.entity.Product;
import com.trendshop.security.JwtUserDetails;
import com.trendshop.service.WishlistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "收藏")
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public Result<List<Product>> getWishlist(@AuthenticationPrincipal JwtUserDetails user) {
        return Result.ok(wishlistService.getWishlist(user.getId()));
    }

    @PostMapping
    public Result<List<Product>> addToWishlist(@AuthenticationPrincipal JwtUserDetails user,
                                                @RequestBody Map<String, Long> body) {
        Long productId = body.get("productId");
        if (productId == null) {
            throw new BusinessException(400, "商品ID不能为空");
        }
        List<Product> data = wishlistService.addToWishlist(user.getId(), productId);
        return Result.ok("已收藏", data);
    }

    @DeleteMapping("/{productId}")
    public Result<List<Product>> removeFromWishlist(@AuthenticationPrincipal JwtUserDetails user,
                                                     @PathVariable Long productId) {
        List<Product> data = wishlistService.removeFromWishlist(user.getId(), productId);
        return Result.ok("已取消收藏", data);
    }
}
