package jp.co.yanagawa.bachTemplete.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;

public class FileUtil {
    private static final Logger LOGGER = Logger.getLogger(FileUtil.class);

    /**
     * クラス名を返す
     *
     * @param fullPathClassName
     * @return
     * @throws Exception
     */
    public static String getClassName(String fullPathClassName) throws Exception {
        int startIdx = fullPathClassName.lastIndexOf(".") + 1;
        int endIdx = fullPathClassName.length();
        String className = StringUtils.substring(fullPathClassName, startIdx, endIdx);
        return className;
    }

    /**
     * 指定パス内に指定文字列で終わるファイル名の配列を返却
     *
     * @param String path
     * @param String partName
     * @return String[] フィルタリング
     */
    public static String[] fileFilterEndsWith(String path, String partName) {
        FilenameFilter completeFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.endsWith(partName)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        File filePath = new File(path);
        String[] files = filePath.list(completeFilter);

        return files;

    }

    /**
     * リソースファイルのパスを返却
     *
     * @param String fileName
     * @return String 指定ファイルのファイルのパス
     */
    public static String getResourcePath(String fileName) {
        return FileUtil.class.getClassLoader().getResource(fileName).getPath();
    }

    /**
     * @param filePathFrom
     * @param filePathTo
     */
    public static boolean fileMove(String filePathFrom, String filePathTo) {

        File from = new File(filePathFrom);
        File to = new File(filePathTo);
        if (!from.renameTo(to)) {
            LOGGER.info("ファイル退避失敗");
            LOGGER.fatal("ファイル退避失敗");
            return false;
        }
        return true;
    }

}