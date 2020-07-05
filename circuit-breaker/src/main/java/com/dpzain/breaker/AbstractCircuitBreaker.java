package com.dpzain.breaker;

import com.dpzain.breaker.exception.BreakerHandlerException;
import com.dpzain.breaker.exception.CircuitBreakerException;
import com.dpzain.breaker.exception.TaskException;
import com.dpzain.breaker.handler.BreakerHandler;
import com.dpzain.breaker.strategy.BreakerStrategy;
import com.dpzain.breaker.strategy.ContextCircuitBreaker;
import com.dpzain.breaker.task.Task;
import com.dpzain.breaker.task.context.TaskContext;
import com.dpzain.breaker.task.TaskHolder;
import com.dpzain.breaker.util.SystemClock;

import java.util.concurrent.*;

/**
 * 封装通用的断路逻辑  (if isBreaker ->callBack   else  executing )    ---> record metrics !
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public abstract class AbstractCircuitBreaker<R> implements CircuitBreaker<R>, ContextCircuitBreaker<R> {


    /**
     * 该断路器实例中的 所有执行中的task
     */
    private ConcurrentHashMap<String, TaskHolder<R>> taskHodlers;


    public AbstractCircuitBreaker() {
        this.taskHodlers = new ConcurrentHashMap<>();
    }


    @Override
    public R execute(Task<R> task, BreakerStrategy<R> breakStrategy, BreakerHandler<R> breakHandler) throws TaskException, BreakerHandlerException, CircuitBreakerException {
//        DefaultTaskContext<R> defaultTaskContext = new DefaultTaskContext<>(taskHodlers, bufferTask);
//        return executeContext(task, breakStrategy, breakHandler, defaultTaskContext);
        return executeContext(task, breakStrategy, breakHandler, null);
    }

    @Override
    public R executeContext(Task<R> task, BreakerStrategy<R> breakStrategy, BreakerHandler<R> breakHandler, TaskContext<R> context) throws TaskException, BreakerHandlerException, CircuitBreakerException {
        final TaskHolder<R> taskHolder = new TaskHolder<>(task, now());
        if (!breakStrategy.isBreaker(taskHolder, context)) {
            R result;
            String taskKey = "";
            try {
                boolean isFailed = false;
                taskKey = Thread.currentThread().getId() + "_" + task.hashCode();
                //同步追踪task状态  可能面临 并发下的map 的 resize 阻塞用户线程
                taskHodlers.put(taskKey, taskHolder);
                try {
                    result = task.execute();
                } catch (Exception e) {
                    //暂只考虑异常失败  多个breakStrategy合并 可以加入task 超时控制
                    isFailed = true;
                    throw new TaskException("Task failed by exception: " + e.getMessage(), e);
                } finally {
                    breakStrategy.submitMetric(taskHolder, isFailed ? TaskHolder.Stats.FAILED : TaskHolder.Stats.SUCCESS);
                }
            } catch (TaskException e) {
                throw e;
            } catch (Exception ex) {
                throw new CircuitBreakerException("task executed exception: " + ex.getMessage(), ex);
            } finally {
                taskHodlers.remove(taskKey);
            }
            return result;
        } else {
            return breakHandler.onBreaker(this, task, breakStrategy, context);
        }
    }

    private static long now() {
        return SystemClock.now();
    }
}
