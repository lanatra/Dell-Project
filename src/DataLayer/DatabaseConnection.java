package DataLayer;

import com.mchange.v2.c3p0.ComboPooledDataSource;


import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private ComboPooledDataSource cpds;
    private static DatabaseConnection datasource;

    private DatabaseConnection() throws IOException, SQLException,PropertyVetoException {

        cpds = new ComboPooledDataSource();
        cpds.setDriverClass("oracle.jdbc.driver.OracleDriver"); //loads the jdbc driver
        cpds.setJdbcUrl("jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat");
        cpds.setUser("cphlj222");
        cpds.setPassword("cphlj222");

        cpds.setInitialPoolSize(7);
        cpds.setAcquireIncrement(15);
        cpds.setMaxPoolSize(1000);
        cpds.setMinPoolSize(3);
        cpds.setMaxStatements(10);
        cpds.setIdleConnectionTestPeriod(2000);
    }

    public static DatabaseConnection getInstance() throws IOException, SQLException,PropertyVetoException {

        if(datasource == null){
            synchronized (DatabaseConnection.class) {
                if(datasource == null){
                    datasource = new DatabaseConnection();
                }
            }
        }
        return datasource;
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }

}
