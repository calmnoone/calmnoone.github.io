package com.trendshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trendshop.entity.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {

    @Select("SELECT ci.*, p.name AS product_name, p.image AS product_image, p.price AS product_price " +
            "FROM cart_items ci JOIN products p ON p.id = ci.product_id " +
            "WHERE ci.user_id = #{userId} ORDER BY ci.added_at DESC")
    @Results({
            @Result(property = "productName", column = "product_name"),
            @Result(property = "productImage", column = "product_image"),
            @Result(property = "productPrice", column = "product_price"),
    })
    List<CartItem> selectCartDetail(@Param("userId") Long userId);

    @Select("SELECT * FROM cart_items WHERE user_id = #{userId} AND product_id = #{productId}")
    CartItem selectByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    @Update("UPDATE cart_items SET quantity = quantity + #{quantity} WHERE user_id = #{userId} AND product_id = #{productId}")
    int increaseQuantity(@Param("userId") Long userId, @Param("productId") Long productId,
                          @Param("quantity") Integer quantity);

    @Delete("DELETE FROM cart_items WHERE user_id = #{userId} AND product_id = #{productId}")
    int deleteByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    @Delete("DELETE FROM cart_items WHERE user_id = #{userId}")
    int deleteByUser(@Param("userId") Long userId);
}
