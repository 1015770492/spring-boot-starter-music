package top.yumbo.music.starter.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import top.yumbo.music.starter.util.BiAsyncRequestUtils;

@RestController
@RequestMapping(value = "{yumbo.music.server.prefix}", method = {RequestMethod.GET, RequestMethod.POST})
public class UserController {

    @RequestMapping("/user/login/check")
    public JSONObject checkQrStatus(@RequestBody(required = false) JSONObject jsonObject,
                                    @RequestParam(name = "key", required = false) String key) {

        if (!"".equals(key)) {
            jsonObject.put("key",key);
        }

        final JSONObject result = BiAsyncRequestUtils.doInvoke("netease", "/login/qr/check", jsonObject);
        return result;
    }

    /**
     * @param username 用户名
     * @param password 密码
     * @param type     登录类型
     * @return 登录后的数据
     */
    @RequestMapping("/user/login")
    public JSONObject login(@RequestBody(required = false) JSONObject jsonObject,
                            @RequestParam(name = "username", required = false) String username,
                            @RequestParam(name = "password", required = false) String password,
                            @RequestParam(name = "type", required = false, defaultValue = "1") Integer type) {
        JSONObject parameter = new JSONObject();
        if (jsonObject == null || "".equals(jsonObject.getString("username")) || "".equals(jsonObject.getString("password"))) {
            if (username == null || username.equals("") || password == null || password.equals("")) {
                parameter.put("username", username);
                parameter.put("password", password);
                parameter.put("type", type);
                return parameter;// 不符合的数据直接返回
            }
            parameter.putIfAbsent("username", username);
            parameter.putIfAbsent("password", password);
        } else {
            parameter = jsonObject;
        }

        // qvAUEmG2ABtXeCk
        JSONObject result = null;
        parameter.put("password", parameter.getString("password"));// 密码参数名相同
        if (type == 1) {
            parameter.put("phone", parameter.getString("username"));
            result = BiAsyncRequestUtils.doInvoke("netease", "/login/cellphone", parameter);

        } else if (type == 2) {
            final JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("email", parameter.getString("username"));
            jsonObject1.put("password", password);
            result = BiAsyncRequestUtils.doInvoke("netease", "/login", jsonObject1);
        } else if (type == 3) {
            final JSONObject qrParameter = new JSONObject();
            result = BiAsyncRequestUtils.doInvoke("netease", "/login/qr/key", parameter);
            final String unikey = result.getJSONObject("data").getString("unikey");
            qrParameter.put("key", unikey);
            qrParameter.put("qrimg", "true");// 返回二维码的base64图片
            result = BiAsyncRequestUtils.doInvoke("netease", "/login/qr/create", qrParameter);
            result.getJSONObject("data").put("key", unikey);
        }

        return result;
    }
}
