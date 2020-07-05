package com.dpzain.breaker.task.context;

import com.dpzain.breaker.task.TaskHolder;

import java.util.Collection;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * 任务执行 上下文
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public interface TaskContext<R> {

    /**
     * 在 circuit-breaker 运行中的 task (只读集合)
     * @return
     */
    Collection<TaskHolder<R>> getHolders();

//    /**
//     * 最近缓存的请求集合
//     * @return
//     */
//    BlockingQueue<TaskHolder<R>> getBufferTaskHolders();

}
