### 云博音乐文档
整体说明：api接受json和url拼串的参数

#### 关于源码
controller获取到参数并且将结果返回。
service层进行处理请求参数以及进行接口之间的调用，采用的是自定义线程池异步并发调用的方式，
异步的请求网易云音乐和qq音乐接口，接口的放回时间取决与其中一个请求的最长时间。

#### 关于配置
如果访问量增加，可以适当的调整线程池的配置，在yml或者properties文件中设置即可
```yml
yumbo:
  music:
    server:
      netease: http://yumbo.top:3000 #配置网易云音乐api的服务器地址
      qq: http://yumbo.top:3300 #配置qq音乐api的服务器地址
      port: 8888 # 配置服务端口
      prefix: api # 配置统一api访问前缀，例如:/search/music ==> /api/search/music
      limit: 30 # 配置默认返回的条数，各取30条，所以返回值是这个值的2倍
    task:
      pool:
        corePoolSize: 15     #设置核心线程数
        maxPoolSize: 40     #设置最大线程数
        keepAliveTime: 300  #设置线程活跃时间（秒）
        workQueueCapacity: 100 #设置工作队列容量
        workQueueFair: false  #设置工作队列是否为公平队列
```
### 全局返回的 数据说明：每一个对象都会带一个type表示是使用网易云还是qq音乐的api（netease是网易云、qq则是qq音乐），这个后台处理，会自动调用对应的ai
全局接收json和url拼串post和get都支持.
#### 搜索单曲
请求路径：`/search/music`
需要的参数：
keywords（必须传入）：搜索词
返回数据示例：
durationTime: 歌曲时长，单位秒
singer: 歌手数组，一般会将name进行拼串，用 `/` 隔开
mvId: 视频的id
mvName: 视频名称
```json
{
    "total": 13,
    "videos": [
        {
            "durationTime": 367,
            "singer": [
                {
                    "transNames": null,
                    "name": "老王乐队",
                    "alias": [
                        "Your Woman Sleep with Others"
                    ],
                    "id": 12676071
                }
            ],
            "mvId": "10865779",
            "mvName": "我还年轻 我还年轻",
            "type": "netease",
            "mvPictureUrl": "http://p1.music.126.net/GLk5pgTP3tMuPlhQsLhZ1Q==/109951164051434519.jpg"
        },
        {
            "durationTime": 296,
            "singer": [
                {
                    "name": "尹清",
                    "name_hilight": "尹清",
                    "mid": "002Hmq6o3j4z4T",
                    "id": 2172733
                },
                {
                    "name": "邱虹凯",
                    "name_hilight": "邱虹凯",
                    "mid": "001GOFoL0ZeaPo",
                    "id": 2172807
                }
            ],
            "mvId": "l0027g8dumh",
            "mvName": "我还年轻 我还年轻 (明日之子第二季 第5期)",
            "type": "qq",
            "mvPictureUrl": "http://y.gtimg.cn/music/photo_new/T015R640x360M101004EOnn44NA6W3.jpg"
        }
    ]
}
```

### 搜索视频mv
请求路径：`/search/mv`
需要的参数：
keywords（必须传入）：搜索词
返回的数据示例：
qq: 返回的videos数组中来自qq 1条
netease: 返回的videos数组来自网易云音乐一条
total: 本次搜索的所有视频有189条
return: 返回了多少条
```json
{
    "qq": 1,
    "total": 189,
    "netease": 1,
    "videos": [
        {
            "durationTime": 317,
            "singer": [
                {
                    "transNames": null,
                    "name": "Beyond",
                    "alias": [
                        "黄家驹",
                        "黄家强",
                        "黄贯中",
                        "叶世荣",
                        "Beyond乐队"
                    ],
                    "id": 11127
                }
            ],
            "mvId": "376199",
            "mvName": "海阔天空",
            "type": "netease",
            "mvPictureUrl": "http://p1.music.126.net/S8InCa4o-pFJszhUvI-NPQ==/3247957351196805.jpg"
        },
        {
            "durationTime": 319,
            "singer": [
                {
                    "name": "BEYOND",
                    "name_hilight": "BEYOND",
                    "mid": "002pUZT93gF4Cu",
                    "id": 2
                }
            ],
            "mvId": "u0010TNv4U6",
            "mvName": "海阔天空 (正式版)",
            "type": "qq",
            "mvPictureUrl": "http://y.gtimg.cn/music/photo_new/T015R640x360M1010024jbha2gPk27.jpg"
        }
    ],
    "return": 2
}
```