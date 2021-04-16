package com.wc.utils.TEst;

public class CalServiceImplProxy implements CalService {
    //持有真实事物类对象
    private CalService calService;

    public CalServiceImplProxy(CalService calService) {
        //创建实际类
        this.calService = calService;
    }

    //增强功能：将计算过程另开线程
    public void longTimeOperation() {
        new Thread(new Runnable(){
            public void run() {
                calService.longTimeOperation();
            }
        }).start();
    }

    //增强功能:测试算法的时间效率
    public void longTimeSorting() {
        long start = System.currentTimeMillis();

        calService.longTimeSorting();

        long end = System.currentTimeMillis();
        System.out.println("sorting uses " + (end - start)/1000.0 + "s");
    }

    //测试方法
    public static void main(String[] args) {
        CalService calService = new CalServiceImplProxy(new CalServiceImpl());
        calService.longTimeOperation();
        calService.longTimeSorting();
        //后续的工作
        System.out.println("doing later work...");
    }
    //start sorting....
    //start calculating....
    //sorting finished
    //sorting uses 2.021s
    //doing later work...
    //longTimeOperation finished,sum = 2000000000
}