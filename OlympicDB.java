import java.util.NoSuchElementException;
import java.util.Scanner;
import java.sql.*;
import java.util.Properties;

public class OlympicDB {
    private String user = "postgres";
    private String pass = "cowcow2024";
    private static final String USER_REQUEST_STR = "Enter the username for connecting to the database: ";
    private static final String PASS_REQUEST_STR = "Enter the password for connecting to the database: ";

    public static void main(String[] args) {
        boolean contMenuLoop = true;
        // Ensure that the JDBC driver library is correctly loaded in
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException err) {
            System.err.println("Unable to detect the JDBC .jar dependency. Check that the library is correctly loaded in and try again.");
            System.exit(-1);
        }

        OlympicDB inputConfig = new OlympicDB();

        String url = "jdbc:postgresql://localhost:5432/";
        Properties props = new Properties();
        props.setProperty("user", inputConfig.getUser());
        props.setProperty("password", inputConfig.getPass());

        do {
            
        } while(true); 

        // Utilize Java try-with-resources to automatically close the connection and statement
        try (Connection conn = DriverManager.getConnection(url, props);
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            // Set the Connection's default schema to match our .sql file
            conn.setSchema("olympic_schema");
            ResultSet resultSet = st.executeQuery("SELECT * FROM MEDAL");

            int medal_id;
            String type;
            int points;

            while(resultSet.next()){
                medal_id = resultSet.getInt("medal_id");
                type = resultSet.getString("type");
                points = resultSet.getInt("points");

                System.out.println(medal_id + " " + type + " " + points); 
            }    

            resultSet.close();
            st.close();
            conn.close();
        } catch (SQLException e1) {
            // JDBC will throw a SQLException if errors occur on the database
            System.err.println("SQL Error");

            // Note that this returns an iterable of errors
            while (e1 != null) {
                System.err.println("Message = " + e1.getMessage());
                System.err.println("SQLState = " + e1.getSQLState());
                System.err.println("SQL Code = " + e1.getErrorCode());
                e1 = e1.getNextException();
            }
        }
    }

    public OlympicDB() {
        Scanner scanner = new Scanner(System.in);
        requestUsername(scanner);
        requestPassword(scanner);
        scanner.close();
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    private String requestUserInput(String inputRequestStr, Scanner scanner) {
        try {
            System.out.println(inputRequestStr);
            return scanner.nextLine();
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again.");
            return null;
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again.");
            return null;
        }
    }

    private void requestUsername(Scanner scanner ) {
        String username = requestUserInput(USER_REQUEST_STR, scanner);
        if (username != null)
            this.user = username;
    }

    private void requestPassword(Scanner scanner ) {
        String pw = requestUserInput(PASS_REQUEST_STR, scanner);
        if (pw != null)
            this.pass = pw;
    }

}
