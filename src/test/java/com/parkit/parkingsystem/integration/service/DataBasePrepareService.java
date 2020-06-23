package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;

/**
 * A class used to clear database entries. It's connected to the test database.
 *
 * @see DataBaseTestConfig
 */
public class DataBasePrepareService {

    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    /**
     * A method which sets parking entries to available. It permits also to clear ticket entries.
     */
    public void clearDataBaseEntries() {
        Connection connection = null;
        try {
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to available
            connection.prepareStatement("update parking set available = true").execute();

            //clear ticket entries;
            connection.prepareStatement("truncate table ticket").execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }


}
