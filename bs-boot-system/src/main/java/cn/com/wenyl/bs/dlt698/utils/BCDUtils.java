package cn.com.wenyl.bs.dlt698.utils;

import java.util.ArrayList;
import java.util.List;

public class BCDUtils {
    // 编码方法
    public static byte[] encodeBCD(String serverAddress) {
        int length = serverAddress.length();
        int byteLength = (length + 1) / 2; // 计算需要的字节数
        byte[] encodedBytes = new byte[byteLength];

        for (int i = 0; i < length; i += 2) {
            // 获取当前字符
            char highNibbleChar = serverAddress.charAt(i);
            int highNibble = Character.getNumericValue(highNibbleChar);

            // 判断是否有下一个字符
            int lowNibble = 0xF; // 默认低4位为FH
            if (i + 1 < length) {
                char lowNibbleChar = serverAddress.charAt(i + 1);
                lowNibble = Character.getNumericValue(lowNibbleChar);
            }

            // 组合高4位和低4位
            encodedBytes[i / 2] = (byte) ((highNibble << 4) | lowNibble);
        }

        // 反转字节顺序（低字节在前，高字节在后）
        reverseByteArray(encodedBytes);

        return encodedBytes;
    }

    // 解码方法
    public static String decodeBCD(byte[] encodedBytes) {
        // 反转字节顺序（恢复原始顺序）
        reverseByteArray(encodedBytes);

        StringBuilder serverAddress = new StringBuilder();

        for (byte b : encodedBytes) {
            // 获取高4位和低4位
            int highNibble = (b & 0xF0) >>> 4;
            int lowNibble = b & 0x0F;

            // 添加高4位对应的数字
            serverAddress.append(highNibble);

            // 如果低4位不是FH，添加低4位对应的数字
            if (lowNibble != 0xF) {
                serverAddress.append(lowNibble);
            }
        }

        return serverAddress.toString();
    }

    // 反转字节数组
    private static void reverseByteArray(byte[] array) {
        int i = 0, j = array.length - 1;
        while (i < j) {
            byte temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            i++;
            j--;
        }
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
