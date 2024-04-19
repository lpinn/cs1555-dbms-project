import java.util.Properties;
import java.sql.*;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.ArrayList;

public class OlympicDB {
    private Scanner sc = new Scanner(System.in); 
    private String user = "postgres"; 
    private String pass = "cowcow24"; 
    private boolean contMenuLoop = true; 
    private ArrayList<String> options_list = new ArrayList<String>(); 
    public static void main(String[] args) throws ClassNotFoundException {
        String choice = "";
        Scanner sc = new Scanner(System.in); 
        OlympicDB od = new OlympicDB(sc);

        od.loadJDBC(); 
        od.setDbInfo(sc);

        String url = "jdbc:postgresql://localhost:5432/";
        Properties props = new Properties();
        props.setProperty("user", od.getUser());
        props.setProperty("password", od.getPass());

        System.out.println("Welcome to the Olympic DB, how may we help you?"); 

        try (Connection conn = DriverManager.getConnection(url, props)){
            conn.setSchema("recitation");
            do {
                System.out.println("Press 1 for Database search, 2 for menu options, or any other key to quit");
                do { 
                    choice = sc.nextLine(); 
                    if(!choice.equals("1") || !choice.equals("2")){
                        od.contMenuLoop = false;                    
                    }
                } while (od.contMenuLoop); 
            
                if(choice.equals("1")){
                    od.contMenuLoop = true;
                    int num = 0; 
                    System.out.println("Which operation would you like to perform? Please choose a valid operation or press q to quit."); 
                    do { 
                        choice = sc.nextLine(); 
                        num = Integer.parseInt(choice); 
                        if(num <= od.options_list.size()){
                            od.contMenuLoop = false;                    
                        } else if
                    } while (od.contMenuLoop); 

                } else if (choice.equals("2")){
                    od.contMenuLoop = true;
                    od.menuOptions(); 
                } 

                /*
                CallableStatement properCase = conn.prepareCall("{ ? = call can_pay_loan( ? ) }"); 
                Boolean rReturn;
                properCase.registerOutParameter(1, Types.BIT);
                properCase.setString(2, "111222333");
                properCase.execute();
                rReturn = properCase.getBoolean(1);
                System.out.println(rReturn);
                */
            } while (od.contMenuLoop);
            
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

    public String menuOptions() {
        StringBuilder options = new StringBuilder(); 
        options.append("1. Can pay loan \n"); 
        options_list.add("1. Can pay loan \n"); 

        return options.toString(); 
    }

}
