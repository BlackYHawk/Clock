package io.hawk.clock.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lan on 2016/4/22.
 */
public class StringUtil {
    public static String pattern = "yyyy-MM-dd HH:mm:ss";
    public static String pattern2 = "yyyy-MM-dd HH:mm";

    public static boolean isEmpty(String str) {
        return str == null || str.equalsIgnoreCase("");
    }

    public static boolean checkLen(String str, int len) {
        return str != null && str.length() == len;
    }

    public static int formatStr2Int(String str) {
        int num;
        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            num = -1;
        }

        return num;
    }

    public static int randomInt(int range) {
        Random rand = new Random();

        return rand.nextInt(range);        //int范围类的随机数
    }

    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }
    /**
     * 获得当前时间
     *
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());

        return dateFormat.format(date);
    }

    /**
     * 格式化时间（输出类似于有效期这样的时间）
     *
     * @param timestamp
     *            需要格式化的时间戳
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formartTime(String timestamp) {
        String str = "";

        if(StringUtil.isEmpty(timestamp)) {
            str = "";
        }
        else {
            try {
                long lTimestamp = Long.parseLong(timestamp);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

                str = dateFormat.format(new Date(lTimestamp * 1000L));
            } catch (NumberFormatException e) {
                str = "";
            }
        }

        return str;
    }

    /**
     * 格式化时间（输出类似于时间戳）
     *
     * @param dateStr
     *            需要格式化的时间戳
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static long formartTimestamp(String dateStr) {
        if(!dateStr.contains(":")) {
            dateStr += " 00:01";
        }

        long timestamp = 0;

        if(StringUtil.isEmpty(dateStr)) {
            timestamp = 0;
        }
        else {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Date date = dateFormat.parse(dateStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                timestamp = cal.getTimeInMillis()/1000;
            } catch (ParseException e) {

            }
        }

        return timestamp;
    }

    /**
     * 格式化时间（输出类似于时间戳）
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static long getCurrentTimestamp() {
        long timestamp = 0;
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        timestamp = cal.getTimeInMillis()/1000;

        return timestamp;
    }

    public static boolean compareTimeToCurrent(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date date = dateFormat.parse(dateStr);
            Date today = new Date();

            return date.after(today);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 字符串格式时间转成long
     */
    private static long Time2Long(String attachTime) {
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = myFormatter.parse(attachTime);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 租车时间差
     *
     * @param startTime
     * @param endTime
     * @return N分钟
     */
    public static int getBetweenDate(String startTime, String endTime) {
        long time1 = Time2Long(startTime);
        long time2 = Time2Long(endTime);
        int time = (int)(time2 - time1)/(1000*60);
        return time>0?time:0;
    }

    /**
     * 格式化时间（输出类似于今天、 昨天这样的时间）
     *
     * @param time
     *            需要格式化的时间 如"2014-07-14 19:01:45"
     * @param pattern
     *            输入参数time的时间格式 如:"yyyy-MM-dd HH:mm:ss"
     *            <p/>
     *            如果为空则默认使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatDisplayTime(String time, String pattern) {
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;

        if (time != null) {
            try {
                Date tDate = new SimpleDateFormat(pattern, Locale.getDefault()).parse(time);// 格式化输入的时间
                Date today = new Date();
                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy", Locale.getDefault());// 年
                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());// 年月日
                Date thisYear = new Date(thisYearDf.parse(
                        thisYearDf.format(today)).getTime());// 得到年
                Date yesterday = new Date(todayDf.parse(todayDf.format(today))
                        .getTime());// 得到年月日
                // Date beforeYes = new Date(yesterday.getTime() - tDay);
                if (tDate != null) {
                    // SimpleDateFormat halfDf = new SimpleDateFormat("MM月dd日");
                    long dTime = today.getTime() - tDate.getTime();// 当前时间和给定时间的时间差
                    if (tDate.before(thisYear)) {// 不是同一年
                        display = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault())
                                .format(tDate);
                    } else {// 是同一年
                        if (dTime < tDay && tDate.after(yesterday)) {// 判断给定时间是不是今天
                            display = "今天"
                                    + new SimpleDateFormat(" HH:mm", Locale.getDefault())
                                    .format(tDate);
                        } else {
                            display = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault())
                                    .format(tDate);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return display;
    }
    public static String formatSex(String value) {
        String text = "";

        if(StringUtil.isEmpty(value)) {
            return text;
        }

        if(value.equals("0")) {
            text = "男";
        }
        else {
            text = "女";
        }

        return text;
    }
    public static String formatSexText(String text) {
        String value = "";

        if(StringUtil.isEmpty(text)) {
            return "";
        }

        if(text.equals("男")) {
            value = "0";
        }
        else {
            value = "1";
        }

        return value;
    }

    public static String formatLockStatus(String status, String name) {
        if("0".equals(status)) {
            return name + "(已删除)";
        }
        else {
            return name;
        }
    }

    /**
     * 判断一个字符串中是否有如下的特殊符号
     */
    public static boolean containSymbol(String str){
//		String specialStr = "[^\\:\\!\"\\#\\$\\%\\&\\'\\(\\)\\*\\+\\,\\-\\.\\/\\:\\;\\<\\=\\>\\?\\@\\[\\\\\\]\\^\\_\\`\\{\\|\\}\\~]*";
        String specialStr = "[^\\:\"\\'\\(\\)\\*\\,\\/\\<\\=\\>\\?\\[\\\\\\]\\^\\`\\{\\|\\}\\~]*";
        Pattern pattern = Pattern.compile(specialStr);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    /**
     * 验证ip是否合法
     *
     * @param text
     *            ip地址
     * @return 验证信息
     */
    public static boolean checkIP(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            // 返回判断信息
            return text.matches(regex);
        }
        // 返回判断信息
        return false;
    }
    /**
     * 利用正则表达式判断手机号
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public static int formatDuration(int duration) {
        if(duration > 0) {
            if (duration < 1000) {
                return 1000;
            }
            else {
                return duration/1000;
            }
        }
        return 0;
    }
}
