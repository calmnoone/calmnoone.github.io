package com.trendshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trendshop.entity.Product;
import com.trendshop.entity.Wishlist;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WishlistMapper extends BaseMapper<Wishlist> {

    @Select("SELECT p.* FROM wishlists w JOIN products p ON p.id = w.product_id WHERE w.user_id = #{userId}")
    List<Product> selectWishlistProducts(@Param("userId") Long userId);

    @Insert("INSERT IGNORE INTO wishlists (user_id, product_id) VALUES (#{userId}, #{productId})")
    int insertIgnore(@Param("userId") Long userId, @Param("productId") Long productId);

    @Delete("DELETE FROM wishlists WHERE user_id = #{userId} AND product_id = #{productId}")
    int deleteByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
