package com.dpzain.breaker.handler;

import com.dpzain.breaker.exception.BreakerHandlerException;
import com.dpzain.breaker.exception.CircuitBreakerException;
import com.dpzain.breaker.exception.TaskException;
import com.dpzain.breaker.strategy.BreakerStrategy;
import com.dpzain.breaker.strategy.ContextCircuitBreaker;
import com.dpzain.breaker.task.Task;
import com.dpzain.breaker.task.context.TaskContext;

/**
 * 断路处理器
 * @param <R>
 */
public interface BreakerHandler<R> {

    R onBreaker(ContextCircuitBreaker<R> circuitBreaker, Task<R> task, BreakerStrategy<R> breakStrategy,
                TaskContext<R> context) throws TaskException, CircuitBreakerException,
            BreakerHandlerException;
}
