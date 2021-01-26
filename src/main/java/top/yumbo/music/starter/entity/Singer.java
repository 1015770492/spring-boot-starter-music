package top.yumbo.music.starter.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Singer implements Serializable {
    private int sid;// 歌手id
    private String name;// 歌手姓名
}
