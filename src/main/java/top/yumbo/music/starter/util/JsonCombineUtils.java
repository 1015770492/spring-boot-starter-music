package top.yumbo.music.starter.util;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonCombineUtils {
    public static JSONObject combineJsonList(int sum, String collectionName, List<JSONObject> neteaseJson, List<JSONObject> qqJson) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", sum);
        final int size1 = neteaseJson.size();
        final int size2 = qqJson.size();
        jsonObject.put("return", size1+size2);// 网易云音乐返回了多少条
        jsonObject.put("netease", size1);// 网易云音乐返回了多少条
        jsonObject.put("qq", size2);// qq返回了多少条数据
        if (size1 > size2) {
            List<JSONObject> temp = neteaseJson;
            neteaseJson = qqJson;
            qqJson = temp;
        }
        // 将小的集合jsonList1 加入到大的集合jsonList2 中
        for (int i = 0; i < neteaseJson.size(); i++) {
            qqJson.add(i * 2, neteaseJson.get(i));
        }
        jsonObject.put(collectionName, qqJson);
        return jsonObject;
    }
}
