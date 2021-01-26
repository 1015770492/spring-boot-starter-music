package top.yumbo.music.starter.entity.yumboEnum;


import java.util.HashMap;

public enum MVBitRateEnum {
    // 分别代表标清、高清、超清、蓝光
    STANDARD(240), HIGH(480), SUPER(720), BLUE_LIGHT(1080);
    public static HashMap<Integer, MVBitRateEnum> bitRateEnumHashMap = new HashMap<>();
    public int bitRate;// 码率

    static {
        bitRateEnumHashMap.put(240, STANDARD);
        bitRateEnumHashMap.put(480, HIGH);
        bitRateEnumHashMap.put(720, SUPER);
        bitRateEnumHashMap.put(1080, BLUE_LIGHT);
    }

    MVBitRateEnum(int bitRate) {
        this.bitRate = bitRate;
    }

    /**
     * 根据值获取枚举对象
     *
     * @param type 枚举的值
     * @return 对应的枚举
     */
    public static MVBitRateEnum getEnum(Integer type) {
        return bitRateEnumHashMap.get(type);
    }
}
