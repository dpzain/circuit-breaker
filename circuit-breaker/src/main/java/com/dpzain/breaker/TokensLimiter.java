//package com.dpzain.breaker;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Random;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * @auther zhangyu(dpzain)
// * @date 2020/7/4
// *
// */
//public class TokensLimiter {
//
//    private static Logger _log = LoggerFactory.getLogger(TokensLimiter.class);
//
//    private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5);
//    private static Random random = new Random();
//
//    // 桶的容量
//    private static int capacity = 10;
//    // 令牌生成速度 10/s
//    private static int rate = 10;
//    // 当前令牌数量
//    private volatile int tokens;
//
//    // 最后一次令牌发放时间
//    private volatile long last_timeStamp = System.currentTimeMillis();
//
//    public  void acquire() {
//        for (int i = 0; i < 100; i++) {
//            scheduledExecutor.schedule(() -> {
//                long now = System.currentTimeMillis();
//                // 当前令牌数
//                tokens = Math.min(capacity, (int) (tokens + (now - last_timeStamp) * rate / 1000));
//                //随机数量的令牌 请求
////                int permits = random.nextInt(9) + 1;
//                //每个任务 一个令牌
//                int permits = 1;
//                _log.info("请求令牌数：{},当前令牌数：{}", permits, tokens);
//                last_timeStamp = now;
////                try {
////                    Thread.sleep(random.nextInt(5000));
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//                if (tokens < permits) {
//                    // 若不到令牌,则拒绝
//                    _log.info("限流了");
//                } else {
//                    // 还有令牌，领取令牌
//                    tokens -= permits;
//                    _log.info("剩余令牌={}", tokens);
//                }
//            }, random.nextInt(5000), TimeUnit.MILLISECONDS);//每个任务 隔0-5 s 后执行
//        }
//    }
//
//    public static void main(String[] args) {
//        TokensLimiter tokensLimiter = new TokensLimiter();
//        try {
//            // 初始化令牌桶 和 最后时间戳 后；给点时间再给100个线程任务
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        tokensLimiter.acquire();
//    }
//
//}
