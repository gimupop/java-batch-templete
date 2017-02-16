package jp.co.yanagawa.bachTemplete;

import jp.co.yanagawa.bachTemplete.common.ExecuteTimeInfo;
import jp.co.yanagawa.bachTemplete.common.Constants;
import jp.co.yanagawa.bachTemplete.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.ArrayList;
import java.util.List;

import static jp.co.yanagawa.bachTemplete.util.FileUtil.getClassName;
import static jp.co.yanagawa.bachTemplete.util.FileUtil.getResourcePath;

public class Start {
    static {
        DOMConfigurator.configure(getResourcePath("log4j.xml"));
    }

    private static final Logger LOGGER = Logger.getLogger(Start.class);
    private static final Logger HISTORY = Logger.getLogger("history");
    private static StringBuilder report = new StringBuilder();
    private static String TASK_NAME_ALL = "all";
    private static String START = "Start";


    public static void main(String[] args) {

        if (args == null || args.length == 0 || StringUtils.isEmpty(StringUtils.trim(args[0]))) {
            LOGGER.info("引数を指定してください。");
            LOGGER.info("TASK_NAME TARGET_DATE");
            return;
        }
        String taskNameForAlert = "";
        String taskName="";
        long start = 0l;
        long end = 0l;
        try {
            taskName = args[0];
            List<Task> manualTaskList = execGmTask(taskName);
            if (manualTaskList == null || manualTaskList.isEmpty()) {
                return;
            }

            for (Task manualTask : manualTaskList) {
                start = System.currentTimeMillis();

                int result = manualTask.execute();
                taskNameForAlert = getClassName(manualTask.getClass().getName());

                end = System.currentTimeMillis();

                printHistory(result, taskNameForAlert, start, end);
            }
        } catch (Exception e) {
            end = System.currentTimeMillis();
            printHistory(Constants.ERROR_CODE, taskNameForAlert, start, end);
            LOGGER.info(e.getMessage(), e);
            LOGGER.fatal(taskName+"実行時にエラーが発生しました。下記メッセージを参考にログをご確認ください。\n"
                    +e.getMessage());
        } finally {
        }
    }

    /**
     * 実行タスクを決定
     *
     * @param taskName
     * @return
     * @throws Exception
     */
    private static List<Task> execGmTask(String taskName) throws Exception {
        List<Task> manualTaskList = new ArrayList<Task>();
        if (TASK_NAME_ALL.equals(taskName) || START.equals(taskName)) {
            manualTaskList.add(new jp.co.yanagawa.bachTemplete.task.bachTemplete());
        }

        return manualTaskList;
    }

    /**
     * ヒストリ
     *
     * @param result
     * @param taskNameForAlert
     * @param start
     * @param end
     */
    private static void printHistory(int result, String taskNameForAlert, long start, long end) {
        boolean success = false;
        if (result == 0) {
            success = true;
        }
        Start.HISTORY.info(ExecuteTimeInfo.logHistory(Start.class.getSimpleName()
                + " [" + taskNameForAlert + "]", start, end, success) + report.toString());
    }
}


