package com.dpzain.breaker.task.context;

import com.dpzain.breaker.task.context.TaskContext;
import com.dpzain.breaker.task.TaskHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * context 配合断路器  有很强的扩展性  (暂时废弃)
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 */
public class DefaultTaskContext<R> implements TaskContext<R> {

    /**
     * recent task
     */
    private final BlockingQueue<TaskHolder<R>> bufferTask;


    /**
     * running all task
     */
    private final ConcurrentHashMap<String, TaskHolder<R>> taskHolders;


    public DefaultTaskContext(ConcurrentHashMap<String, TaskHolder<R>> taskHolders,BlockingQueue<TaskHolder<R>> bufferTask) {
        this.taskHolders = taskHolders;
        this.bufferTask = bufferTask;
    }

    @Override
    public Collection<TaskHolder<R>> getHolders() {
        return Collections.unmodifiableCollection(taskHolders.values());
    }

//    @Override
//    public BlockingQueue<TaskHolder<R>> getBufferTaskHolders() {
//        return bufferTask;
//    }

}
