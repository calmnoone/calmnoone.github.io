package com.trendshop.controller;

import com.trendshop.common.Result;
import com.trendshop.common.PageResult;
import com.trendshop.entity.Product;
import com.trendshop.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "商品")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/categories")
    public Result<List<String>> getCategories() {
        return Result.ok(productService.getCategories());
    }

    @GetMapping
    public Result<PageResult<Product>> listProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort) {
        page = Math.max(1, page);
        pageSize = Math.min(50, Math.max(1, pageSize));
        return Result.ok(productService.listProducts(page, pageSize, category, keyword, sort));
    }

    @GetMapping("/{id}")
    public Result<Product> getProduct(@PathVariable Long id) {
        return Result.ok(productService.getProduct(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Result<Void> updateProduct(@PathVariable Long id, @RequestBody Product updates) {
        productService.updateProduct(id, updates);
        return Result.ok("更新成功", null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/count")
    public Result<Map<String, Object>> getProductCount() {
        return Result.ok(Map.of("count", productService.getTotalCount()));
    }
}
