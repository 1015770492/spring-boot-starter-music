package top.yumbo.music.starter.entity.qq;


import top.yumbo.music.starter.entity.abstractMusic.AbstractYumboMusic;

public class QQMusic extends AbstractYumboMusic {
    {
        // 这些方法传入的值对于api返回的字段的值 目的是映射Type、data
        setType("qq");
        setData("data");
        setSongs("list");
        setMusicId("songmid");
        setSongName("songname");
        setMvId("vid");
        setDurationTime("interval");// 单位秒
        setAlbumId("albummid");
        setAlbumName("albumname");
        setAlbumPictureUrl("albummid");// qq没有直接返回封面，统一在点击的时候用
        setSingers("singer");
    }

}
