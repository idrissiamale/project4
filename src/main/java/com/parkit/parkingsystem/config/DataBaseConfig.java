package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * A class which configures and gives access to the parking system database.
 */
public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    /**
     * Establishing a connection to the database by using the given database url.
     *
     * @return a connection to the url.
     * @throws ClassNotFoundException will be thrown if the JDBC driver is not available in Classpath or if the class loader is not able to find it.
     * @throws SQLException           will be thrown if database access errors occurs or url is null.
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/prod?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "rootroot");
    }

    /**
     * Closing the connection to the database.
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
