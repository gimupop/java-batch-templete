package jp.co.yanagawa.bachTemplete.common;

import static org.apache.commons.lang3.time.DateFormatUtils.*;
import static org.apache.commons.lang3.time.DurationFormatUtils.*;

import java.util.Date;

public class ExecuteTimeInfo {
    /** 開始時間 */
    private long start;
    /** 終了時間 */
    private long end;

    /**
     * コンストラクタ。
     * 
     * @param start 開始時間
     * @param end 終了時間
     */
    public ExecuteTimeInfo(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return format(new Date(start), "HH:mm:ss");
    }

    public String getEnd() {
        return format(new Date(end), "HH:mm:ss");
    }

    public long getTime() {
        return end - start;
    }

    public String getFormatTime() {
        return formatDuration(end - start, "HH:mm:ss");
    }

    public static String logHistory(String name, long start, long end, boolean success) {
        ExecuteTimeInfo time = new ExecuteTimeInfo(start, end);
        String historyLog =
                name
                        + " "
                        + time.getStart()
                        + " "
                        + time.getEnd()
                        + " "
                        + time.getFormatTime()
                        + " "
                        + (success ? "OK" : "NG");
        return historyLog;
    }
}