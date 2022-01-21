package data_access;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.Setter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Setter
public class ConnectionFactory
{
    private static final String URL = "jdbc:mysql://localhost:3306/dbslide";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static DataSource getDataSource()
    {
        MysqlDataSource ds = new MysqlDataSource();



        return ds;
    }
}
