package com.dpzain.breaker.handler.impl;

import com.dpzain.breaker.exception.BreakerHandlerException;
import com.dpzain.breaker.exception.CircuitBreakerException;
import com.dpzain.breaker.exception.TaskException;
import com.dpzain.breaker.handler.BreakerHandler;
import com.dpzain.breaker.strategy.BreakerStrategy;
import com.dpzain.breaker.strategy.ContextCircuitBreaker;
import com.dpzain.breaker.task.Task;
import com.dpzain.breaker.task.context.TaskContext;

/**
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public class DefaultHandler<R> implements BreakerHandler<R> {

//    private static final Logger _log = LoggerFactory.getLogger(DefaultHandler.class);


    @Override
    public R onBreaker(ContextCircuitBreaker<R> circuitBreaker, Task<R> task, BreakerStrategy<R> breakStrategy, TaskContext<R> context) throws TaskException, CircuitBreakerException, BreakerHandlerException {
        //TODO callBack 记录日志 加入队列  重试等
        System.out.println("熔断了~~~");
        return null;
    }
}
