package top.yumbo.music.starter.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yumbo.music.starter.entity.netease.NeteaseCloudMusicInfo;
import top.yumbo.music.starter.entity.netease.NeteaseMusic;
import top.yumbo.music.starter.entity.qq.QQMusic;
import top.yumbo.music.starter.entity.qq.QQMusicInfo;
import top.yumbo.music.starter.entity.abstractMusic.AbstractYumboMusic;
import top.yumbo.music.starter.util.BiAsyncRequestUtils;
import top.yumbo.music.starter.util.JsonCombineUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class YumboMusicService {
    @Autowired
    NeteaseCloudMusicInfo neteaseCloudMusicInfo;

    @Autowired
    QQMusicInfo qqMusicInfo;

    @Autowired
    YumboCommonService yumboCommonService;


    /**
     * 搜索单曲
     *
     * @param parameter 封装了keywords的json字符串
     * @return 统一后的json数据
     * <p>
     * type：netease或qq 表示来自哪一个音乐，前端通过传这个字段就知道是调用哪一个音乐api，默认网易云
     * musicId: 音乐id
     * songName: 歌曲名称
     * mvId: mv的id
     * durationTime: 时长
     * singer: 歌手数组
     * albumId: 专辑id
     * albumName: 专辑名称
     * albumPictureUrl: 专辑封面图片的图片地址，或者专辑的id（qq音乐则是将id再存一份，因为qq音乐搜索没有返回这个url）
     */
    public JSONObject searchMusic(JSONObject parameter) {
        final JSONObject emptyJson = new JSONObject();
        if (parameter == null) {
            return emptyJson;
        }
        final String keywords = parameter.getString("keywords");
        if (keywords == null || "" == keywords) {
            return emptyJson;
        }
        final JSONObject searchSet = yumboCommonService.search(parameter);// 返回qq音乐和网易云音乐的集合
        // qq音乐总条数，为了分页使用
        int qqMusicSize = searchSet.getJSONObject("qq").getJSONObject("data").getIntValue("total");
        int neteaseMusicSize = searchSet.getJSONObject("netease").getJSONObject("result").getIntValue("songCount");
        int sum = qqMusicSize + neteaseMusicSize;
        List<JSONObject> neteaseJsonList1 = getJsonSingleList(searchSet, new NeteaseMusic());// 解析并返回处理后的网易云List
        List<JSONObject> qqJsonList2 = getJsonSingleList(searchSet, new QQMusic());// 解析并返回处理后的qq音乐处理后的List

        // 合成一个json，{"total": 总和,"netease":来自网易云音乐的条数,"qq":来自qq音乐的条数,"data":[]}
        final JSONObject jsonObject = JsonCombineUtils.combineJsonList(sum, "songs", neteaseJsonList1, qqJsonList2);
        return jsonObject;
    }


    /**
     * 从json对象提取出网易或qq 音乐的数据
     *
     * @param searchSet          搜索得到的所有数据
     * @param abstractYumboMusic 音乐共有的数据
     * @return 转换后的网易云数据 或者 返回转换后的qq音乐数据
     * 返回的字段信息：
     * <p>
     * type：netease或qq
     * musicId: 音乐id
     * songName: 歌曲名称
     * mvId: mv的id
     * durationTime: 时长
     * singer: 歌手数组
     * albumId: 专辑id
     * albumName: 专辑名称
     * albumPictureUrl: 专辑封面图片的图片地址，或者专辑的id（qq音乐则是将id再存一份，因为qq音乐搜索没有返回这个url）
     */
    private List<JSONObject> getJsonSingleList(JSONObject searchSet, AbstractYumboMusic abstractYumboMusic) {
        final JSONObject result = searchSet.getJSONObject(abstractYumboMusic.getType()).getJSONObject(abstractYumboMusic.getData());
        JSONArray songs = result.getJSONArray(abstractYumboMusic.getSongs());
        final boolean qq = abstractYumboMusic.getType().equals("qq");// 判断是否是qq
        final List<JSONObject> collect = songs.stream().map(s -> {
            JSONObject song = (JSONObject) s;
            final JSONObject music = new JSONObject();
            music.put("type", abstractYumboMusic.getType());
            music.put("musicId", song.getString(abstractYumboMusic.getMusicId()));// 歌曲id
            music.put("songName", song.getString(abstractYumboMusic.getSongName()));// 歌曲名称
            music.put("mvId", song.get(abstractYumboMusic.getMvId()));// mv的id
            int time = song.getIntValue(abstractYumboMusic.getDurationTime());
            if (!qq) {
                time /= 1000;// 网易云音乐是毫秒，而qq音乐是秒，因此这里将时间处理一下，统一返回秒
            }
            music.put("durationTime", time);// 时长，单位毫秒
            // 歌手我这里没有处理，而是直接返回
            music.put("singer", song.getJSONArray(abstractYumboMusic.getSingers()));// 歌手

            JSONObject al = song.getJSONObject("al");//获取专辑信息
            if (qq) {
                al = song; // qq返回的专辑id在song就能获取到，通过这个操作将它重新指向song
            }
            music.put("albumId", al.getString(abstractYumboMusic.getAlbumId()));// 专辑id
            music.put("albumName", al.getString(abstractYumboMusic.getAlbumName()));// 专辑名称
            music.put("albumPictureUrl", al.getString(abstractYumboMusic.getAlbumPictureUrl()));// 专辑封面图片地址
            return music;
        }).collect(Collectors.toList());

        return collect;
    }


    public JSONObject songUrl(String type, String relativeUrl, JSONObject jsonObject) {
        if ("qq".equals(type)){
            jsonObject.put("type",jsonObject.get("br"));
            jsonObject.remove("br");
        }
        return BiAsyncRequestUtils.doInvoke(type, relativeUrl, jsonObject);
    }
}
