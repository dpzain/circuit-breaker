package com.dpzain.breaker.task;

import com.dpzain.breaker.task.Task;

import java.lang.reflect.Method;

/**
 * 方法 Task
 * @auther zhangyu(dpzain)
 * @date 2020/7/5
 *
 */
public class MethodTask<R, T> implements Task<R> {
    private final T object;
    private final Object[] methodParams;
    private final Method method;


    public MethodTask(T object, String methodName, Class<R> returnType, Object... methodParams) throws NoSuchMethodException {
        this.object = object;
        this.methodParams = methodParams;
        //方法参数类型
        Class<?>[] methodParamTypes = new Class[methodParams.length];
        for (int i = 0; i < methodParams.length; i++) {
            methodParamTypes[i] = methodParams[i].getClass();
        }
        method = object.getClass().getDeclaredMethod(methodName, methodParamTypes);
        Class<?> realReturnType = method.getReturnType();
        if (returnType != realReturnType)
            throw new NoSuchMethodException("returnType error!!");
    }

    @Override
    @SuppressWarnings("unchecked")
    public R execute() throws Exception {
        return (R) method.invoke(object, methodParams);
    }

}
