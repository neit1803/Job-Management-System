package com.tienhuynh.user_service.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CommonResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> CommonResponse<T> ok(T data) {
        CommonResponse<T> res = new CommonResponse<>();
        res.success = true;
        res.data = data;
        return res;
    }

    public static <T> CommonResponse<T> error(String msg) {
        CommonResponse<T> res = new CommonResponse<>();
        res.success = false;
        res.message = msg;
        return res;
    }
}

