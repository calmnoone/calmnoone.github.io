package com.trendshop.controller;

import com.trendshop.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Result<Map<String, String>> health() {
        return Result.ok(Map.of(
                "message", "ok",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
