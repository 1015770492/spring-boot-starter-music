package top.yumbo.music.starter.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.yumbo.util.music.annotation.MusicService;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;
import top.yumbo.util.music.musicImpl.qq.QQMusicInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * 联合两个异步任务请求工具类
 */
public class BiAsyncRequestUtils {

    // 两个请求需要进行异步处理，提供效率
    private static ExecutorService executorService = YumboThreadExecutorServiceUtils.threadPoolExecutor;
    /**
     * 得到反射类
     */
    final static Class<NeteaseCloudMusicInfo> neteaseCloudMusicInfoClass = NeteaseCloudMusicInfo.class;
    final static Class<QQMusicInfo> qqMusicInfoClass = QQMusicInfo.class;
    /**
     * 反射获取到实例对象
     */
    final static Object ncmInstance = getNCMInstance(neteaseCloudMusicInfoClass); // 反射获取网易云音乐实例
    final static Object qqmInstance = getQQMInstance(qqMusicInfoClass);// 反射获取qq音乐实例

    /**
     * 将所有Method都存起来，方便二次利用，不需要重新获取
     */
    // 保存网易云音乐所有加了MusicService注解的方法（这些方法就是api）
    final static HashMap<String, Method> NCM_Api_MapMethod = new HashMap<String, Method>() {
        {
            saveMethod(this, neteaseCloudMusicInfoClass.getMethods());// 保存所有加了注解的方法
        }
    };
    // 保存QQ音乐所有加了MusicService注解的方法（这些方法就是api）
    final static HashMap<String, Method> QQ_Api_MapMethodHashMap = new HashMap<String, Method>() {
        {
            saveMethod(this, qqMusicInfoClass.getMethods());
        }
    };
    /**
     * 策略模式，根据字符串得到对应的Api的Map
     */
    final static HashMap<String, HashMap<String, Method>> reflectClassMap = new HashMap<String, HashMap<String, Method>>() {
        {
            put("netease", NCM_Api_MapMethod);// 目的是把所有api通过这个map获取到所有反射的方法，有策略模式的味道
            put("qq", QQ_Api_MapMethodHashMap);
        }
    };


    /**
     * 将请求路径和对应的方法存入到hashMap中
     *
     * @param Api_MapMethod 存储反射得到的所有加了@MusciService注解的 Method对象
     * @param methods       反射获取到的Method对象数组
     */
    public static void saveMethod(HashMap<String, Method> Api_MapMethod, Method[] methods) {
        for (Method method : methods) {
            final MusicService annotation = method.getAnnotation(MusicService.class);// 看下有没有这个注解
            if (annotation != null) {
                final String url = annotation.url();
                if (url != null) { // 注解不为空，且url不为null将url以及对应的method存入hashMap中
                    Api_MapMethod.put(url, method);
                }
            }
        }
    }


    /**
     * 进行异步组合发送请求
     *
     * @param neteaseRelativeUrl    网易云音乐请求的相对路径
     * @param neteaseMusicParameter 网易云音乐api需要的json参数
     * @param qqMusicRelativeUrl    qq音乐请求的相对路径
     * @param qqMusicParameter      qq音乐请求需要的json参数
     * @return {"netease":网易云音乐的数据,"qq":qq音乐的数据}    netease表示来自网易云api、qq表示来自qq音乐
     */
    public static JSONObject invokeMethod(String neteaseRelativeUrl, JSONObject neteaseMusicParameter, String qqMusicRelativeUrl, JSONObject qqMusicParameter) {

        // 异步调用网易云音乐api，与后面的qq音乐进行异步组合任务
        CompletableFuture<JSONObject> neteaseCloudMusicFuture = CompletableFuture.supplyAsync(
                () -> doInvoke("netease", ncmInstance, neteaseRelativeUrl, neteaseMusicParameter)// 通过策略模式调用对应的方法传入对应的参数，将返回的json数据返回
                , executorService);
        // 异步调用qq音乐api
        CompletableFuture<JSONObject> qqMusicFuture = CompletableFuture.supplyAsync(
                () -> doInvoke("qq", qqmInstance, qqMusicRelativeUrl, qqMusicParameter)
                , executorService);

        final CompletableFuture<JSONObject> thenCombineAsync = neteaseCloudMusicFuture.thenCombineAsync(qqMusicFuture, (res1, res2) -> {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("netease", res1);// 将网易云的数据添加到这个json对象
            jsonObject.put("qq", res2);// 将qq的数据加入到这个json对象
            return jsonObject;// 将封装好的json以一个jsonObject方法返回
        }, executorService);

        return getJsonArray(thenCombineAsync);// 返回处理后的数据
    }

    /**
     * 阻塞式的方式获取json数据
     *
     * @param completableFuture 封装获取json的代码，简化主业务代码
     * @return 返回json数组，因为有两套api，我将两个json外面套一层形成数组，在service层解开，并处理对应json进行合并
     */
    private static JSONObject getJsonArray(CompletableFuture<JSONObject> completableFuture) {
        JSONObject jsonObject = null;
        try {
            jsonObject = completableFuture.get();// 将处理好的json数据返回
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * @param serverProvider 值为：netease 或者 qq 表示调用的是哪一个api
     * @param relativeUrl    请求的相对路径
     * @param parameter      请求需要带的参数
     * @return json类型的结果
     */
    private static JSONObject doInvoke(String serverProvider, Object instance, String relativeUrl, JSONObject parameter) {
        JSONObject result = null;
        try {
            result = (JSONObject) reflectClassMap.get(serverProvider).get(relativeUrl).invoke(instance, parameter);// 网易云音乐的部分直接使用;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 反射方式获取到实例
     *
     * @param obj         反射类
     * @param constructor 构造方法
     * @return 反射得到的实例对象
     */
    private static Object getInstance(Object obj, Constructor<?> constructor) {
        if (constructor.getParameterCount() == 0) { // 找到无参的那个构造方法
            try {
                obj = constructor.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    /**
     * 得到网易云音乐实例对象
     * 反射的方式获取到网易云音乐api的工具类，目的是为了下一步通过反射调用方法
     *
     * @param neteaseCloudMusicInfoClass 网易云音乐api工具类
     * @return 反射得到的实例对象
     */
    private static Object getNCMInstance(Class<NeteaseCloudMusicInfo> neteaseCloudMusicInfoClass) {
        Object ncm = null;// 反射得到的网易云音乐实例
        for (Constructor<?> constructor : neteaseCloudMusicInfoClass.getDeclaredConstructors()) {
            ncm = getInstance(ncm, constructor);
        }
        return ncm;
    }

    /**
     * 得到qq音乐实例对象
     * 反射的方式获取到qq音乐api工具类，目的是为了下一步通过反射调用方法
     *
     * @param qqMusicInfoClass qq音乐api工具类
     * @return 反射得到的实例对象
     */
    private static Object getQQMInstance(Class<QQMusicInfo> qqMusicInfoClass) {
        Object qqm = null;// 反射得到的qq音乐实例
        for (Constructor<?> constructor : qqMusicInfoClass.getDeclaredConstructors()) {
            qqm = getInstance(qqm, constructor);
        }
        return qqm;
    }


}
