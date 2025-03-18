package cn.com.wenyl.bs.dlt698.utils;

import java.util.ArrayList;
import java.util.List;

public class BCDUtils {
    /**
     * 将十进制字符串转换为压缩BCD码的byte数组。
     * 规则：每个半字节存储一个十进制数字；
     * 若数字位数为奇数，则最后一个字节的低4位用0xF填充。
     *
     * @param decimalStr 只包含数字0-9的十进制字符串
     * @return 压缩BCD码表示的byte数组
     * @throws IllegalArgumentException 当输入为null或包含非数字字符时抛出异常
     */
    public static byte[] encodeBCD(String decimalStr) {
        if (decimalStr == null || decimalStr.isEmpty()) {
            return new byte[0];
        }
        if (!decimalStr.matches("\\d+")) {
            throw new IllegalArgumentException("输入必须只包含数字0-9");
        }

        int len = decimalStr.length();
        // 如果位数为奇数，byte数组长度为 (len+1)/2
        int byteLen = (len + 1) / 2;
        byte[] bcd = new byte[byteLen];
        int charIndex = 0;
        for (int i = 0; i < byteLen; i++) {
            // 获取当前位数字，转换为对应的数值（0-9）
            int highNibble = decimalStr.charAt(charIndex) - '0';
            charIndex++;
            int lowNibble;
            if (charIndex < len) {
                lowNibble = decimalStr.charAt(charIndex) - '0';
                charIndex++;
            } else {
                // 若为奇数位，则最后一位的低4位填充为0xF
                lowNibble = 0xF;
            }
            // 将两个半字节合并成一个byte
            bcd[i] = (byte) ((highNibble << 4) | (lowNibble & 0x0F));
        }
        return bcd;
    }

    /**
     * 将压缩BCD码的byte数组转换为十进制字符串。
     * 规则：若最后一个字节的低4位为0xF，则视为填充，不转换为数字。
     *
     * @param bcdArray 压缩BCD码的byte数组
     * @return 对应的十进制字符串
     */
    public static String decodeBCD(byte[] bcdArray) {
        if (bcdArray == null || bcdArray.length == 0) {
            return "";
        }
        StringBuilder decoded = new StringBuilder();
        int len = bcdArray.length;
        for (int i = 0; i < len; i++) {
            byte b = bcdArray[i];
            // 取高4位
            int highNibble = (b >> 4) & 0x0F;
            // 取低4位
            int lowNibble = b & 0x0F;
            decoded.append(highNibble);
            // 对于最后一个字节，若低4位为0xF，则不添加（视为填充）
            if (i == len - 1 && lowNibble == 0xF) {
                // 不做任何处理
            } else {
                decoded.append(lowNibble);
            }
        }
        return decoded.toString();
    }

    /**
     * 将由 decodeBCD 得到的字符串还原为原始的压缩BCD码字节数组。
     * 本函数假设 decodeBCD 输出的数字中，原本的无效BCD数字（大于9，比如0xA）
     * 被转换为字符串 "10"，故在还原时，将 "10" 解析为 nibble 值 10。
     *
     * 如果字符串中出现其他多位数字，可能会导致解析歧义。
     *
     * @param decoded 由 decodeBCD 得到的字符串，例如 "101010101010101010101010"
     * @return 还原后的压缩BCD码字节数组
     * @throws IllegalArgumentException 当字符串中包含非数字字符时抛出异常
     */
    public static byte[] recoverOriginalBCD(String decoded) {
        if (decoded == null || decoded.isEmpty()) {
            return new byte[0];
        }

        // 使用列表保存逐个解析出的 nibble 数值
        List<Integer> nibbleList = new ArrayList<>();
        int i = 0;
        while (i < decoded.length()) {
            // 如果后面至少有2个字符且这2个字符正好为 "10"，则认为这是一个 nibble 值 10
            if (i + 1 < decoded.length()) {
                String twoChar = decoded.substring(i, i + 2);
                if (twoChar.equals("10")) {
                    nibbleList.add(10);
                    i += 2;
                    continue;
                }
            }
            // 否则取单个字符作为 nibble 值（0-9）
            char ch = decoded.charAt(i);
            if (!Character.isDigit(ch)) {
                throw new IllegalArgumentException("无效字符: " + ch);
            }
            nibbleList.add(ch - '0');
            i++;
        }

        // 将 nibble 数组合并为 byte 数组，每两个 nibble 组成一个 byte
        int nibbleCount = nibbleList.size();
        int byteCount = (nibbleCount + 1) / 2;
        byte[] result = new byte[byteCount];
        int nibbleIndex = 0;
        for (int j = 0; j < byteCount; j++) {
            int high = nibbleList.get(nibbleIndex++);
            int low;
            if (nibbleIndex < nibbleCount) {
                low = nibbleList.get(nibbleIndex++);
            } else {
                // 如果 nibble 数量为奇数，则最后一个 nibble 的低4位填充 0xF
                low = 0xF;
            }
            result[j] = (byte)((high << 4) | (low & 0x0F));
        }
        return result;
    }

}
