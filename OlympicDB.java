import java.util.Properties;
import java.sql.*;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;

public class OlympicDB {
    private String user;
    private String pass; 
    private Connection conn; 

    private boolean connected; 

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public OlympicDB(BufferedReader br, String user, String pass){
        this.br = br;
        this.user = user;
        this.pass = pass;
    }   

    public static void main(String[] args){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        OlympicDB od = new OlympicDB(br, "postgres", "cowcow2024"); 

        String menuOptions = ""; 
        menuOptions = od.getMenuOptions();

        String choice = "";

        boolean loop = true;

        System.out.println("--------------------------------------------------");
        System.out.println("Welcome to the Olympic DB, how may we help?");
        System.out.println("--------------------------------------------------");
        System.out.println(menuOptions);
        System.out.println("--------------------------------------------------");

        
        while(loop){
            try {
                System.out.print(">> ");
                choice = br.readLine();
            } catch (IOException e){
                System.out.println("Issue with capturing input"); 
            }
            
            switch (choice) {
                case "1":
                    System.out.println("---");
                    System.out.println("You are connecting to the system!"); 
                    od.connect();

                    break;

                case "2":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {
                        System.out.println("Creating a new account!");
                        od.callCreateAccount();
                    }
                    break;

                case "3":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {
                        System.out.println("Removing an account...");
                        od.callRemoveAccount();
                    }
                    break;

                case "4":
                    System.out.println("---");
                        if(od.connected == false){
                            System.out.println("You need to connect to a DB first."); 
                        } else {
                            System.out.println("Adding a new participant!");
                            od.callRemoveAccount();
                        }
                    break;

                case "5":
                    System.out.println("Removing participant...");
                    break;

                case "6":
                    System.out.println("Adding a team member!");
                    break;

                case "7":
                    System.out.println("Removing a team member...");
                    break;
                
                case "8":
                    System.out.println("Registering a new team!");
                    break;

                case "9":
                    System.out.println("Adding a new event!");
                    break;

                case "10":
                    System.out.println("Adding a team to an event!");
                    break;

                case "11":
                    System.out.println("Adding event outcomes!");
                    break;

                case "12":
                    System.out.println("Disqualifying a team...");
                    break;

                case "13":
                    System.out.println("Listing venues in an Olympiad...");
                    break;

                case "14":
                    System.out.println("Listing events of an Olympiad...");
                    break;

                case "15":
                    System.out.println("Listing teams in an event...");
                    break;

                case "16":
                    System.out.println("Showing placements in an event...");
                    break;

                case "17":
                    System.out.println("Listing participants on a team...");
                    break;

                case "18":
                    System.out.println("Listing country placements in an Olympiad...");
                    break;

                case "19":
                    System.out.println("Listing athlete placement...");
                    break;

                case "20":
                    System.out.println("Displaying country rankings...");
                    break;

                case "21":
                    System.out.println("Listing most successful participants in an Olympiad...");
                    break;

                case "22":
                    System.out.println("Listing top sports...");
                    break;

                case "23":
                    System.out.println("Finding connected coaches...");
                    break;

                case "24":
                    System.out.println("Exiting the system...");
                    loop = false;
                    break;
                
                default:
                    System.out.println("That's an invalid input, please let us know if you would like to quit by pressing 24"); 
            }
        }


    }

    public String getMenuOptions(){
        StringBuilder sb = new StringBuilder();

        sb.append("1. connect to db \n"); 
        sb.append("2. create account \n"); 
        sb.append("3. remove account \n"); 
        sb.append("4. add participant \n"); 
        sb.append("5. remove participant \n"); 
        sb.append("6. add team member \n"); 
        sb.append("7. remove team member \n"); 
        sb.append("8. register team \n"); 
        sb.append("9. add event \n"); 
        sb.append("10. add team event \n"); 
        sb.append("11. add event outcome \n"); 
        sb.append("12. disqualify team \n"); 
        sb.append("13. list venues in olympiad \n"); 
        sb.append("14. list events of olympiad \n"); 
        sb.append("15. list teams in event \n"); 
        sb.append("16. show placements in event \n"); 
        sb.append("17. list participants on team \n"); 
        sb.append("18. list country placements in olympiad \n"); 
        sb.append("19. list athlete placement \n"); 
        sb.append("20. country rankings \n"); 
        sb.append("21. most successful participants in olympiad \n"); 
        sb.append("22. top sports \n"); 
        sb.append("23. connected coaches \n"); 
        sb.append("24. exit"); 

        return sb.toString();
    }

    public void loadJDBC() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException err) {
            System.err.println("Unable to detect the JDBC .jar dependency. Check that the library is correctly loaded in and try again.");
            System.exit(-1);
        }
    }

    public void setDbInfo(BufferedReader br) {
        try {
            System.out.print("Enter username: ");
            user = br.readLine();
            System.out.print("Enter password: ");
            pass = br.readLine();
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("IllegalArgumentException " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("IOException " + ex.getMessage());
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

    public void connect() {
        loadJDBC();
        setDbInfo(br);
        String url = "jdbc:postgresql://localhost:5432/";
        Properties props = new Properties();
        props.setProperty("user", getUser());
        props.setProperty("password", getPass());
        props.setProperty("escapeSyntaxCallMode", "callIfNoReturn");
        try {
            conn = DriverManager.getConnection(url, props);
            conn.setSchema("olympic_schema");
            connected = true;
            System.out.println("Connected to the database successfully.");

        } catch (Exception ex) {
            System.out.println("Failed to connect to the database. Please try again. " + ex.getMessage());
        }

    }

    public void callCreateAccount(){        
        CallableStatement properCase = null;
        try {
            properCase = conn.prepareCall("{ ? = CALL create_account( ?, ?, ? ) }");
        
            System.out.print("Enter username: ");
            String username = br.readLine();
            System.out.print("Enter password: ");
            String password = br.readLine();
            System.out.print("Enter role: ");
            String role = br.readLine();

            Boolean rReturn;

            properCase.registerOutParameter(1, Types.BIT);
            properCase.setString(2, username);
            properCase.setString(3, password);
            properCase.setString(4, role);

            properCase.execute();

            rReturn = properCase.getBoolean(1);

            if(rReturn){
                System.out.println("Account was created successfully!");
            } else {
                System.out.println("Account was not created... please try again");
            }
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } catch (IOException ex) {
            System.err.println("IO Exception E" + ex.getMessage());
        }  
    }

    public void callRemoveAccount(){        
        CallableStatement properCase = null;
        try {
            properCase = conn.prepareCall("{ CALL remove_account( ? ) }");
        
            System.out.print("Enter account id: ");
            int id = Integer.parseInt(br.readLine());

            properCase.setInt(1, id);

            properCase.execute();

            System.out.println("Successfully removed account"); 
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } catch (IOException ex) {
            System.err.println("IO Exception E" + ex.getMessage());
        }
    }

}
