import java.util.Properties;
import java.sql.*;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class OlympicDB {
    private Scanner sc = new Scanner(System.in); 
    private String user = "postgres"; 
    private String pass = "cowcow24"; 
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner sc = new Scanner(System.in); 
        OlympicDB od = new OlympicDB(sc);

        od.loadJDBC(); 
        od.setDbInfo(sc);

        String url = "jdbc:postgresql://localhost:5432/";
        Properties props = new Properties();
        props.setProperty("user", od.getUser());
        props.setProperty("password", od.getPass());

        // Java try-with-resources automatically closes the connection and callable statement
        try (Connection conn = DriverManager.getConnection(url, props)){
             //CallableStatement properCase = conn.prepareCall("{ ? = call ( ? ) }")) {
            // Set the Connection's default schema to match our .sql file
            conn.setSchema("olympic_schema");
            //calling a function with return value
        } catch (SQLException err) {
            System.out.println("SQL Error");
            while (err != null) {
                System.out.println("Message = " + err.getMessage());
                System.out.println("SQLState = " + err.getSQLState());
                System.out.println("SQL Code = " + err.getErrorCode());
                err = err.getNextException();
            }
        }
    }

    public OlympicDB(Scanner sc) {
        this.sc = sc;
    }

    public void loadJDBC() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException err) {
            System.err.println("Unable to detect the JDBC .jar dependency. Check that the library is correctly loaded in and try again.");
            System.exit(-1);
        }
    }

    public void setDbInfo(Scanner sc) {
        System.out.println("Please enter your username and password (one after the other): ");
        try {
            user = sc.nextLine(); 
            pass = sc.nextLine(); 
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again.");
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again.");
        }
    }

    public String getUser(){
        if(user != null){
            return user; 
        }
        return null; 
    }

    public String getPass(){
        if(pass != null){
            return pass;
        }
        return null; 
    } 

}
