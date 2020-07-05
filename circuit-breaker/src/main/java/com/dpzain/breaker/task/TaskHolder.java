package com.dpzain.breaker.task;

import java.util.Objects;

/**
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public class TaskHolder<R> {
    private final Task<R> task;
    //任务的开始时间
    private final long startedTimestamp;
    //任务的结束时间
//    private final long endTimestamp;

    //要么成功 要么失败 暂不区分失败类型 TODO
    private volatile Stats taskStats;


    /**
     * Task 状态
     */
    public enum Stats{
        SUCCESS,
        FAILED
    }

    public TaskHolder(Task<R> task, long startedTimestamp) {
        this.task = task;
        this.startedTimestamp = startedTimestamp;
//        this.endTimestamp = 0;
        taskStats = Stats.SUCCESS;
    }


    public void setTaskStats(Stats taskStats) {
        this.taskStats = taskStats;
    }

    public long getStartedTimestamp() {
        return startedTimestamp;
    }

    public Task<R> getTask() {
        return task;
    }

    public Stats getTaskStats() {
        return taskStats;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TaskHolder)) {
            return false;
        }

        TaskHolder<?> that = (TaskHolder<?>) other;
        return (startedTimestamp == that.startedTimestamp) &&
                Objects.equals(task, that.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, startedTimestamp);
    }

    @Override
    public String toString() {
        return this.getTaskStats().equals(Stats.FAILED)?"1":"0";
    }
}
