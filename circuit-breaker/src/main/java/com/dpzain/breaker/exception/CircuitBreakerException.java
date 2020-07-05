package com.dpzain.breaker.exception;

/**
 *
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public class CircuitBreakerException extends RuntimeException {

  public CircuitBreakerException(String message) {
    super(message);
  }

  public CircuitBreakerException(String message, Throwable t) {
    super(message, t);
  }

}
