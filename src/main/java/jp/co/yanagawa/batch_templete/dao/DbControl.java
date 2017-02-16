package jp.co.yanagawa.bachTemplete.dao;

import jp.co.yanagawa.bachTemplete.common.Config;
import org.apache.log4j.Logger;

import java.sql.*;

public class DbControl extends Dao {
    private static final Logger LOGGER = Logger.getLogger(DbControl.class);

    /**
     * Mysqlのコネクションを取得します。
     */
    public static Connection getMyCoreMasterDbConnection() throws SQLException {
        String user = Config.getProperty("");
        String password = Config.getProperty("");
        String jdbcUrl = Config.getProperty("");
        Connection conn = getMysqlConnection(jdbcUrl, user, password, true);
        LOGGER.debug("◆DBMS URL◆" + getUrlOfDbms(conn));
        return conn;
    }

    /**
     * Connectionを切断
     *
     * @param conn
     * @param stmt
     * @throws Exception
     */
    public static void closeConnection(Connection conn, Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
        closeConnection(conn);
    }

    /**
     * Connectionを切断
     *
     * @param conn
     * @param stmt
     * @throws Exception
     */
    public static void closeConnection(Connection conn, PreparedStatement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
        closeConnection(conn);
    }

    /**
     * SQLを実行
     *
     * @param stmt
     * @throws Exception
     */
    public static int execUpdate(PreparedStatement stmt) throws SQLException {
        return stmt.executeUpdate();
    }

    /**
     * SQLを実行
     *
     * @param execSql
     * @param stmt
     * @throws Exception
     */
    public static int execBatch(String execSql, Statement stmt) throws SQLException {
        return stmt.executeUpdate(execSql);
    }

    /**
     * SQLを実行
     *
     * @param execSql
     * @param stmt
     * @throws Exception
     */
    public static int execBatch(String execSql, PreparedStatement stmt) throws SQLException {
        return stmt.executeUpdate(execSql);
    }

    /**
     * SQLを実行
     *
     * @param stmt
     * @throws Exception
     */
    public static void execBatch(PreparedStatement stmt) throws SQLException {
        stmt.executeBatch();
    }

    /**
     * 文字列をsinglequoteで囲む
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String wrappingStr(String str) throws Exception {
        return "\'" + str + "\'";
    }

    /**
     * ConnectionのURLを返す
     *
     * @param conn
     * @return
     * @throws Exception
     */
    public static String getUrlOfDbms(Connection conn) throws SQLException {
        if (conn == null) {
            return "This connection is null.";
        }
        DatabaseMetaData dmd = conn.getMetaData();
        return dmd.getURL();
    }
}