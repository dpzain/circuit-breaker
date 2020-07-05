package com.dpzain.breaker.strategy.impl;

import com.dpzain.breaker.strategy.BreakerStrategy;
import com.dpzain.breaker.constant.Status;

/**
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public abstract class AbstractBreakerStategy<R> implements BreakerStrategy<R>{

    /**
     *  open or closed  不考虑半开 优化    TODO  断路器状态 不应该和策略放在一起;
     */
    protected volatile Status status;


    public AbstractBreakerStategy() {
    }

    public AbstractBreakerStategy(Status status) {
        this.status = status;
    }
}
