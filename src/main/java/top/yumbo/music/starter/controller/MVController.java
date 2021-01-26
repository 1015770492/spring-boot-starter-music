package top.yumbo.music.starter.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yumbo.music.starter.entity.yumboEnum.MVBitRateEnum;
import top.yumbo.music.starter.service.YumboMVService;

@RestController
@RequestMapping(value = "{yumbo.music.server.prefix}", method = {RequestMethod.GET, RequestMethod.POST})
public class MVController {
    @Autowired
    YumboMVService yumboMVService;


    @RequestMapping("/search/mv")
    public JSONObject searchMV(@RequestBody(required = false) JSONObject jsonObject,
                               @RequestParam(name = "keywords", required = false) String keywords,
                               @RequestParam(name = "limit", required = false) Integer limit,
                               @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {
        if (jsonObject == null || jsonObject.getString("keywords").equals("")) {
            JSONObject parameter = new JSONObject();
            if (keywords == null || keywords == "") {
                parameter.put("msg", "请传入搜索词：keywords");
                parameter.put("可选参数limit", "limit：一次性返回的数量，默认是30*2，也就是传入的值的2倍，是两个api返回的数据的总和");
                parameter.put("可选参数offset", "offset：偏移量，默认值0，offset=页数*每页的条数limit，用于分页");
                return parameter;
            }
            parameter.put("keywords", keywords);
            parameter.put("limit", limit);//默认每一个平台取30条，返回60条数据
            parameter.put("offset", offset);
            jsonObject = parameter;
        }
        jsonObject.put("type", "1004");// mv
        return yumboMVService.searchMv(jsonObject);
    }

    /**
     * 网易云码率：
     * qq音乐会一次性返回所有，码率和网易云音乐一样
     *
     * @param id 视频id
     * @param br 240、480、720、1080
     * @return
     */
    @RequestMapping("/mv/url")
    public JSONObject mvUrl(@RequestBody(required = false) JSONObject jsonObject,
                            @RequestParam(name = "type", required = false) String type,
                            @RequestParam(name = "id", required = false) String id,
                            @RequestParam(name = "br", required = false) Integer br) {
        if (jsonObject == null || jsonObject.getString("id").equals("")) {
            JSONObject parameter = new JSONObject();
            if (id == null || id.equals("")) {
                parameter.put("msg", "请传入视频的mvId：id");
                parameter.put("可选参数br", "br：码率，240，480，720，1080");
                return parameter;
            }
            parameter.put("id", id);
            parameter.put("br", br);//码率
            jsonObject = parameter;
        }
        JSONObject mvUrlJson = yumboMVService.mvUrl(type, "/mv/url", jsonObject);
        final JSONObject data = mvUrlJson.getJSONObject("data");
        final JSONObject result = new JSONObject();
        result.put("id", id);
        result.put("br", br);
        if (type.equals("qq")) {
            // 得到对应码率的url
            String url = data.getJSONArray(id).getString(MVBitRateEnum.getEnum(br).bitRate);
            result.put("url", url);
        } else {
            final String url = data.getString("url");
            result.putIfAbsent("br",data.getString("r"));
            result.put("url", url);
        }
        return result;
    }
}
