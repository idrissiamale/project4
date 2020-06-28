package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * A test class version of DataBaseConfig class which gives access to the test database.
 *
 * @see com.parkit.parkingsystem.config.DataBaseConfig
 */
public class DataBaseTestConfig extends DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");

    /**
     * Establishing a connection to the Test database by using the given database url.
     *
     * @return a connection to the url.
     * @throws ClassNotFoundException will be thrown if the JDBC driver is not available in Classpath or if the class loader is not able to find it.
     * @throws SQLException           will be thrown if database access errors occurs or url is null.
     * @throws IOException            Loading the properties file "database.properties" with the JDBC driver, url, username and password.
     *                                IOException will be thrown if the file is not found.
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("src/main/resources/database.properties");
        properties.load(in);
        in.close();
        String driver = properties.getProperty("jdbc.driver");
        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");
        logger.info("Create DB connection");
        Class.forName(driver);
        return DriverManager.getConnection(
                url, username, password);
    }

    /**
     * Closing the connection to the Test database.
     *
     * @param con, connection to the given database url.
     */
    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }

    /**
     * Closing the prepared statement.
     *
     * @param ps, a precompiled SQL statement.
     * @see com.parkit.parkingsystem.constants.DBConstants
     */
    public void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement", e);
            }
        }
    }

    /**
     * Closing the database result sets.
     *
     * @param rs, a result set.
     */
    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set", e);
            }
        }
    }
}
