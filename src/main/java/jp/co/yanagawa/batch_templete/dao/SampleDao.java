package jp.co.yanagawa.bachTemplete.dao;

import jp.co.yanagawa.bachTemplete.entity.AudienceIdEncryption;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class SampleDao extends DbControl {
    private static final Logger LOGGER = Logger.getLogger(sampleDao.class);

    public static final String TARGET_TBL = "";

    /**
     *
     * @return
     * @throws SQLException
     */
    public static SampleEntity selectEncryptionInfo() throws Exception {
        SampleEntity dspList;

        Connection conn = null;
        try {
            StringBuilder sql = new StringBuilder();
            conn = getDbConnection();
            dspList = queryForSinglRowCamelCase(conn, sql.toString(), AudienceIdEncryption.class, new Object[]{});
        } finally {
            closeConnection(conn);
        }
        return dspList;
    }

}