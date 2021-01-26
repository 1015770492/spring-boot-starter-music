package top.yumbo.music.starter.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
 
/**
 * 线程池配置属性类
 */
@Data
@ConfigurationProperties(prefix = "yumbo.music.task.pool")
public class YumboThreadPoolProperties {

    private Integer corePoolSize;// 核心池大小
    private Integer maxPoolSize;// 最大池大小，非核心=最大-核心
    private Integer keepAliveTime;// 存活时间秒
    private Integer workQueueCapacity;// 工作队列容量
    private Boolean workQueueFair;// 工作队列是否是公平队列

}