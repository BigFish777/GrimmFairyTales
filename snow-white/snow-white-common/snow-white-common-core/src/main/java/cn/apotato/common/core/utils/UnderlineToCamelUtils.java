package cn.apotato.common.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 驼峰法-下划线互转
 *
 * @author cshaper
 * @date 2023/04/21
 */
public class UnderlineToCamelUtils {

    private static Pattern UNDERLINE_TO_HUMP_PATTERN = Pattern.compile("([A-Za-z\\d]+)(_)?");
    private static Pattern HUMP_TO_UNDERLINE_PATTERN = Pattern.compile("[A-Z]([a-z\\d]+)?");

    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underlineToCamel(String line, boolean smallCamel) {
        if (StringUtils.isBlank(line)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Matcher matcher = UNDERLINE_TO_HUMP_PATTERN.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camelToUnderline(String line) {
        if (StringUtils.isBlank(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuilder sb = new StringBuilder();
        Matcher matcher = HUMP_TO_UNDERLINE_PATTERN.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }
}
