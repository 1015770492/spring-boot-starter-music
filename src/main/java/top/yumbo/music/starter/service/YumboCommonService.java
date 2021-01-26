package top.yumbo.music.starter.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.yumbo.music.starter.entity.yumboEnum.SearchTypeEnum;
import top.yumbo.music.starter.util.BiAsyncRequestUtils;

@Service
public class YumboCommonService {

    @Value("${yumbo.music.service.limit}")
    Integer defaultLimit;

    /**
     * 公共接口，提供给其它方法使用
     * keywords:搜索词
     * limit：返回的条数限制
     * offset: 偏移量
     * type: 搜索类型
     * 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单,
     * 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合
     */
    public JSONObject search(JSONObject neteaseQueryParameter) {
        /**
         * 转换查询参数，处理QQ音乐部分数据
         */
        final JSONObject qqMusicParameter = new JSONObject();// 创建qq需要使用的参数

        final int type = neteaseQueryParameter.getIntValue("type");// 获取传来的搜索类型值
        // 策略模式+枚举处理搜索类型的映射关系
        final Integer qqType = SearchTypeEnum.mapQQType.get(SearchTypeEnum.getEnum(type));// 获取qq对应的搜索类型的值
        qqMusicParameter.put("t", qqType);
        qqMusicParameter.put("key", neteaseQueryParameter.get("keywords"));// 添加搜索词
        int limit = neteaseQueryParameter.getIntValue("limit");// 分页的大小，后面根据偏移量计算出页数
        // 如果传入的limit>0则直接使用传入的值
        int value = (limit > 0 ? limit : (defaultLimit > 0 ? defaultLimit : 30));// 没有设置默认值则使用30，否则使用yml的默认值
        neteaseQueryParameter.replace("limit", limit, value);// 注意这个
        // 计算出qq音乐需要的页数
        int pageNo = (neteaseQueryParameter.getIntValue("offset") / value) + 1;
        qqMusicParameter.put("pageSize", value);// qq的一页的个数是pageSize这个参数
        qqMusicParameter.put("pageNo", pageNo);// 加入参数中
        System.out.println(neteaseQueryParameter);
        // 通过反射调用工具进行方法的调用
        final JSONObject result = BiAsyncRequestUtils.invokeMethod("/cloudsearch", neteaseQueryParameter, "/search", qqMusicParameter);
        return result;
    }

}
