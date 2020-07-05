package com.dpzain.breaker.task;

/**
 * 执行执行受保护代码的Task
 * @param <R>
 */
public interface Task<R> {

  R execute() throws Exception;
}
