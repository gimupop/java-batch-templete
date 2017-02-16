package jp.co.yanagawa.bachTemplete.dao;

import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.*;
import org.apache.log4j.Logger;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class Dao {
    private static final Logger LOGGER = Logger.getLogger(Dao.class);

    /**
     * コネクションの取得(Mysql)
     *
     * @return
     */
    protected static Connection getMysqlConnection(String url, String user, String password, boolean autoCommit) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            con.setAutoCommit(autoCommit);
            return con;
        } catch (ClassNotFoundException e) {
            LOGGER.error("class not found: " + e.getMessage(), e);
            return null;
        } catch (SQLException e) {
            LOGGER.error("SQL Exception: " + e.getMessage(), e);
            return null;
        } catch (Exception e) {
            LOGGER.error("Exception: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * コネクションを閉じる
     *
     * @param conn
     */
    protected static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            LOGGER.error("Exception: " + e.getMessage());
        }
    }

    /**
     * PreparedStatementを例外を投げずに閉じる。 例外はログにwarnレベルで記録
     *
     * @param pst
     * @return
     */
    protected static PreparedStatement cleanupQuietly(PreparedStatement pst) {
        try {
            return cleanup(pst);
        } catch (SQLException e) {
            LOGGER.warn("can not close prepared statement. but, ignore this because in silent mode" + pst, e);
            return pst;
        }
    }

    /**
     * PreparedStatementを閉じる。 閉じた場合はnullを返却
     *
     * @param pst
     * @return
     * @throws SQLException
     */
    protected static PreparedStatement cleanup(PreparedStatement pst) throws SQLException {
        if (pst != null) {
            pst.close();
            pst = null;
        }
        return null;
    }

    /**
     * ResultSetを例外を投げずに閉じる
     *
     * @param rs
     * @return
     */
    protected static ResultSet cleanupQuietly(ResultSet rs) {
        try {
            return cleanup(rs);
        } catch (SQLException e) {
            LOGGER.warn("can not close resultset. but, ignore this because in silent mode" + rs, e);
            return rs;
        }
    }

    /**
     * ResultSetを閉じる。 閉じた場合はnullを返却。
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    protected static ResultSet cleanup(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
            rs = null;
        }
        return null;
    }

    /**
     * テーブルを削除
     *
     * @param con
     * @param tableName
     * @throws SQLException
     */
    protected static void dropTable(Connection con, String tableName) throws SQLException {
        dropTable(con, tableName, false);
    }

    /**
     * テーブルを削除
     *
     * @param con
     * @param tableName
     * @param ifExists  テーブルが有る場合にのみ削除するか？
     * @throws SQLException
     */
    protected static void dropTable(Connection con, String tableName, boolean ifExists) throws SQLException {
        String dropSql = "DROP TABLE " + (ifExists ? "IF EXISTS" : "") + " " + tableName;
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(dropSql);
            pst.execute();
            cleanup(pst);
        } finally {
            cleanupQuietly(pst);
        }
    }

    /**
     * １件を取得するSELECTクエリを実行します。
     *
     * @param con
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static Object[] queryForSinglRow(Connection con, String sql, Object... params) throws SQLException {
        LOGGER.debug(logSQL(sql, params));
        return new QueryRunner().query(con, sql, new ArrayHandler(), params);
    }

    /**
     * １件を取得するSELECTクエリを実行します。 COUNT や MAX などの関数実行の結果取得に使用
     *
     * @param con
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static Long queryForFuncResult(Connection con, String sql, Object... params) throws SQLException {
        LOGGER.debug(logSQL(sql, params));
        return new QueryRunner().query(con, sql, new ScalarHandler<Long>(), params);
    }

    /**
     * １件を取得するSELECTクエリを実行します。 指定したentity型のリストを取得
     * entityのプロパティ名はキャメルケースで作成してください。
     *
     * @param <T>
     * @param con
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static <T> T queryForSinglRowCamelCase(Connection con, String sql, Class<T> entity, Object... params)
            throws SQLException {
        LOGGER.debug(logSQL(sql, params));
        ByteToStringProcessorCamelCase processor = new ByteToStringProcessorCamelCase();
        return new QueryRunner().query(con, sql, new BeanHandler<T>(entity, processor), params);
    }

    /**
     * SELECTクエリを実行します。 指定したentity型のリストを取得 entityのプロパティ名はキャメルケースで作成してください。
     *
     * @param <T>
     * @param con
     * @param sql
     * @param entity
     * @param params
     * @return
     * @throws SQLException
     */
    protected static <T> List<T> queryForListCamelCase(Connection con, String sql, Class<T> entity, Object... params)
            throws SQLException {
        LOGGER.debug(logSQL(sql, params));
        ByteToStringProcessorCamelCase processor = new ByteToStringProcessorCamelCase();
        List<T> list = (List<T>) new QueryRunner().query(con, sql, new BeanListHandler<T>(entity, processor), params);
        return list;
    }

    /**
     * INSERT、UPDATEまたはDELETEクエリのバッチを実行します。
     *
     * @param con
     * @param sql
     * @param params
     * @return 更新件数
     * @throws SQLException
     */
    protected static int[] batch(Connection con, String sql, Object[][] params) throws SQLException {
        LOGGER.debug("\n" + sql + "\n" + toString(params));
        return new QueryRunner().batch(con, sql, params);
    }

    /**
     * UPDATEクエリを実行します。
     *
     * @param con
     * @param sql
     * @param params
     * @return 更新件数
     * @throws SQLException
     */
    protected static int update(Connection con, String sql, Object... params) throws SQLException {
        LOGGER.debug("\n" + sql + "\n" + Arrays.toString(params));
        return new QueryRunner().update(con, sql, params);
    }

    /**
     * パラメータの文字列表現を取得します。
     *
     * @param params パラメータ
     * @return パラメータの文字列表現
     */
    private static String toString(Object[][] params) {
        if (params == null || params.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            Object[] objects = params[i];
            builder.append(Arrays.toString(objects));
            if (i != params.length - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }


    public static void query(Connection con, String sql, Object... params) throws SQLException {
        LOGGER.debug(logSQL(sql, params));
        PreparedStatement pst = con.prepareStatement(sql);
        pst.execute();
        cleanup(pst);
        cleanupQuietly(pst);
    }

    /**
     * ログ用のSQLとパラメータ文字列を取得します。
     *
     * @param sql    SQL
     * @param params パラメータ
     * @return ログ用のSQLとパラメータ文字列
     */
    private static String logSQL(String sql, Object... params) {
        return "\n" + sql + " [" + Arrays.toString(params) + "]";
    }

    /**
     * エンティティのプロパティ名がキャメルケースのものに対応した ByteToStringProcessorです。
     *
     * @author yano_akihiro_gn
     */
    private static class ByteToStringProcessorCamelCase extends BeanProcessor implements RowProcessor {
        private static final String BLOB = "BLOB";

        @Override
        protected int[] mapColumnsToProperties(ResultSetMetaData rsmd, PropertyDescriptor[] props) throws SQLException {
            int cols = rsmd.getColumnCount();
            int columnToProperty[] = new int[cols + 1];
            Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);
            for (int col = 1; col <= cols; col++) {
                String columnName = rsmd.getColumnLabel(col);
                if (null == columnName || 0 == columnName.length()) {
                    columnName = rsmd.getColumnName(col);
                }
                for (int i = 0; i < props.length; i++) {
                    if (equalsColumnProperty(columnName, props[i].getName())) {
                        columnToProperty[col] = i;
                        break;
                    }
                }
            }
            return columnToProperty;
        }

        private boolean equalsColumnProperty(String colName, String propName) {
            return colName.replaceAll("_", "").equalsIgnoreCase(propName.replaceAll("_", ""));
        }

        @Override
        protected Object processColumn(ResultSet rs, int index, Class<?> propType) throws SQLException {
            if (!propType.isPrimitive() && rs.getObject(index) == null) {
                return null;
            }
            if (!propType.isPrimitive() && rs.getObject(index) == null) {
                return null;
            }
            if (propType.equals(String.class)) {
                if (rs.getMetaData().getColumnTypeName(index).indexOf(BLOB) > -1) {
                    Blob blob = rs.getBlob(index);
                    byte[] value = blob.getBytes(1L, (int) blob.length());
                    return valueOf(value);
                }
                return rs.getString(index);
            } else if (propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
                return (rs.getInt(index));
            } else if (propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
                return (rs.getBoolean(index));
            } else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
                return (rs.getLong(index));
            } else if (propType.equals(Double.TYPE) || propType.equals(Double.class)) {
                return (rs.getDouble(index));
            } else if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
                return (rs.getFloat(index));
            } else if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
                return (rs.getShort(index));
            } else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
                return rs.getBytes(index);
            } else if (propType.equals(Timestamp.class)) {
                return rs.getTimestamp(index);
            } else if (propType.equals(BigDecimal.class)) {
                return rs.getBigDecimal(index);
            } else {
                return rs.getObject(index);
            }
        }

        public Object[] toArray(ResultSet resultset) throws SQLException {
            return new BasicRowProcessor().toArray(resultset);
        }

        public Map<String, Object> toMap(ResultSet resultset) throws SQLException {
            return new BasicRowProcessor().toMap(resultset);
        }

        /**
         * byte[]から文字列に変換する。
         *
         * @param value
         * @return String
         */
        public String valueOf(byte[] value) {
            if (null == value) {
                return "";
            }
            try {
                return new String(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }

    }

    /**
     * Map
     *
     * @param con
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    protected static List<Map<String, Object>> queryForMap(Connection con, String sql, Object... params)
            throws SQLException {
        LOGGER.debug(logSQL(sql, params));
        ResultSetHandler<List<Map<String, Object>>> rs = new MapListHandler();
        return new QueryRunner().query(con, sql, rs, params);
    }



}