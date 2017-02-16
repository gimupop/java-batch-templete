package jp.co.yanagawa.bachTemplete.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static org.apache.commons.lang3.time.DateFormatUtils.format;

public class DateUtil {
    public static final String FORMAT_YMD_HYPHEN = "yyyy-MM-dd";
    public static final String FORMAT_HMS_HYPHEN = "HH:mm:ss";

    /**
     * 昨日
     * 形式：YYYYMMDD
     *
     * @return
     */
    public static String getYesterday() {
        return formatShort(previousDate(getNow()));
    }

    /**
     * 指定の翌日
     *
     * @param date
     * @return
     */
    public static Date nextDate(Date date) {
        return calculate(date, Calendar.DAY_OF_MONTH, 1);
    }

    /**
     * 指定の前日
     *
     * @param date
     * @return
     */
    public static Date previousDate(Date date) {
        return calculate(date, Calendar.DAY_OF_MONTH, -1);
    }

    /**
     * [yyyy-MM-dd]形式の日付を取得
     *
     * @param str
     * @param format
     * @return
     * @throws Exception
     */
    public static String getDate(String str, String format) throws Exception {
        if (StringUtils.isEmpty(str) || str.length() < 10) {
            return StringUtils.EMPTY;
        }
        String date = str.substring(0, 10);
        if (checkDate(date, format) == false) {
            return StringUtils.EMPTY;
        }
        return date;
    }

    /**
     * [yyyy-MM-dd]形式の正しい日付なのかをチェック
     *
     * @param date
     * @param format
     * @return
     * @throws Exception
     */
    public static boolean checkDate(String date, String format) throws Exception {
        try {
            DateUtils.parseDateStrictly(date, new String[]{format});
        } catch (ParseException pe) {
            return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 指定日付を返す
     *
     * @param srcDate
     * @param field
     * @param amount
     * @return
     */
    public static Date calculate(Date srcDate, int field, int amount) {
        if (srcDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(srcDate);
        cal.add(field, amount);
        return cal.getTime();
    }

    /**
     * 指定した日数を足して返す
     *
     * @param amount
     * @return
     */
    public static Date addDate(int amount) {
        return calculate(getNow(), Calendar.DAY_OF_MONTH, amount);
    }

    /**
     * 指定した日数を足して返す
     *
     * @param amount
     * @return
     */
    public static String addDateStr(int amount) {
        return formatShort(addDate(amount));
    }

    /**
     * 現在時刻を返す
     *
     * @return Date
     */
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 現在時刻を返す
     *
     * @return String
     */
    public static String getTimestamp() {
        return format(getNow(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 現在時刻を返す
     *
     * @return String
     */
    public static String getSimpleTimestamp() {
        return format(getNow(), "yyyyMMdd-HHmmss");
    }

    /**
     * 現在時までを返す
     *
     * @return String
     */
    public static String getRetunrTimestamp() {
        return format(getNow(), "yyyyMMddHH");
    }

    /**
     * 指定日付を文字列で返す（日まで）
     *
     * @param date
     * @return
     */
    public static String formatShort(Date date) {
        return format(date, "yyyyMMdd");
    }

    /**
     * 指定日付を文字列で返す（分まで）
     *
     * @param date
     * @return
     */
    public static String formatShortWithHM(Date date) {
        return format(date, "yyyyMMddHHmm");
    }

    /**
     * 指定日付を文字列で返す（秒まで）
     *
     * @param date
     * @return
     */
    public static String formatShortWithHMS(Date date) {
        return format(date, "yyyyMMddHHmmss");
    }

    /**
     * 現在の日付のみを返す
     *
     * @return
     */
    public static String getOnlyCurrentDate() {
        return format(getNow(), FORMAT_YMD_HYPHEN);
    }

    /**
     * 現在の時刻のみを返す
     *
     * @return
     */
    public static String getOnlyCurrentTime() {
        return format(getNow(), FORMAT_HMS_HYPHEN);
    }
}