package top.yumbo.music.starter.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "yumbo.music.service.qq")
public class YumboMusicServiceQQProperties {
    String server;
    String account;
    String password;
}
