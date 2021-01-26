package top.yumbo.music.starter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.yumbo.music.starter.configuration.properties.YumboThreadPoolProperties;
import top.yumbo.music.starter.util.BiAsyncRequestUtils;

import java.util.concurrent.*;

/**
 * 创建线程池配置类
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(YumboThreadPoolProperties.class) // 表示He
public class YumboThreadPoolExecuterAutoConfiguration {

    @Autowired
    private YumboThreadPoolProperties yumboThreadPoolProperties;// 获取配置信息

    @Bean
    public BiAsyncRequestUtils biAsyncRequestUtils() {
        int corePoolSize = yumboThreadPoolProperties.getCorePoolSize() == null ? 6 : yumboThreadPoolProperties.getCorePoolSize();
        int maximumPoolSize = yumboThreadPoolProperties.getMaxPoolSize() == null ? 20 : yumboThreadPoolProperties.getMaxPoolSize();
        int keepAliveTime = yumboThreadPoolProperties.getKeepAliveTime() == null ? 10 : yumboThreadPoolProperties.getKeepAliveTime();
        int workQueueCapacity = yumboThreadPoolProperties.getWorkQueueCapacity() == null ? 10 : yumboThreadPoolProperties.getWorkQueueCapacity();
        boolean workQueueFair = yumboThreadPoolProperties.getWorkQueueFair();// 默认值就是false

        //1.公平的线程队列，如果不传第二参数则默认是false
        ArrayBlockingQueue<Runnable> workQueue1 = new ArrayBlockingQueue<>(workQueueCapacity, workQueueFair);
        /**
         *创建自定义的线程池示例
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize, //核心线程默认6个
                maximumPoolSize, //最大线程20个
                keepAliveTime, //空闲时间20秒（由下面的参数设置），非核心线程的空闲时间超过则销毁
                TimeUnit.SECONDS,//时间但是秒，如果要其它单位则换成对应的即可
                workQueue1,   //使用的是LinkedBlockingDeque，也可以使用ArrayBlockingQueue
                Executors.defaultThreadFactory(),//默认的线程工厂创建新线程
                new ThreadPoolExecutor.AbortPolicy()//直接拒绝新任务
        );
        BiAsyncRequestUtils.setExecutorService(threadPoolExecutor);// 添加线程池进去
        return new BiAsyncRequestUtils();
    }


}