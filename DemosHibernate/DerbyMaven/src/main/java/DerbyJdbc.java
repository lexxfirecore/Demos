import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.derby.tools.ij;

/**
 * This program demonstrates how to connect to Apache Derby (Java DB) database
 * for the embedded driver and network client driver.
 * @author www.codejava.net
 *
 */
public class DerbyJdbc {

    static String dbURL1 = "jdbc:derby:codejava/webdb1;create=true";



    public static void main(String[] args) {

        derbyRun();
//        connectDerby();
    }

    private static void derbyRun() {
        try {
            ij.startJBMS();
            //ij.main();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void connectDerby() {
        try {
            // connect method #1 - embedded driver

            Connection conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                System.out.println("Connected to database #1");
            }
/*

            // connect method #2 - network client driver
            String dbURL2 = "jdbc:derby://localhost/webdb2;create=true";
            String user = "tom";
            String password = "secret";
            Connection conn2 = DriverManager.getConnection(dbURL2, user, password);
            if (conn2 != null) {
                System.out.println("Connected to database #2");
            }

            // connect method #3 - network client driver
            String dbURL3 = "jdbc:derby://localhost/webdb3";
            Properties properties = new Properties();
            properties.put("create", "true");
            properties.put("user", "tom");
            properties.put("password", "secret");


            Connection conn3 = DriverManager.getConnection(dbURL3, properties);
            if (conn3 != null) {
                System.out.println("Connected to database #3");
            }

            */
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}