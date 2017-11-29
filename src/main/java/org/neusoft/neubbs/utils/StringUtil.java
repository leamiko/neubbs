package org.neusoft.neubbs.utils;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.controller.exception.TokenErrorException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

/**
 * 字符串 工具类
 *
 * @author Suvan
 */
public final class StringUtil {

    private StringUtil() { }

    /**
     * 空判断（true - 为空）
     *
     * @param str 输入字符串
     * @return boolean 检测结果
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 长度范围判断（ min <= str.length() <= max）
     *
     * @param str 输入字符串
     * @param min 最小值
     * @param max 最大值
     * @return boolean 检测结果
     */
    public static boolean isScope(String str, int min, int max) {
        return (min <= str.length() && str.length() <= max);
    }

    /**
     * 是否过期（判断此时是否过期，true-过期）
     *
     * @param expireTime 过期指定时间（时间戳）
     * @return boolean 检测结果
     * @throws TokenErrorException 口令错误异常
     */
    public static boolean isExpire(String expireTime) throws TokenErrorException {
        long time;
        try {
            time = Long.parseLong(expireTime);
        } catch (NumberFormatException nfe) {
            throw new TokenErrorException(ApiMessage.IVALID_TOKEN).log(LogWarn.ACCOUNT_15);
        }

        return time <= System.currentTimeMillis();
    }

    /**
     * 生成邮件 HTML 字符串（构建，邮箱激活邮件内容）
     *
     * @param url 输入字符串
     * @return String 生成的HTML字符串
     */
    public static String createEmailActivationHtmlString(String url) {
        StringBuffer sb = new StringBuffer();
            sb.append("<html><head></head><body><h1>Neubbs 帐号活邮件，点击激活帐号</h1><br>");
            sb.append("<a href=\"" + url + "\">");
            sb.append(url);
            sb.append("</a></body></html>");

        return sb.toString();
    }

    /**
     * 获取当天 24 点 时间戳
     *
     * @return String 24点时间戳
     */
    public static String getTodayTwentyFourClockTimestamp() {
        Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, SetConst.TWENTY_FOUR);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);

        return String.valueOf(calendar.getTimeInMillis());
    }

    /**
     * 获取相隔时间（格式：xxx 天）
     *
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return String 相隔天数（今天 or xxx 天前）
     */
    public static String getSeparateDay(long startTime, long endTime) {
        int separateDay = (int) ((startTime - endTime) / SetConst.ONE_DAY_MS);
        return separateDay == 0 ? SetConst.TODAY : separateDay + SetConst.DAY_AGE;
    }

    /**
     * 拼接用户头像 FTP URL
     *
     * @param userInfoMap 用户信息键值对
     * @return String model-user-avator完整URL
     */
    public static String spliceUserAvatorImageFtpUrl(Map<String, Object> userInfoMap) {

        Resource resource = new ClassPathResource("/neubbs.properties");
        Properties props = null;
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder avatorFtpUrl = new StringBuilder("ftp://");
            avatorFtpUrl.append(props.getProperty("ftp.ip"));

        String imageFileName = (String) userInfoMap.get(ParamConst.IMAGE);
        if (imageFileName.contains(SetConst.DEFAULT)) {
            avatorFtpUrl.append(imageFileName);
        } else {
            avatorFtpUrl.append((Integer) userInfoMap.get(ParamConst.ID) + "-"
                    + (String) userInfoMap.get(ParamConst.NAME)
                    + "/avator/" + imageFileName
            );
        }

        return avatorFtpUrl.toString();
    }

    /**
     * 补全前后斜杠（\）
     *      - 用于补全 URL 路径
     *
     * @param str 输入字符串
     * @return String 补全结果
     */
    public static String completeBeforeAfterSprit(String str) {
        char firstChar = str.charAt(0);
        char lastChar = str.charAt(str.length() - 1);

        StringBuilder sb = new StringBuilder(str);
            sb.insert(0, firstChar == '/' ? "" : "/");
            sb.append(lastChar == '/' ? "" : "/");

        return sb.toString();
    }
}
