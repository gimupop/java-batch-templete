package jp.co.yanagawa.bachTemplete.testUtil;


import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.xml.sax.InputSource;
import java.sql.Connection;
import java.sql.SQLException;

import static jp.co.yanagawa.bachTemplete.dao.DbControl.getMyCoreMasterDbConnection;

/**
 * Created by yanagawa_keita on 5/19/16.
 */
public class DbSetup {

    @SuppressWarnings("Duplicates")
    public static void setUpMyMasterSql(String xmlFilePath) {
        try (Connection conn = getMyCoreMasterDbConnection()) {
            IDatabaseConnection dbconn = new DatabaseConnection(conn);
            FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            IDataSet ds = builder.build(new InputSource(xmlFilePath));

            DbTestUtil.replacementDataSet(ds);

            DatabaseOperation.CLEAN_INSERT.execute(dbconn, ds);
        } catch (SQLException | DatabaseUnitException e) {
            e.printStackTrace();
        }
    }


}

