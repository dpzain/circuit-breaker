package com.dpzain;


import com.dpzain.breaker.CircuitBreaker;
import com.dpzain.breaker.CircuitBreakerRunner;
import com.dpzain.breaker.DefaultCircuitBreaker;
import com.dpzain.breaker.exception.TaskException;
import com.dpzain.breaker.handler.BreakerHandler;
import com.dpzain.breaker.handler.impl.DefaultHandler;
import com.dpzain.breaker.strategy.BreakerStrategy;
import com.dpzain.breaker.strategy.impl.DefaultBreakerStrategy;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public class MockTest {


    private ExecutorService executor = Executors.newFixedThreadPool(20);


    @Test
    public void reflectTest() {
        AtomicInteger param = new AtomicInteger(0);
        MockService myCaller = new MockService();
        final CircuitBreakerRunner<String, MockService> breaker = new CircuitBreakerRunner<>(myCaller, "mock");
//        AtomicBoolean openFlag= new AtomicBoolean(false);
        while (true) {
            //模拟大约每 500ms  防止 无界队列
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                String result = null;
                try {
                    result = breaker.runService(param.incrementAndGet());
                    System.out.println(Thread.currentThread().getName() + ":" + result);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                System.out.println("result:" + result);
                System.out.println();
            });
//            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private final MockService mockService;
    private final CircuitBreaker<String> circuitBreaker;
    private final BreakerHandler<String> breakHandler;
    private final BreakerStrategy<String> breakStrategy;


    public MockTest() {
        this.mockService = new MockService();
        // prepare the circuit breaker
        this.circuitBreaker = new DefaultCircuitBreaker<>();
        this.breakHandler = new DefaultHandler<>();
        this.breakStrategy = new DefaultBreakerStrategy<>(10, 50, 5000);
    }


    @Test
    public void test() {
        try {
            String execute = circuitBreaker.execute(() -> mockService.mock(1), breakStrategy, breakHandler);
            System.out.println(execute);
        } catch (TaskException e) {
            System.out.println("executed resulted in exception: " + e.getTaskException());
            throw new RuntimeException(e.getTaskException().getMessage(), e.getTaskException());
        }
    }

//    public static void main(String[] args) {
//        MockService mockService = new MockService();
//        try {
//            Task<String> task = new MethodTask<>(mockService, "mock", String.class, 1);
//            task.execute();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
