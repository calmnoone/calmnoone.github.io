package com.trendshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trendshop.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @Select("<script>" +
            "SELECT * FROM products WHERE 1=1 " +
            "<if test='category != null and category != \"\" and category != \"全部\"'> AND category = #{category} </if>" +
            "<if test='keyword != null and keyword != \"\"'> AND (name LIKE CONCAT('%',#{keyword},'%') OR category LIKE CONCAT('%',#{keyword},'%')) </if>" +
            "<choose>" +
            "  <when test='sort == \"price-asc\"'> ORDER BY price ASC </when>" +
            "  <when test='sort == \"price-desc\"'> ORDER BY price DESC </when>" +
            "  <when test='sort == \"rating\"'> ORDER BY rating DESC </when>" +
            "  <when test='sort == \"sales\"'> ORDER BY reviews DESC </when>" +
            "  <otherwise> ORDER BY id DESC </otherwise>" +
            "</choose>" +
            "</script>")
    IPage<Product> selectProductPage(Page<Product> page, @Param("category") String category,
                                      @Param("keyword") String keyword, @Param("sort") String sort);

    @Select("SELECT DISTINCT category FROM products ORDER BY category")
    List<String> selectCategories();
}
