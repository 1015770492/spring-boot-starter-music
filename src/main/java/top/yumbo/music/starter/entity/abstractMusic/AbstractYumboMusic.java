package top.yumbo.music.starter.entity.abstractMusic;

import lombok.Data;

@Data
public abstract class AbstractYumboMusic {
    private String type;
    private String data;
    private String songs;
    private String musicId;
    private String songName;
    private String mvId;
    private String durationTime;
    private String albumId;
    private String AlbumName;
    private String albumPictureUrl;
    private String singers;
    private Integer br;// 码率
}
