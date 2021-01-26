package top.yumbo.music.starter.configuration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "yumbo.music.service")
public class YumbMusicServiceProperties {
    @Value("${yumbo.music.service.port:8888}")
    String port;
    @Value("${yumbo.music.service.prefix:api}")
    String prefix;
    @Value("${yumbo.music.service.limit:30}")
    Integer limit;
}
