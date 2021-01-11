package top.yumbo.music.starter.autoconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;
import top.yumbo.util.music.musicImpl.qq.QQMusicInfo;

@Configuration
public class YumboMusicAutoConfiguration {

    @Bean
    public NeteaseCloudMusicInfo neteaseCloudMusicInfo(){
        return new NeteaseCloudMusicInfo();
    }
    @Bean
    public QQMusicInfo qqMusicInfo(){
        return new QQMusicInfo();
    }

}
