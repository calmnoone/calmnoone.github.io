package com.trendshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trendshop.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT u.*, p.nickname, p.bio, p.gender, p.birthday, p.address " +
            "FROM users u LEFT JOIN user_profiles p ON p.user_id = u.id WHERE u.id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "profile.nickname", column = "nickname"),
            @Result(property = "profile.bio", column = "bio"),
            @Result(property = "profile.gender", column = "gender"),
            @Result(property = "profile.birthday", column = "birthday"),
            @Result(property = "profile.address", column = "address"),
    })
    User selectUserWithProfile(@Param("id") Long id);

    @Select("SELECT u.*, p.nickname, p.bio, p.gender, p.birthday, p.address " +
            "FROM users u LEFT JOIN user_profiles p ON p.user_id = u.id WHERE u.username = #{username}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "profile.nickname", column = "nickname"),
            @Result(property = "profile.bio", column = "bio"),
            @Result(property = "profile.gender", column = "gender"),
            @Result(property = "profile.birthday", column = "birthday"),
            @Result(property = "profile.address", column = "address"),
    })
    User selectByUsername(@Param("username") String username);

    @Select("SELECT u.*, p.nickname, p.bio, p.gender, p.birthday, p.address " +
            "FROM users u LEFT JOIN user_profiles p ON p.user_id = u.id WHERE u.email = #{email}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "profile.nickname", column = "nickname"),
            @Result(property = "profile.bio", column = "bio"),
            @Result(property = "profile.gender", column = "gender"),
            @Result(property = "profile.birthday", column = "birthday"),
            @Result(property = "profile.address", column = "address"),
    })
    User selectByEmail(@Param("email") String email);

    @Select("<script>" +
            "SELECT u.*, p.nickname, p.bio, p.gender, p.birthday, p.address " +
            "FROM users u LEFT JOIN user_profiles p ON p.user_id = u.id WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'> AND (u.username LIKE CONCAT('%',#{keyword},'%') OR u.email LIKE CONCAT('%',#{keyword},'%')) </if>" +
            "<if test='role != null and role != \"\"'> AND u.role = #{role} </if>" +
            "<if test='status != null'> AND u.status = #{status} </if>" +
            "ORDER BY u.id DESC" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "profile.nickname", column = "nickname"),
            @Result(property = "profile.bio", column = "bio"),
            @Result(property = "profile.gender", column = "gender"),
            @Result(property = "profile.birthday", column = "birthday"),
            @Result(property = "profile.address", column = "address"),
    })
    IPage<User> selectUserPage(Page<User> page, @Param("keyword") String keyword,
                                @Param("role") String role, @Param("status") Integer status);
}
