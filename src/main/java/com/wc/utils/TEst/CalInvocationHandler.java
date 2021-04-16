package com.wc.utils.TEst;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CalInvocationHandler implements InvocationHandler {
    //持有真实对象
    Object target;

    //从外部传入真实类
    public CalInvocationHandler(Object target) {
        this.target = target;
    }

    /**
     * 调用代理对象的任何方法都会间接调用invoke()方法
     * @param proxy-该InvocationHandler持有的真实对象
     * @param method-代理对象调用的方法的反射类，通过该参数可以获取代理对象调用的方法的名称等基本信息，以此判断该提供什么控制
     * @param args-调用方法时传入的参数
     */
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        //如果是执行cal1，另起线程
        if("longTimeOperation".equals(method.getName())) {
            //另开一个线程
            new Thread(new Runnable() {
                public void run() {
                    try {

                        //反射调用方法
                        method.invoke(target, args);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return null;
        }
        //如果是执行cal2，提供计时控制
        else if("longTimeSorting".equals(method.getName())) {
            long start = System.currentTimeMillis();

            //反射调用方法
            method.invoke(target, args);

            long end = System.currentTimeMillis();
            System.out.println("sorting uses " + (end - start)/1000.0 + "s");
            return null;
        }

        //如果是其他方法请求，一律不受理
        return null;
    }

    /**
     * 使用Proxy.newProxyInstance创建this持有的真实对象的代理对象
     * @return 返回InvocationHandler持有的真实对象的代理
     */
    public Object getProxy() {
        //提供真实对象的类加载器，接口Class，以及InvocationHandler本身，直接创建一个代理对象
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }
}