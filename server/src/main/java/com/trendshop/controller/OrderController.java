package com.trendshop.controller;

import com.trendshop.common.Result;
import com.trendshop.common.PageResult;
import com.trendshop.dto.OrderRequest;
import com.trendshop.entity.Order;
import com.trendshop.security.JwtUserDetails;
import com.trendshop.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "订单")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Order> createOrder(@AuthenticationPrincipal JwtUserDetails user,
                                      @Valid @RequestBody OrderRequest request) {
        Order order = orderService.createOrder(user.getId(), request);
        return Result.created("下单成功", order);
    }

    @GetMapping
    public Result<PageResult<Order>> listOrders(@AuthenticationPrincipal JwtUserDetails user,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        page = Math.max(1, page);
        pageSize = Math.min(50, Math.max(1, pageSize));
        return Result.ok(orderService.listOrders(user.getId(), page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Order> getOrder(@AuthenticationPrincipal JwtUserDetails user,
                                   @PathVariable Long id) {
        return Result.ok(orderService.getOrder(user.getId(), id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/count")
    public Result<Map<String, Object>> getOrderCount() {
        return Result.ok(Map.of("count", orderService.getTotalCount()));
    }
}
