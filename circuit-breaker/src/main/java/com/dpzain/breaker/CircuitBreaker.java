package com.dpzain.breaker;

import com.dpzain.breaker.exception.BreakerHandlerException;
import com.dpzain.breaker.exception.CircuitBreakerException;
import com.dpzain.breaker.exception.TaskException;
import com.dpzain.breaker.handler.BreakerHandler;
import com.dpzain.breaker.strategy.BreakerStrategy;
import com.dpzain.breaker.task.Task;

/**
 *
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public interface CircuitBreaker<R> {

    R execute(Task<R> task, BreakerStrategy<R> breakStrategy, BreakerHandler<R> breakHandler) throws TaskException, BreakerHandlerException, CircuitBreakerException;


}