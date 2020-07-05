package com.dpzain.breaker.strategy.impl;

import com.dpzain.breaker.constant.Status;
import com.dpzain.breaker.task.context.TaskContext;
import com.dpzain.breaker.task.TaskHolder;
import com.dpzain.breaker.util.SystemClock;

import java.util.Iterator;
import java.util.concurrent.*;

/**
 * 默认断路器策略 （计数 近N次执行  M次失败）
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public class DefaultBreakerStrategy<R> extends AbstractBreakerStategy<R> {


//    private static final int DEFAULT_BUFFER_TASK_CAPACITY = 100; //默认缓存最近一百个请求
//
//    private static final int MAX_BUFFER_TASK_CAPACITY = 1000; //最大缓存

    /**
     *  缓存最近的请求buffer
     */
    private BlockingQueue<TaskHolder<R>> bufferTask;


//    private final RingBuffer<TaskHolder<R>> bufferTask;


    /**
     * 失败阈值 M
     */
    private int threshold;

    /**
     * 最近 N 次请求
     */
    private int taskWindow;

    /**
     * 延迟  毫秒
     */
    private long delayTime;

    /**
     *  该策略 最近一次 熔断的时间
     */
    private volatile long lastOpenTime;


    public DefaultBreakerStrategy(int threshold, int taskWindow, long delayTime) {
        super(Status.CLOSED);//默认关闭
        if (taskWindow > 100)
            taskWindow = 100;
        if (taskWindow <= 0)
            taskWindow = 50;
        if (threshold >= taskWindow)
            threshold = taskWindow / 2;
        this.threshold = threshold;
        this.taskWindow = taskWindow;
        this.delayTime = delayTime;
        this.lastOpenTime = 0;
        this.bufferTask = new LinkedBlockingQueue<>(taskWindow * 2);
    }

    @Override
    public boolean isBreaker(TaskHolder<R> taskHolder, TaskContext<R> context) {
        long now = SystemClock.now();
        if (Status.OPEN.equals(status) && now - this.lastOpenTime < delayTime)
            return true;
        if (Status.OPEN.equals(status)) {//reset
            //打开状态且已到延迟  上次熔断之后自动关闭 第一次尝试,清空队列; 避免 可能出现这次成功的请求入队 下次又立马拒绝;  清空也不太合理  最好加个半开状态  尝试去close 逐渐恢复; 类似tcp滑动窗口拥塞控制
            bufferTask.clear();
            status = Status.CLOSED;
            return false;
        }
        int offerCount;
        if ((offerCount = bufferTask.size() - taskWindow) >= 0) {
            // 取出队尾 taskWindow个元素 做失败率统计
            Iterator<TaskHolder<R>> iterator = bufferTask.iterator();
            int i = 0, failCount = 0;
            while (iterator.hasNext()) {
                if (i++ >= offerCount) {
                    TaskHolder<R> next = iterator.next();
                    failCount += next.getTaskStats().equals(TaskHolder.Stats.FAILED) ? 1 : 0;
                }
            }
            if (failCount >= threshold) {
                status = Status.OPEN;
                lastOpenTime = now;
                return true;
            }
//                bufferTask.iterator().forEachRemaining(e -> System.out.print(e.toString()));
//                System.out.println();
        }// 缓存的请求不到 window个 不做统计(刚开始初始化  即使 所有请求 全失败了 也继续放行 （颗粒度问题）)
        return false;
    }

    @Override
    public void submitMetric(TaskHolder<R> taskHolder, TaskHolder.Stats stats) {
        taskHolder.setTaskStats(stats);
        synchronized (this) {
            //阻塞队列 做缓存不好的地方
//                    // 存在问题 高并发状态下 先来的请求 可能后入队 (后入队的就是最近的请求 也没毛病 改用Lock 实现公平锁也OK )
//                    // fixme  可采用observer模式基于异步的事件 封装 task开始执行 执行完毕 超时 异常 等等事件  去上报和汇总task执行状态，比如RxJava的Observable.window() ; 这里简单记录正在执行的和最近执行的task
            if (!bufferTask.offer(taskHolder)) {
                bufferTask.poll();
                bufferTask.offer(taskHolder);
            }
        }
    }


//    /**
//     * 循环队列 装多个窗口 至少为 windowSize 2倍
//     */
//    private volatile int[] requestSlices;
//    /**
//     * 队列的总长度
//     */
//    private volatile int requestSliceSize;
//
//    /**
//     * 窗口长度 windowSize 个时间片 (请求数）
//     */
//    private volatile int windowSize;
//
//    /**
//     * 失败次数 阈值
//     */
//    private int failThreshold;
//
//    /**
//     * 滑窗的起始 位置
//     */
//    private volatile int begin;
//    /**
//     * 滑窗的终点 位置
//     */
//    private volatile int last;
//
//    public DefaultBreakerStrategy(int windowSize) {
//        this.begin = 0;
//        this.last = windowSize - 1;
//        this.windowSize = windowSize;
//        this.requestSliceSize = windowSize * 2 + 1;
//    }
//
//    private void initTimeSlices() {
//        if (requestSlices != null) {
//            return;
//        }
//        int[] localTimeSlices = new int[requestSliceSize];
//        for (int i = 0; i < requestSliceSize; i++) {
//            localTimeSlices[i] = 0;
//        }
//        requestSlices = localTimeSlices;
//    }
//
//
//    /**
//     * 请求返回、超时、异常 无论如何 移动窗口  success set 0 else 1, 并返回窗口中所有 的失败请求计数总和
//     * @return
//     */
//    public synchronized int incrementAndSum(boolean isError) {
//        initTimeSlices();
//        int sum = 0;
//        if (++begin >= requestSliceSize)
//            begin = 0;
//        if (++last >= requestSliceSize)
//            last = 0;
//        requestSlices[last] = isError ? 1 : 0;
//
//        if (begin < last) {
//            for (int i = begin; i <= last; i++) {
//                sum += requestSlices[i];
//            }
//        } else {
//            for (int i = begin; i < requestSliceSize; i++) {
//                sum += requestSlices[i];
//            }
//            for (int i = 0; i < last; i++) {
//                sum += requestSlices[i];
//            }
//        }
//        return sum;
//    }

//    /**
//     * 清零 闭区间
//     */
//    private void clearBetween(int fromIndex, int toIndex) {
//        for (int i = fromIndex; i <= toIndex; i++) {
//            requestSlices[i]=0;
//        }
//    }
}
