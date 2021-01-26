package top.yumbo.music.starter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.yumbo.music.starter.configuration.properties.YumbMusicServiceProperties;
import top.yumbo.music.starter.configuration.properties.YumboMusicServiceNeteaseProperties;
import top.yumbo.music.starter.configuration.properties.YumboMusicServiceQQProperties;
import top.yumbo.music.starter.configuration.properties.YumboThreadPoolProperties;
import top.yumbo.music.starter.entity.netease.NeteaseCloudMusicInfo;
import top.yumbo.music.starter.entity.qq.QQMusicInfo;
import top.yumbo.music.starter.entity.yumboEnum.MusicEnum;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({
        YumboMusicServiceQQProperties.class,
        YumboMusicServiceNeteaseProperties.class,
        YumbMusicServiceProperties.class})
@ComponentScan("top.yumbo.music.starter.*")
// 表示He
public class YumboMusicAutoConfiguration {

    @Autowired
    YumboMusicServiceQQProperties qqProperties;
    @Autowired
    YumboMusicServiceNeteaseProperties neteaseProperties;

    @Bean
    public YumbMusicServiceProperties yumbMusicServerProperties() {
        return new YumbMusicServiceProperties();
    }

    @Bean
    public YumboThreadPoolProperties yumboThreadPoolProperties() {
        return new YumboThreadPoolProperties();
    }

    /**
     * 注入网易云音乐api服务
     *
     * @return 网易云音乐api服务工具类
     */
    @Bean
    public NeteaseCloudMusicInfo neteaseCloudMusicInfo() {
        final NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();
        neteaseCloudMusicInfo.setLoginType(neteaseProperties.getLoginType());// 设置登录类型
        MusicEnum.setBASE_URL_163Music(neteaseProperties.getServer());// 设置网易云音乐服务器地址
        return neteaseCloudMusicInfo;// 返回注入服务器地址和默认登录类型的工具类
    }

    /**
     * qq音乐只实现qq登录
     * 注入qq音乐的api服务
     *
     * @return qq的api工具类
     */
    @Bean
    public QQMusicInfo qqMusicInfo() {
        final QQMusicInfo qqMusicInfo = new QQMusicInfo();
        MusicEnum.setBASE_URL_QQMusic(qqProperties.getServer());// 注入qq音乐服务器地址
        return qqMusicInfo;
    }


}
