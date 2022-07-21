package com.guxt.take.excption;

/**
 * 运行时异常处理
 */
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
