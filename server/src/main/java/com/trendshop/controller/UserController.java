package com.trendshop.controller;

import com.trendshop.common.Result;
import com.trendshop.common.PageResult;
import com.trendshop.dto.UpdateProfileRequest;
import com.trendshop.dto.UpdateUserRequest;
import com.trendshop.entity.User;
import com.trendshop.security.JwtUserDetails;
import com.trendshop.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "用户")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ── Profile (self-service) ──

    @GetMapping("/me")
    public Result<User> getProfile(@AuthenticationPrincipal JwtUserDetails user) {
        return Result.ok(userService.getCurrentUser(user.getId()));
    }

    @PutMapping("/me")
    public Result<Void> updateProfile(@AuthenticationPrincipal JwtUserDetails user,
                                       @RequestBody UpdateProfileRequest request) {
        userService.updateProfile(user.getId(), request);
        return Result.ok("更新成功", null);
    }

    // ── Admin user management ──

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        page = Math.max(1, page);
        pageSize = Math.min(100, Math.max(1, pageSize));
        return Result.ok(userService.listUsers(page, pageSize, keyword, role, status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> getUser(@PathVariable Long id) {
        return Result.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        userService.updateUser(id, request);
        return Result.ok("更新成功", null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteUser(@AuthenticationPrincipal JwtUserDetails currentUser,
                                    @PathVariable Long id) {
        if (currentUser.getId().equals(id)) {
            return Result.error(400, "不能删除自己的账号");
        }
        userService.deleteUser(id);
        return Result.ok("删除成功", null);
    }
}
