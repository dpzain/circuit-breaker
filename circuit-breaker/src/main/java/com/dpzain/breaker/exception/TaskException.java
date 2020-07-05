package com.dpzain.breaker.exception;

/**
 *
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public class TaskException extends Exception {
  private final Throwable taskExp;

  public TaskException(String message, Throwable taskExp) {
    super(message, taskExp);
    this.taskExp = taskExp;
  }

  public Throwable getTaskException() {
    return taskExp;
  }
}
