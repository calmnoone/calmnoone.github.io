package com.trendshop.common;

public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static Result<Void> ok() {
        return new Result<>(200, "操作成功", null);
    }

    public static <T> Result<T> created(String message, T data) {
        return new Result<>(201, message, data);
    }

    public static Result<Void> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static Result<Void> error(String message) {
        return new Result<>(400, message, null);
    }
}
