package com.mockingbird.Springbootcafe.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result {
    public static int SUCCESS_CODE = 0;
    public static int FAIL_CODE = 1;

    int code;
    String msg;
    Object data;
    private Result(int code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success() {
        return new Result(SUCCESS_CODE,null,null);
    }

    public static Result success(Object data) {
        return new Result(SUCCESS_CODE,"",data);
    }

    public static Result fail(String message) {
        return new Result(FAIL_CODE,message,null);
    }


}
