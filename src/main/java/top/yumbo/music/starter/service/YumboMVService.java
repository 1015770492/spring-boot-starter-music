package top.yumbo.music.starter.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yumbo.music.starter.entity.netease.NeteaseMV;
import top.yumbo.music.starter.entity.qq.QQMV;
import top.yumbo.music.starter.entity.abstractMusic.AbstractYumboMV;
import top.yumbo.music.starter.util.BiAsyncRequestUtils;
import top.yumbo.music.starter.util.JsonCombineUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class YumboMVService {

    @Autowired
    YumboCommonService yumboCommonService;

    /**
     * 搜索mv
     *
     * @param parameter 请求参数
     * @return 返回处理后的视频信息
     */
    public JSONObject searchMv(JSONObject parameter) {
        final JSONObject searchSet = yumboCommonService.search(parameter);
        final List<JSONObject> jsonMVList1 = getJsonMVList(searchSet, new NeteaseMV());
        final List<JSONObject> jsonMVList2 = getJsonMVList(searchSet, new QQMV());
        int qqMusicSize = searchSet.getJSONObject("qq").getJSONObject("data").getIntValue("total");
        int neteaseMusicSize = searchSet.getJSONObject("netease").getJSONObject("result").getIntValue("mvCount");
        int sum = qqMusicSize + neteaseMusicSize;
        final JSONObject jsonObject = JsonCombineUtils.combineJsonList(sum, "videos", jsonMVList1, jsonMVList2);
        return jsonObject;
    }

    /**
     * 提取返回的结果中的信息
     *
     * @param searchSet       传入的所有数据集合
     * @param abstractYumboMV 要提前的信息模板数据
     * @return 转换后的json数据
     */
    private List<JSONObject> getJsonMVList(JSONObject searchSet, AbstractYumboMV abstractYumboMV) {
        final JSONObject result = searchSet.getJSONObject(abstractYumboMV.getType()).getJSONObject(abstractYumboMV.getData());

        JSONArray videosList = result.getJSONArray(abstractYumboMV.getVideos());
        final boolean qq = abstractYumboMV.getType().equals("qq");
        final List<JSONObject> collect = videosList.stream().map(v -> {
            JSONObject video = (JSONObject) v;
            final JSONObject music = new JSONObject();
            music.put("type", abstractYumboMV.getType());// qq 还是 netease

            music.put("mvId", video.getString(abstractYumboMV.getMvId()));// 视频id
            music.put("mvName", video.getString(abstractYumboMV.getMvName()));// mv名称
            music.put("mvPictureUrl", video.get(abstractYumboMV.getMvPictureUrl()));// mv的封面

            int time = video.getIntValue(abstractYumboMV.getDurationTime());
            if (!qq) {
                time /= 1000;// 网易云音乐是毫秒，而qq音乐是秒，因此这里将时间处理一下，统一返回秒
            }
            music.put("durationTime", time);// 时长，单位毫秒
            // 歌手我这里没有处理，而是直接返回
            music.put("singer", video.getJSONArray(abstractYumboMV.getSingers()));// 歌手
            return music;
        }).collect(Collectors.toList());

        return collect;
    }

    public JSONObject mvUrl(String serverProvider, String relativeUrl, JSONObject jsonObject) {
        return BiAsyncRequestUtils.doInvoke(serverProvider, relativeUrl, jsonObject);
    }


}
