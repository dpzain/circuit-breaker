package com.dpzain.breaker.strategy;

import com.dpzain.breaker.task.context.TaskContext;
import com.dpzain.breaker.task.TaskHolder;

/**
 * 断路策略 顶层接口
 * @param <R>
 */
public interface BreakerStrategy<R> {

    boolean isBreaker(TaskHolder<R> task, TaskContext<R> context);

    /**
     * 上报状态
     * @param stats
     * @return
     */
    void submitMetric(TaskHolder<R> task,TaskHolder.Stats stats);
}
