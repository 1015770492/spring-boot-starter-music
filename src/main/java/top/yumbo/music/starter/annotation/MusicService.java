package top.yumbo.music.starter.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通过该注解可以获取到
 */

@Retention(RetentionPolicy.RUNTIME) // 注解
@Target({ElementType.FIELD,ElementType.METHOD}) // 注解在局部变量中使用,以及字段使用
public @interface MusicService {
    String url() default "";// 这个url是相对路径，服务器地址需要通过枚举得到
    String serverAddress() default "";// 其它音乐服务
}
