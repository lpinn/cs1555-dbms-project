import java.util.Properties;
import java.sql.*;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;

public class OlympicDB {
    private String user = "postgres"; 
    private String pass = "cowcow24"; 
    private boolean contMenuLoop = true; 
    private ArrayList<String> options_list = new ArrayList<String>();
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 
    public static void main(String[] args) throws ClassNotFoundException {
        String choice = "";
        int op_choice = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        OlympicDB od = new OlympicDB(br);
        String mo = od.menuOptions(); 

        od.loadJDBC(); 
        od.setDbInfo(br);

        String url = "jdbc:postgresql://localhost:5432/";
        Properties props = new Properties();
        props.setProperty("user", od.getUser());
        props.setProperty("password", od.getPass());

        System.out.println("---Welcome to the Olympic DB, how may we help you?---"); 

            do {
                System.out.println("Press 1 to use DB functionality, 2 for menu options, or any other key to quit");
                do { 
                    try {
                        choice = od.br.readLine();
                    } catch (IOException e){
                        System.err.println("IOException");
                    }
                    if(!choice.equals("1") || !choice.equals("2")){
                        od.contMenuLoop = false;                    
                    }
                } while (od.contMenuLoop); 
            
                if(choice.equals("1")){
                    od.contMenuLoop = true;
                    do {
                        System.out.println("Which operation would you like to perform? Please choose a valid operation or press any number that isn't associated with an operation to quit."); 

                        try {
                            choice = od.br.readLine();
                            op_choice = Integer.parseInt(choice);
                        } catch (IOException e){
                            System.err.println("IOException");
                        }
                        if(op_choice == -1 || op_choice > od.options_list.size()){
                            od.contMenuLoop = false;
                        }

                        switch (op_choice) {
                            case 1:
                                try (Connection conn = DriverManager.getConnection(url, props);
                                CallableStatement properCase = conn.prepareCall("{ ? = CALL create_account( ?, ?, ? ) }")){
                                    conn.setSchema("olympic_schema");

                                    od.callCreateAccount(properCase);
                                } catch (SQLException err) {
                                    System.out.println("SQL Error");
                                    while (err != null) {
                                        System.out.println("Message = " + err.getMessage());
                                        System.out.println("SQLState = " + err.getSQLState());
                                        System.out.println("SQL Code = " + err.getErrorCode());
                                        err = err.getNextException();
                                    }
                                }
                                break;
                            case 2:
                                props.setProperty("escapeSyntaxCallMode", "callIfNoReturn");

                                try (Connection conn = DriverManager.getConnection(url, props);
                                 CallableStatement properCase = conn.prepareCall("{ CALL remove_account( ? ) }")){
                                    conn.setSchema("olympic_schema");

                                    od.callRemoveAccount(properCase);
                                } catch (SQLException err) {
                                    System.out.println("SQL Error");
                                    while (err != null) {
                                        System.out.println("Message = " + err.getMessage());
                                        System.out.println("SQLState = " + err.getSQLState());
                                        System.out.println("SQL Code = " + err.getErrorCode());
                                        err = err.getNextException();
                                    }
                                }
                                break;
                            case 3:
                                try (Connection conn = DriverManager.getConnection(url, props);
                                 CallableStatement properCase = conn.prepareCall("{ ? = CALL add_participant( ?, ?, ?, ?, ?, ?, ? ) }")){
                                    conn.setSchema("olympic_schema");

                                    od.callAddParticipant(properCase);
                                } catch (SQLException err) {
                                    System.out.println("SQL Error");
                                    while (err != null) {
                                        System.out.println("Message = " + err.getMessage());
                                        System.out.println("SQLState = " + err.getSQLState());
                                        System.out.println("SQL Code = " + err.getErrorCode());
                                        err = err.getNextException();
                                    }
                                }
                                break;
                            case 4:
                                props.setProperty("escapeSyntaxCallMode", "callIfNoReturn");

                                try (Connection conn = DriverManager.getConnection(url, props);
                                 CallableStatement properCase = conn.prepareCall("{ CALL remove_participant( ? ) }")){
                                    conn.setSchema("olympic_schema");

                                    od.callRemoveParticipant(properCase);
                                } catch (SQLException err) {
                                    System.out.println("SQL Error");
                                    while (err != null) {
                                        System.out.println("Message = " + err.getMessage());
                                        System.out.println("SQLState = " + err.getSQLState());
                                        System.out.println("SQL Code = " + err.getErrorCode());
                                        err = err.getNextException();
                                    }
                                }
                                break;
                            case 5:
                                try (Connection conn = DriverManager.getConnection(url, props);
                                 CallableStatement properCase = conn.prepareCall("{ ? = CALL add_team_member ( ?, ? ) }")){
                                    conn.setSchema("olympic_schema");

                                    od.callAddTeamMember(properCase);
                                } catch (SQLException err) {
                                    System.out.println("SQL Error");
                                    while (err != null) {
                                        System.out.println("Message = " + err.getMessage());
                                        System.out.println("SQLState = " + err.getSQLState());
                                        System.out.println("SQL Code = " + err.getErrorCode());
                                        err = err.getNextException();
                                    }
                                }
                                break;
                            case 6:
                                props.setProperty("escapeSyntaxCallMode", "callIfNoReturn");

                                try (Connection conn = DriverManager.getConnection(url, props);
                                 CallableStatement properCase = conn.prepareCall("{ CALL remove_team_member ( ?, ? ) }")){
                                    conn.setSchema("olympic_schema");

                                    od.callRemoveTeamMember(properCase);
                                } catch (SQLException err) {
                                    System.out.println("SQL Error");
                                    while (err != null) {
                                        System.out.println("Message = " + err.getMessage());
                                        System.out.println("SQLState = " + err.getSQLState());
                                        System.out.println("SQL Code = " + err.getErrorCode());
                                        err = err.getNextException();
                                    }
                                }
                                break;
                            case 7:
                                props.setProperty("escapeSyntaxCallMode", "callIfNoReturn");

                                try (Connection conn = DriverManager.getConnection(url, props);
                                 CallableStatement properCase = conn.prepareCall("{ ? = CALL register_team ( ?, ?, ?, ?, ? ) }")){
                                    conn.setSchema("olympic_schema");

                                    od.callRegisterTeam(properCase);
                                } catch (SQLException err) {
                                    System.out.println("SQL Error");
                                    while (err != null) {
                                        System.out.println("Message = " + err.getMessage());
                                        System.out.println("SQLState = " + err.getSQLState());
                                        System.out.println("SQL Code = " + err.getErrorCode());
                                        err = err.getNextException();
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    } while (od.contMenuLoop); 
                } else if (choice.equals("2")){
                    od.contMenuLoop = true;
                    System.out.println(mo);
                } 
            } while (od.contMenuLoop);
    }

    public OlympicDB(BufferedReader br) {
        this.br = br;
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
            System.err.println("No lines were read from user input, please try again.");
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again.");
        } catch (IOException ex) {
            System.err.println("IOException");
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
        options.append("--------------------------------------------------------\n");

        options.append("1. Create Account \n"); 
        options_list.add("1. Create Account \n"); 

        options.append("2. Remove Account \n"); 
        options_list.add("2. Remove Account \n"); 

        options.append("3. Add Participant \n"); 
        options_list.add("3. Add Participant \n");

        options.append("4. Remove Participant \n");
        options_list.add("4. Remove Participant \n");

        options.append("5. Add Team Member \n");
        options_list.add("5. Add Team Member \n");

        options.append("6. Remove Team Member \n");
        options_list.add("6. Remove Team Member \n");

        options.append("7. Register Team \n");
        options_list.add("7. Register Team \n");

        options.append("--------------------------------------------------------\n");

        return options.toString(); 
    }

    public void callCreateAccount(CallableStatement pc){
        System.out.println("You're creating an account! Please enter the following"); 
        try {
            System.out.print("Enter username: ");
            String username = br.readLine();
            System.out.print("Enter password: ");
            String password = br.readLine();
            System.out.print("Enter role: ");
            String role = br.readLine();

            Boolean rReturn;

            pc.registerOutParameter(1, Types.BIT);
            pc.setString(2, username);
            pc.setString(3, password);
            pc.setString(4, role);

            pc.execute();

            rReturn = pc.getBoolean(1);

            if(rReturn){
                System.out.println("Account was created successfully!");
            } else {
                System.out.println("Account was not created...");
            }
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again.");
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again.");
        } catch (SQLException ex) {
            System.err.println("SQL Exception E"); 
        } catch (IOException ex) {
            System.err.println("IO Exception");
        }
        
    }

    public void callRemoveAccount(CallableStatement pc){
        System.out.println("You want to remove an account. Please enter the id of the account you want to remove:"); 
        try {
            System.out.print("Enter account id: ");
            int id = Integer.parseInt(br.readLine());

            pc.setInt(1, id);

            pc.execute();

            System.out.println("Removed account"); 

        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again.");
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("SQL Exception: " + ex.getMessage());        
        } catch (IOException ex) {
            System.err.println("IO Exception");
        }
    }

    public void callAddParticipant(CallableStatement pc){
        System.out.println("You're creating a new participant! Please enter the following"); 
        try {
            System.out.print("Enter account id: ");
            int account_id = Integer.parseInt(br.readLine());
            System.out.print("Enter first name: ");
            String first = br.readLine();
            System.out.print("Enter middle name (if applicable, press enter if not): ");
            String middle = br.readLine();
            System.out.print("Enter last name: ");
            String last = br.readLine();
            System.out.print("Enter birth country: ");
            String country = br.readLine();
            System.out.print("Enter date of birth (in this format - YYYY-MM-DD): ");
            String t = br.readLine();
            Timestamp dob = Timestamp.valueOf(t);
            System.out.print("Enter gender (M/F): ");
            String gender = br.readLine();

            Boolean rReturn;

            pc.registerOutParameter(1, Types.BIT);
            pc.setInt(2, account_id);
            pc.setString(3, first);
            pc.setString(4, middle);
            pc.setString(5, last);
            pc.setString(6, country);
            pc.setTimestamp(7, dob);
            pc.setString(8, gender);

            pc.execute();

            rReturn = pc.getBoolean(1);

            if(rReturn){
                System.out.println("Participant was added successfully!");
            } else {
                System.out.println("Participant could not be created...");
            }
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again.");
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("SQL Exception: " + ex.getMessage());        
        } catch (IOException ex) {
            System.err.println("IO Exception");
        }
    }

    public void callRemoveParticipant(CallableStatement pc){
        System.out.println("You want to remove a participant. Please enter the id of the participant you want to remove:"); 
        try {
            System.out.print("Enter participant id: ");
            int id = Integer.parseInt(br.readLine());

            pc.setInt(1, id);

            pc.execute();

            System.out.println("Removed participant"); 

        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again.");
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("SQL Exception: " + ex.getMessage());        
        } catch (IOException ex) {
            System.err.println("IO Exception");
        }
    }

    public void callAddTeamMember(CallableStatement pc){
        System.out.println("You are adding a team member! Please enter the following:"); 
        try {
            System.out.print("Enter team id: ");
            int team_id = Integer.parseInt(br.readLine());
            System.out.print("Enter participant id: ");
            int participant_id = Integer.parseInt(br.readLine());
            
            Boolean rReturn;

            pc.registerOutParameter(1, Types.BIT);
            pc.setInt(2, team_id);
            pc.setInt(3, participant_id);
            
            pc.execute();

            rReturn = pc.getBoolean(1);

            if(rReturn){
                System.out.println("Team member was added successfully!");
            } else {
                System.out.println("Team member could not be created...");
            }
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again.");
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("SQL Exception: " + ex.getMessage());        
        } catch (IOException ex) {
            System.err.println("IO Exception");
        }
    }
    
    public void callRemoveTeamMember(CallableStatement pc){
        System.out.println("You are removing a team member! Please enter the following:"); 
        try {
            System.out.print("Enter team id: ");
            int team_id = Integer.parseInt(br.readLine());
            System.out.print("Enter participant id: ");
            int participant_id = Integer.parseInt(br.readLine());
            
            pc.setInt(1, participant_id);
            pc.setInt(2, team_id);
            
            pc.execute();

            System.out.println("Removed team member\n");
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again.");
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("SQL Exception: " + ex.getMessage());        
        } catch (IOException ex) {
            System.err.println("IO Exception");
        }
    }

    public void callRegisterTeam(CallableStatement pc){
        System.out.println("You are registering a team! Please enter the following:"); 
        try {
            System.out.print("Enter olympiad: ");
            String olympiad = br.readLine();
            System.out.print("Enter sport id: ");
            int sport_id = Integer.parseInt(br.readLine());
            System.out.print("Enter coach id: ");
            int coach_id = Integer.parseInt(br.readLine());
            System.out.print("Enter country code: ");
            String country_code = br.readLine();
            System.out.print("Enter gender: ");
            String gender = br.readLine();

            Boolean rReturn;

            pc.registerOutParameter(1, Types.BIT);
            pc.setString(2, olympiad);
            pc.setInt(3, sport_id);
            pc.setInt(4, coach_id);
            pc.setString(5, country_code);
            pc.setString(6, gender);

            pc.execute();

            rReturn = pc.getBoolean(1);

            if(rReturn){
                System.out.println("Team was registered successfully!");
            } else {
                System.out.println("Team could not be registered...");
            }
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again.");
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("SQL Exception: " + ex.getMessage());        
        } catch (IOException ex) {
            System.err.println("IO Exception");
        }
    }
}
