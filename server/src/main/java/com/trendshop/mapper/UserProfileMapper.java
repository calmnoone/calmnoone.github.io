package com.trendshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trendshop.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
}
