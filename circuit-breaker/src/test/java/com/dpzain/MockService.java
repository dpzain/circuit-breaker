package com.dpzain;

import java.util.Random;

/**
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public class MockService {
    private Random random = new Random();

    private static boolean[] randomFail;

    static {
        //模拟 接口随机失败率
        randomFail = new boolean[100];
        for (int i = 0; i < randomFail.length; i++) {
            randomFail[i] = i >= 30;
        }
    }

    public String mock(Integer paramId){

        System.out.println("mock started :" + paramId);

        try {
            Thread.sleep(random.nextInt(1000));//接口0-1s内返回
        } catch (InterruptedException ignored) {
        }

        if (!randomFail[random.nextInt(randomFail.length)]) {
//            System.out.println("Execution fialed!");
            throw new RuntimeException("Execution fialed!!!!");
        }
        System.out.println("mock finished :" + paramId);
        return "re_" + paramId;
    }
}