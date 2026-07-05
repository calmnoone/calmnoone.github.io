package com.trendshop.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trendshop.common.BusinessException;
import com.trendshop.common.PageResult;
import com.trendshop.dto.UpdateProfileRequest;
import com.trendshop.dto.UpdateUserRequest;
import com.trendshop.entity.User;
import com.trendshop.entity.UserProfile;
import com.trendshop.mapper.UserMapper;
import com.trendshop.mapper.UserProfileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    public UserService(UserMapper userMapper, UserProfileMapper userProfileMapper) {
        this.userMapper = userMapper;
        this.userProfileMapper = userProfileMapper;
    }

    public User getCurrentUser(Long userId) {
        User user = userMapper.selectUserWithProfile(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        user.setPassword(null);
        return user;
    }

    @Transactional
    public void updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(404, "用户不存在");

        // update user fields
        boolean userChanged = false;
        if (request.getAvatar() != null) { user.setAvatar(request.getAvatar()); userChanged = true; }
        if (request.getPhone() != null) { user.setPhone(request.getPhone()); userChanged = true; }
        if (userChanged) userMapper.updateById(user);

        // update profile fields
        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, userId));
        if (profile != null) {
            boolean profileChanged = false;
            if (request.getNickname() != null) { profile.setNickname(request.getNickname()); profileChanged = true; }
            if (request.getBio() != null) { profile.setBio(request.getBio()); profileChanged = true; }
            if (request.getGender() != null) { profile.setGender(request.getGender()); profileChanged = true; }
            if (request.getBirthday() != null) { profile.setBirthday(request.getBirthday()); profileChanged = true; }
            if (request.getAddress() != null) { profile.setAddress(request.getAddress()); profileChanged = true; }
            if (profileChanged) userProfileMapper.updateById(profile);
        }
    }

    public PageResult<Map<String, Object>> listUsers(int page, int pageSize, String keyword, String role, String statusStr) {
        Integer status = (statusStr != null && !statusStr.isEmpty()) ? Integer.parseInt(statusStr) : null;
        Page<User> p = new Page<>(page, pageSize);
        IPage<User> result = userMapper.selectUserPage(p, keyword, role, status);

        List<Map<String, Object>> list = result.getRecords().stream().map(u -> {
            Map<String, Object> map = new java.util.LinkedHashMap<>();
            map.put("id", u.getId());
            map.put("username", u.getUsername());
            map.put("email", u.getEmail());
            map.put("avatar", u.getAvatar());
            map.put("phone", u.getPhone());
            map.put("role", u.getRole());
            map.put("status", u.getStatus());
            map.put("createdAt", u.getCreatedAt());
            map.put("updatedAt", u.getUpdatedAt());
            map.put("profile", u.getProfile());
            return map;
        }).collect(Collectors.toList());

        return PageResult.of(list, page, pageSize, result.getTotal());
    }

    public Map<String, Object> getUserById(Long id) {
        User user = userMapper.selectUserWithProfile(id);
        if (user == null) throw new BusinessException(404, "用户不存在");
        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("avatar", user.getAvatar());
        map.put("phone", user.getPhone());
        map.put("role", user.getRole());
        map.put("status", user.getStatus());
        map.put("createdAt", user.getCreatedAt());
        map.put("updatedAt", user.getUpdatedAt());
        map.put("profile", user.getProfile());
        return map;
    }

    @Transactional
    public void updateUser(Long id, UpdateUserRequest request) {
        User user = userMapper.selectById(id);
        if (user == null) throw new BusinessException(404, "用户不存在");

        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getStatus() != null) user.setStatus(request.getStatus());
        userMapper.updateById(user);

        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, id));
        if (profile != null) {
            boolean changed = false;
            if (request.getNickname() != null) { profile.setNickname(request.getNickname()); changed = true; }
            if (request.getBio() != null) { profile.setBio(request.getBio()); changed = true; }
            if (request.getGender() != null) { profile.setGender(request.getGender()); changed = true; }
            if (request.getBirthday() != null) { profile.setBirthday(request.getBirthday()); changed = true; }
            if (request.getAddress() != null) { profile.setAddress(request.getAddress()); changed = true; }
            if (changed) userProfileMapper.updateById(profile);
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        if (userMapper.selectById(id) == null) {
            throw new BusinessException(404, "用户不存在");
        }
        userMapper.deleteById(id);
    }

    // Helper for import
    private Map<String, Object> toUserMap(User u) {
        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("id", u.getId());
        map.put("username", u.getUsername());
        map.put("email", u.getEmail());
        map.put("avatar", u.getAvatar());
        map.put("phone", u.getPhone());
        map.put("role", u.getRole());
        map.put("status", u.getStatus());
        map.put("createdAt", u.getCreatedAt());
        map.put("updatedAt", u.getUpdatedAt());
        map.put("profile", u.getProfile());
        return map;
    }
}
