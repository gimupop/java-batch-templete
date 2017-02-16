package jp.co.yanagawa.bachTemplete.testUtil;


import jp.co.yanagawa.bachTemplete.common.Config;
import jp.co.yanagawa.bachTemplete.dao.DbControl;
import jp.co.yanagawa.bachTemplete.testUtil.matcher.DateCloseTo;
import org.apache.log4j.Logger;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.assertion.DiffCollectingFailureHandler;
import org.dbunit.assertion.Difference;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.SortedTable;
import org.hamcrest.core.Is;
import org.junit.Assert;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @author narahashi_toru
 */
public class DbTestUtil extends DbControl {
    private static final Logger LOGGER = Logger.getLogger(DbTestUtil.class);

    /**
     * PureDataのgross_margin_dbコネクションを取得します。
     */
    public static Connection getMyMasterDbConnection() throws SQLException {
        String user = Config.getProperty("mysql.user");
        String password = Config.getProperty("mysql.password");
        String jdbcUrl = Config.getProperty("mysql.jdbc.url.blade_bt_db");
        //core_master_db
        jdbcUrl = jdbcUrl.replaceAll("\\/[a-zA-Z_]+$", "/core_master_db");
        Connection conn = getMysqlConnection(jdbcUrl, user, password, true);
        LOGGER.info("◆DBMS URL◆" + getUrlOfDbms(conn));
        return conn;
    }

    private static Connection myConn = null;
    private static IDatabaseConnection myDbConn = null;

    private static Connection myCoreConn = null;
    private static IDatabaseConnection myCoreDbConn = null;

    private static Connection pdConn = null;
    private static IDatabaseConnection pdDbConn = null;

    public static IDatabaseConnection getMyConnectionCore() throws SQLException, DatabaseUnitException {
        if (myCoreDbConn == null) {
            myCoreConn = getMyCoreMasterDbConnection();
            myCoreDbConn = new DatabaseConnection(myCoreConn);
        }
        return myCoreDbConn;
    }

    public static void closeMyConnection() {
        try {
            if (myDbConn != null) {
                myDbConn.close();
                myDbConn = null;
            }
            if (myConn != null) {
                myConn.close();
                myConn = null;
            }
            if (myCoreConn != null) {
                myCoreDbConn.close();
                myCoreDbConn = null;
            }
            if (myCoreConn != null) {
                myCoreConn.close();
                myCoreConn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closePdConnection() {
        try {
            if (pdDbConn != null) {
                pdDbConn.close();
                pdDbConn = null;
            }
            if (pdConn != null) {
                pdConn.close();
                pdConn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static IDataSet getMyBladeActualDataCore() {
        try {
            return getMyConnectionCore().createDataSet();
        } catch (SQLException | DatabaseUnitException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IDataSet replacementDataSet(IDataSet ds) {
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(ds);
        replacementDataSet.addReplacementObject("[null]", null);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        replacementDataSet.addReplacementObject("[now]", now);
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
        new java.sql.Date(System.currentTimeMillis());
        try{
            replacementDataSet.addReplacementObject("[today]", dateFmt.parse(dateFmt.format(now)));
        }catch (Exception e){}
        return replacementDataSet;
    }
    
    public static void toleanceDateAssert(ITable expected, ITable actual, String[] toleanceDateCols, long toleanceSecond) throws DatabaseUnitException {
        DiffCollectingFailureHandler myHandler = new DiffCollectingFailureHandler();
        Assertion.assertEquals(new SortedTable(expected), new SortedTable(actual), myHandler);
        @SuppressWarnings("unchecked")
        List<Difference> diffList = myHandler.getDiffList();
        List<String> checkCols = Arrays.asList(toLowwerCases(toleanceDateCols));
        for (Difference diff : diffList) {
            String colName = diff.getColumnName();
            if (checkCols.contains(colName.toLowerCase())) {
                Assert.assertThat(colName, (Date)diff.getExpectedValue(), Is.is(DateCloseTo.closeTo((Date)diff.getActualValue(), 60)));
            } else {
                Assert.assertThat(colName, diff.getExpectedValue(), Is.is(diff.getActualValue()));
            }
        }
    }
    
    private static String[] toLowwerCases(String[] strings) {
        String[] lowwerStrings = new String[strings.length];
        int i = 0;
        for (String string : strings) {
            lowwerStrings[i++] = string.toLowerCase();
        }
        return lowwerStrings;
    }

}

