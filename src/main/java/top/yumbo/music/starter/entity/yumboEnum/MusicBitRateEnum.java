package top.yumbo.music.starter.entity.yumboEnum;

import java.util.HashMap;

public enum MusicBitRateEnum {
    // 分别代表标准音质、标准音质、高品质、 flac和ape都是无损，但是网页不支持ape格式，这里只是放在这里实际上用不到
    STANDARD("128"), M4A("m4a"), HIGH("320"), SUPER("flac"), APE("ape");
    public static HashMap<String, MusicBitRateEnum> qqBitRateEnumHashMap = new HashMap<>();
    public static HashMap<MusicBitRateEnum, String> neteaseBitRateEnumHashMap = new HashMap<>();
    public String bitRate;// 码率

    static {
        qqBitRateEnumHashMap.put("128k", STANDARD);
        qqBitRateEnumHashMap.put("192k", M4A);
        qqBitRateEnumHashMap.put("256k", HIGH);
        qqBitRateEnumHashMap.put("320k", SUPER);
        qqBitRateEnumHashMap.put("480k", APE);

        neteaseBitRateEnumHashMap.put(STANDARD, "128k");
        neteaseBitRateEnumHashMap.put(M4A, "192k");
        neteaseBitRateEnumHashMap.put(HIGH, "256k");
        neteaseBitRateEnumHashMap.put(SUPER, "320k");
        neteaseBitRateEnumHashMap.put(APE, "480k");

    }

    MusicBitRateEnum(String bitRate) {
        this.bitRate = bitRate;
    }

    /**
     * 根据值获取枚举对象
     *
     * @param bitRate 枚举的值
     * @return 对应的枚举
     */
    public static MusicBitRateEnum getEnum(String bitRate) {
        return qqBitRateEnumHashMap.get(bitRate);
    }
}
