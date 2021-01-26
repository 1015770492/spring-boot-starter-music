package top.yumbo.music.starter.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 这是一个放回的数据模板
 * 音乐实体类，定义云博音乐的音乐信息
 */
@Data
public class YumboMusic implements Serializable {
    private String type;// 类型
    private String musicId;// 歌曲id
    private String albumId;// 专辑id
    private String albumPictureUrl;// 专辑图片地址，qq音乐的这个字段对于专辑图片的id
    private String songName;// 歌曲名称
    private String durationTime;// 歌曲时长
    private String mvId;// 视频id
    private String url;// 播放地址
    private String singer;// 歌手
}
