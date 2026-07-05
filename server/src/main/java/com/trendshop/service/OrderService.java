package com.trendshop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trendshop.common.BusinessException;
import com.trendshop.common.PageResult;
import com.trendshop.dto.OrderRequest;
import com.trendshop.entity.Order;
import com.trendshop.entity.OrderItem;
import com.trendshop.entity.Product;
import com.trendshop.entity.User;
import com.trendshop.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final CartItemMapper cartItemMapper;

    public OrderService(OrderMapper orderMapper, OrderItemMapper orderItemMapper,
                        ProductMapper productMapper, UserMapper userMapper,
                        CartItemMapper cartItemMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.productMapper = productMapper;
        this.userMapper = userMapper;
        this.cartItemMapper = cartItemMapper;
    }

    @Transactional
    public Order createOrder(Long userId, OrderRequest request) {
        User user = userMapper.selectUserWithProfile(userId);
        if (user == null) throw new BusinessException(400, "用户不存在");

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderRequest.OrderItemDto dto : request.getItems()) {
            Product product = productMapper.selectById(dto.getProductId());
            if (product == null) {
                throw new BusinessException(400, "商品 ID " + dto.getProductId() + " 不存在");
            }
            if (product.getStock() < dto.getQuantity()) {
                throw new BusinessException(400, product.getName() + " 库存不足");
            }

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()));
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(dto.getQuantity());
            item.setSubtotal(subtotal);
            orderItems.add(item);
            total = total.add(subtotal);

            // decrement stock
            product.setStock(product.getStock() - dto.getQuantity());
            productMapper.updateById(product);
        }

        // create order
        Order order = new Order();
        order.setId(System.currentTimeMillis());
        order.setUserId(userId);
        order.setTotal(total);
        order.setStatus("pending");
        order.setAddress(request.getAddress() != null ? request.getAddress() :
                (user.getProfile() != null ? user.getProfile().getAddress() : ""));
        order.setNote(request.getNote() != null ? request.getNote() : "");
        orderMapper.insert(order);

        // insert order items
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }

        // clear cart
        cartItemMapper.deleteByUser(userId);

        // load full order
        return getOrder(userId, order.getId());
    }

    public PageResult<Order> listOrders(Long userId, int page, int pageSize) {
        Page<Order> p = new Page<>(page, pageSize);
        IPage<Order> result = orderMapper.selectOrderPage(p, userId);

        for (Order o : result.getRecords()) {
            o.setItems(orderItemMapper.selectByOrderId(o.getId()));
        }

        return PageResult.of(result.getRecords(), page, pageSize, result.getTotal());
    }

    public Order getOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException(404, "订单不存在");
        }
        order.setItems(orderItemMapper.selectByOrderId(orderId));
        return order;
    }

    public long getTotalCount() {
        return orderMapper.selectCount(null);
    }
}
