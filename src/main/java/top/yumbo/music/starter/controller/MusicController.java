package top.yumbo.music.starter.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yumbo.music.starter.configuration.properties.YumboMusicServiceNeteaseProperties;
import top.yumbo.music.starter.configuration.properties.YumboMusicServiceQQProperties;
import top.yumbo.music.starter.service.YumboMusicService;

/**
 * 以网易云音乐为主，对于网易云音乐中版权问题的去QQ音乐下载资源
 */
@RestController
@RequestMapping(value = "{yumbo.music.server.prefix}", method = {RequestMethod.GET, RequestMethod.POST})
public class MusicController {

    @Autowired
    YumboMusicService yumboMusicService;


    @Autowired
    YumboMusicServiceQQProperties qqProperties;
    @Autowired
    YumboMusicServiceNeteaseProperties neteaseProperties;

    @RequestMapping("/music/{server}")
    public String getServerUrl(@PathVariable(value = "server", required = true) String server) {
        System.out.println(server);
        if ("netease".equals(server)) {
            return "网易云音乐服务器地址: "+neteaseProperties.getServer();
        } else if ("qq".equals(server)){
            return "qq服务器地址:"+qqProperties.getServer();
        }else {
            return "您输入的"+server+"的api服务不存在服务器地址";
        }
    }

    /**
     * keywords:搜索词
     * limit：返回的条数限制
     * offset: 偏移量
     * type: 搜索类型
     * 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单,
     * 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合
     */
    @RequestMapping("/search/music")
    public JSONObject searchMusic(@RequestBody(required = false) JSONObject jsonObject,
                                  @RequestParam(name = "keywords", required = false) String keywords,
                                  @RequestParam(name = "limit", required = false, defaultValue = "0") Integer limit,
                                  @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {
        if (jsonObject == null) {
            JSONObject parameter = new JSONObject();
            if (keywords == null || keywords == "") {
                parameter.put("msg", "请传入搜索词：keywords");
                parameter.put("可选参数1", "limit：一次性返回的数量，默认是30*2，也就是传入的值的2倍，是两个api返回的数据的总和");
                parameter.put("可选参数2", "offset：偏移量，默认值0,offset=页数*每页的条数limit，用于分页");
                return parameter;
            }
            parameter.put("keywords", keywords);
            parameter.put("limit", limit);
            parameter.put("offset", offset);
            jsonObject = parameter;
        }
        jsonObject.put("type", "1");
        return yumboMusicService.searchMusic(jsonObject);
    }


    /**
     * 获取音乐播放链接
     *
     * @param jsonObject id的封装类
     * @param musicId    单个musicId
     * @param br         码率:默认 999000
     *                   默认 128 // 128：mp3 128k，320：mp3 320k，m4a：m4a格式 128k，flac：flac格式 无损，ape：ape格式 无损
     * @return
     */
    @RequestMapping("/song/url")
    public JSONObject songUrl(@RequestBody(required = false) JSONObject jsonObject,
                              @RequestParam(name = "type", required = false) String type,
                              @RequestParam(name = "musicId", required = false) String musicId,
                              @RequestParam(name = "br", required = false) String br) {
        if (jsonObject == null || jsonObject.getString("musicId").equals("")) {
            JSONObject parameter = new JSONObject();
            if (musicId == null || musicId.equals("")) {
                parameter.put("msg", "请传入歌曲的musicId：id");
                parameter.put("可选参数br", "br：");
                return parameter;
            }
            parameter.put("id", musicId);// 添加查询参数id
            parameter.put("br", br);//码率
            jsonObject = parameter;
        }
        JSONObject result = new JSONObject();

        JSONObject songUrlJson = yumboMusicService.songUrl(type, "/song/url", jsonObject);
        if (type.equals("netease")) {
            final JSONObject data = songUrlJson.getJSONArray("data").getJSONObject(0);
            result.put("url",data.getString("url"));
            result.put("id",data.getString("id"));
            result.put("br",data.getString("br"));
        } else if (type.equals("qq")) {
            result.put("url", songUrlJson.getString("data"));
            result.put("id", musicId);
            result.put("br", br);
        }

        return result;
    }


}
