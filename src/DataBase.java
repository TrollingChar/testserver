import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by UserName on 25.07.2016.
 */
public class DataBase {
    static Connection dbh;
    static Statement state;

    public static final String url = "jdbc:postgresql://127.0.0.1/users";
    public static final String userName = "postgres";
    public static final String password = "123456";
    public static final String dbName = "test";

    public static void configure() throws Exception
    {
        dbh = DriverManager.getConnection(url, userName, password);
    }

    public static ResultSet getStatus(int id) throws Exception
    {
        ResultSet result;
        state = dbh.createStatement();
        result = state.executeQuery("select score from " + dbName + " where id = " + id);
        state.close();
        if(result.next())
            return result;
        return null;
    }

    public static ResultSet signUp(int id) throws Exception
    {
        state.executeUpdate("INSERT INTO " + dbName + " VALUES( " + id + ", 0);");
        state.close();
        return getStatus(id);
    }
}
