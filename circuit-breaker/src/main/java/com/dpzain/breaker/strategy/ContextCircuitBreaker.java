package com.dpzain.breaker.strategy;

import com.dpzain.breaker.exception.BreakerHandlerException;
import com.dpzain.breaker.exception.CircuitBreakerException;
import com.dpzain.breaker.exception.TaskException;
import com.dpzain.breaker.handler.BreakerHandler;
import com.dpzain.breaker.task.Task;
import com.dpzain.breaker.task.context.TaskContext;

/**
 * taskContext 上下文 断路器
 * @param <R>
 */
public interface ContextCircuitBreaker<R> {

    R executeContext(Task<R> task, BreakerStrategy<R> breakStrategy, BreakerHandler<R> breakHandler, TaskContext<R> context) throws TaskException, BreakerHandlerException, CircuitBreakerException;
}
