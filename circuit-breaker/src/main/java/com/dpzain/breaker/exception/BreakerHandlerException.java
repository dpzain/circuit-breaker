package com.dpzain.breaker.exception;


/**
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public class BreakerHandlerException extends RuntimeException {

    public BreakerHandlerException(String message) {
        super(message);
    }

    public BreakerHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
