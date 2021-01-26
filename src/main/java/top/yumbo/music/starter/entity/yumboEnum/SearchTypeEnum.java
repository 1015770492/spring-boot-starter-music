package top.yumbo.music.starter.entity.yumboEnum;

import java.util.HashMap;

/**
 * 枚举网易云音乐搜索类型
 * 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单,
 * 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合
 * QQ音乐则是：
 * 0：单曲，2：歌单，7：歌词，8：专辑，9：歌手，12：mv
 */
public enum SearchTypeEnum {
    SINGLE(1), ALBUM(10), SINGER(100), SONG_LIST(1000), USER(1002), MV(1004), LYRICS(1006), FM(1009), VIDEO(1014), COMPLEX(1018);
    public Integer type;// 搜索类型
    public static HashMap<SearchTypeEnum, Integer> mapQQType = new HashMap<>();// 映射qq音乐的搜索类型
    public static HashMap<Integer,SearchTypeEnum> neteaseSearchType=new HashMap<>();

    SearchTypeEnum(Integer type) {
        this.type = type;// 这个type只是为了在IDEA中的提示显示对应的值而设置
    }

    static {
        neteaseSearchType.put(1,SINGLE);// 网易云音乐的单曲值是1
        neteaseSearchType.put(10,ALBUM);
        neteaseSearchType.put(100,SINGER);
        neteaseSearchType.put(1000,SONG_LIST);
        neteaseSearchType.put(1002,USER);
        neteaseSearchType.put(1004,MV);
        neteaseSearchType.put(1006,LYRICS);
        neteaseSearchType.put(1009,FM);
        neteaseSearchType.put(1014,VIDEO);
        neteaseSearchType.put(1018,COMPLEX);
        mapQQType.put(SINGLE, 0);// qq音乐单曲对应的值是0
        mapQQType.put(SONG_LIST, 2);
        mapQQType.put(LYRICS, 7);
        mapQQType.put(ALBUM, 8);
        mapQQType.put(SINGER, 9);
        mapQQType.put(MV, 12);
    }

    /**
     * 根据值获取枚举对象
     * @param type 枚举的值
     * @return 对应的枚举
     */
    public static SearchTypeEnum getEnum(Integer type){
        return neteaseSearchType.get(type);
    }

}
