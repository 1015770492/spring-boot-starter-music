package top.yumbo.music.starter.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yumbo.music.starter.utils.BiAsyncRequestUtils;
import top.yumbo.music.starter.entity.type.SearchTypeEnum;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;
import top.yumbo.util.music.musicImpl.qq.QQMusicInfo;

@Service
public class YumboMusicService {
    @Autowired
    NeteaseCloudMusicInfo neteaseCloudMusicInfo;

    @Autowired
    QQMusicInfo qqMusicInfo;

    /**
     * keywords:搜索词
     * limit：返回的条数限制
     * offset: 偏移量
     * type: 搜索类型
     * 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单,
     * 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合
     */
    public JSONObject search(JSONObject neteaseMusicParameter) {
        // 处理QQ音乐部分数据
        final JSONObject qqMusicParameter = new JSONObject();// 创建qq需要使用的参数
        final int type = neteaseMusicParameter.getIntValue("type");// 获取传来的搜索类型值
        // 策略模式+枚举处理搜索类型的映射关系
        final Integer qqType = SearchTypeEnum.mapQQType.get(SearchTypeEnum.getEnum(type));// 获取qq对应的搜索类型的值
        qqMusicParameter.put("t", qqType);
        qqMusicParameter.put("key", neteaseMusicParameter.get("keywords"));// 添加搜索词
        final int limit = neteaseMusicParameter.getIntValue("limit");// 分页的大小，后面根据偏移量计算出页数
        qqMusicParameter.put("pageSize", limit);// qq的一页的个数是pageSize这个参数
        int pageNo = (neteaseMusicParameter.getIntValue("offset") / limit) + 1;// 计算出页数
        qqMusicParameter.put("pageNo", pageNo);// 加入参数中
        final JSONObject result = BiAsyncRequestUtils.invokeMethod("/cloudsearch", neteaseMusicParameter, "/search", qqMusicParameter);

        return result;
    }




}
