package top.yumbo.music.starter.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "yumbo.music.service.netease")
public class YumboMusicServiceNeteaseProperties {
    String server;
    String account;
    String password;
    String loginType;
}
