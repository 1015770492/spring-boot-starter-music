package top.yumbo.music.starter.entity.abstractMusic;

import lombok.Data;

@Data
public abstract class AbstractYumboMV {
    private String type;
    private String data;
    private String videos;
    private String mvId;
    private String mvName;
    private String mvPictureUrl;
    private String singers;
    private String durationTime;
    private Integer br;

}
