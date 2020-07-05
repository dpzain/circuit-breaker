package com.dpzain.breaker;

import com.dpzain.breaker.exception.TaskException;
import com.dpzain.breaker.handler.BreakerHandler;
import com.dpzain.breaker.handler.impl.DefaultHandler;
import com.dpzain.breaker.strategy.BreakerStrategy;
import com.dpzain.breaker.strategy.impl.DefaultBreakerStrategy;
import com.dpzain.breaker.task.MethodTask;

/**
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public class CircuitBreakerRunner<R, T> {

    private T service;
    private CircuitBreaker<R> circuitBreaker;
    private BreakerHandler<R> breakHandler;
    private BreakerStrategy<R> breakStrategy;
    private String methodName;

    public  CircuitBreakerRunner (T service,String methodName) {
        this(service,methodName,new DefaultCircuitBreaker<R>(), new DefaultBreakerStrategy<R>(10, 50, 5000), new DefaultHandler<R>());
    }

    public   CircuitBreakerRunner(T service, String methodName,CircuitBreaker<R> circuitBreaker, BreakerStrategy<R> breakStrategy, BreakerHandler<R> breakHandler) {
        this.methodName = methodName;
        this.service=service;
        this.circuitBreaker = circuitBreaker;
        this.breakHandler = breakHandler;
        this.breakStrategy = breakStrategy;
    }

    @SuppressWarnings("all")
    public R runService(Object ... param ) throws NoSuchMethodException {
        try {
            return (R) circuitBreaker.execute(new MethodTask(service, methodName, String.class, param), breakStrategy,
                    breakHandler);
        } catch (TaskException e) {
            //反射执行 都抛出 InvocationTargetException 异常
            System.out.println("Calling mock resulted in exception: " + e.getMessage());
            throw new RuntimeException(e.getTaskException().getMessage(), e.getTaskException());
        } catch (NoSuchMethodException e) {
            throw e;
        }
    }
}
