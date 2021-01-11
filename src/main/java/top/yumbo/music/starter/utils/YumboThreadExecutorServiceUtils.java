package top.yumbo.music.starter.utils;

import java.util.concurrent.*;

public class YumboThreadExecutorServiceUtils {
    //1.公平的线程队列，如果不传第二参数则默认是false
    static ArrayBlockingQueue<Runnable> workQueue1 = new ArrayBlockingQueue<>(10, true);
    //2.默认阻塞队列个数，空参构造方法会得到一个默认的线程队列值为Integer.MAX_VALUE 2的31次方
    static LinkedBlockingDeque<Runnable> workQueue2 = new LinkedBlockingDeque<>(10);

    /**
     *创建自定义的线程池示例
     */
    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            6, //核心线程6个
            20, //最大线程20个
            20, //空闲时间20秒（由下面的参数设置），非核心线程的空闲时间超过则销毁
            TimeUnit.SECONDS,//时间但是秒，如果要其它单位则换成对应的即可
            workQueue2,   //使用的是LinkedBlockingDeque，也可以使用ArrayBlockingQueue
            Executors.defaultThreadFactory(),//默认的线程工厂创建新线程
            new ThreadPoolExecutor.AbortPolicy()//直接拒绝新任务
    );
}
