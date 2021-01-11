package top.yumbo.music.starter.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yumbo.music.starter.service.YumboMusicService;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;

/**
 * 以网易云音乐为主，对于网易云音乐中版权问题的去QQ音乐下载资源
 */
@RestController
public class MusicController {

    @Autowired
    YumboMusicService yumboMusicService;

    /**
     * keywords:搜索词
     * limit：返回的条数限制
     * offset: 偏移量
     * type: 搜索类型
     * 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单,
     * 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合
     */
    @GetMapping("/search")
    @ResponseBody
    public JSONObject search(@RequestBody(required = false) JSONObject jsonObject,
                             @RequestParam(name = "keywords",required = false) String keywords,
                             @RequestParam(name = "limit",required = false) String limit,
                             @RequestParam(name = "offset",required = false) String offset,
                             @RequestParam(name = "type",required = false) String type) {
        final JSONObject parameter = new JSONObject();
        if (jsonObject == null) {
            parameter.put("keywords", keywords);
            parameter.put("limit", limit);
            parameter.put("offset", offset);
            parameter.put("type", type);
            jsonObject = parameter;
        }
        return yumboMusicService.search(jsonObject);
    }

}
