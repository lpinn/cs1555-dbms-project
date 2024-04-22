import java.util.Properties;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.BufferedReader;

public class OlympicDB {
    private String user;
    private String pass; 
    private Connection conn; 

    private boolean connected; 

    Scanner sc;
    BufferedReader br;

    public OlympicDB(BufferedReader br, Scanner sc, String user, String pass){
        this.br = br;
        this.sc = sc;
        this.user = user;
        this.pass = pass;
    }   

    public static void main(String[] args){
        Scanner sc = new Scanner(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
        OlympicDB od = new OlympicDB(br, sc, "postgres", "cowcow2024"); 

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
                            od.callAddParticipant();
                        }
                    break;

                case "5":
                    System.out.println("---");
                        if(od.connected == false){
                            System.out.println("You need to connect to a DB first."); 
                        } else {
                            System.out.println("Removing participant...");
                            od.callRemoveParticipant();
                        }
                    break;

                case "6":
                    System.out.println("---");
                        if(od.connected == false){
                            System.out.println("You need to connect to a DB first."); 
                        } else {
                            System.out.println("Adding a team member!");
                            od.callAddTeamMember();
                        }
                    break;

                case "7":
                    System.out.println("---");
                        if(od.connected == false){
                            System.out.println("You need to connect to a DB first."); 
                        } else {
                            System.out.println("Removing a team member...");
                            od.callRemoveTeamMember();
                        }
                    break;
                
                case "8":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {
                        System.out.println("Registering a new team!");
                        od.callRegisterTeam();
                    }
                    break;

                case "9":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first.");
                    } else {
                        System.out.println("Adding a new event!");
                    }
                    
                    break;

                case "10":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    }else{
                        System.out.println("Adding a team to an event!");
                        od.addTeamToEvent();
                    }
                    break;

                case "11":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {
                        System.out.println("Adding an outcome to an event!");
                        od.addEventOutcome();
                    }
                    break;

                case "12":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {
                        System.out.println("Disqualifying a team...");
                        od.disqualifyTeam();
                    }
                    break;

                case "13":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {
                        System.out.println("Listing venues in an Olympiad...");
                        od.listVenuesInOlympiad();
                    }
                    break;

                case "14":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {       
                        System.out.println("Listing events of an Olympiad...");
                        od.listEventsOfOlympiad();
                    }
                    break;

                case "15":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {
                        System.out.println("Listing teams in an event...");
                        od.listTeamsInEvent();
                    }
                    break;

                case "16":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {
                        System.out.println("Showing placements in an event...");
                        od.showPlacementsInEvent();
                    }
                    break;

                case "17":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {
                        System.out.println("Listing participants on a team...");
                        od.listParticipantsOnTeam();
                    }
                    break;

                case "18":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {
                        System.out.println("Listing country placements in an Olympiad...");
                        od.listCountryPlacementsInOlympiad();
                    }
                    break;

                case "19":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {   
                        System.out.println("Listing athlete placement...");
                        od.listAthletePlacement();
                    }
                    break;

                case "20":
                System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {   
                        System.out.println("Displaying country rankings...");
                        od.callCountryRankings(); 
                    }
                    
                    break;

                case "21":
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {   
                        System.out.println("Listing most successful participants in an Olympiad...");
                        od.callMostSuccessfulParticipantsInOlympiad();
                    }
                
                    break;

                case "22":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {   
                        System.out.println("Listing top sports...");
                        od.topSports();
                    }break;

                case "23":
                    System.out.println("---");
                    if(od.connected == false){
                        System.out.println("You need to connect to a DB first."); 
                    } else {   
                        System.out.println("Finding connected coaches...");
                        od.connectedCoaches();
                    }
                    break;

                case "24":
                    System.out.println("Exiting the system...");
                    od.exit();
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

    public int readInt(Scanner sc, String label) {
        boolean validInput = false;
        String str = null;
        int i = 0;
        while (!validInput) {
            System.out.print(label);
            try {
                str = sc.nextLine();
                i = Integer.parseInt(str);
                validInput = true;
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid integer value entered");
            }
        }
        return i;
    }

    public String readString(Scanner sc, String label, int len, boolean allow_empty) {
        boolean validInput = false;
        String str = null;
        while (!validInput) {
            System.out.print(label);
            str = sc.nextLine();

            if (str.trim().equals("") && !allow_empty) {
                System.out.println("Empty string is not allowed for input");
            } else if (str.length() > len) {
                System.out.println("String size exceeds allowed length");
            } else {
                validInput = true;
            }
        }
        return str;
    }

    public java.util.Date readDate(Scanner sc, String label, String format) {
        boolean validInput = false;
        String str = null;
        java.util.Date dt = null;
        while (!validInput) {
            System.out.print(label);
            str = sc.nextLine();

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                dt = sdf.parse(str);
                validInput = true;
            } catch (ParseException pe) {
                System.out.println("Invalid date entered");
            }
        }
        return dt;
    }

    public Timestamp readTimestamp(Scanner scanner, String label) {
        boolean validInput = false;
        String str = null;
        Timestamp ts = null;
        while (!validInput) {
            System.out.print(label);
            str = scanner.nextLine();

            try {
                System.out.println(str);
                ts = Timestamp.valueOf(str);
                validInput = true;
            } catch (IllegalArgumentException iae) {
                System.out.println("Invalid date entered. Please reenter");
            }
        }
        return ts;
    }

    public String rPad(String in, int len) {
        if (in == null) {
            in = "";
        }
        StringBuffer sb = new StringBuffer(in);

        for (int i = 0; i < (len - in.length()); i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public void closeStatement(Statement st){
        if(st != null){
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeRS(ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            System.out.println("Connected to the database successfully.");

        } catch (Exception ex) {
            System.out.println("Failed to connect to the database. Please try again. " + ex.getMessage());
        }

    }

    public void callCreateAccount() {        
        CallableStatement properCase = null;
        try {
            conn.setAutoCommit(false); 

            properCase = conn.prepareCall("{ ? = CALL create_account( ?, ?, ? ) }");
        
            String username = readString(sc, "Enter username: ", 30, false);
            String password = readString(sc, "Enter password: ", 30, false);
            String role = readString(sc, "Enter Role: ", 12, false);

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

            conn.commit(); 
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());																																		 
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e){
                System.err.println(ex.getMessage()); 
            }
        } catch (Exception ex){
            System.err.println(ex.getMessage());
        } finally {
            closeStatement(properCase);
        }
    }

    public void callRemoveAccount(){        
        CallableStatement properCase = null;
        try {
            conn.setAutoCommit(false); 

            properCase = conn.prepareCall("{ CALL remove_account( ? ) }");
        
            int id = readInt(sc, "Enter account id: ");

            properCase.setInt(1, id);

            properCase.execute();

            System.out.println("Successfully removed account"); 
            conn.commit(); 
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());																							 
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e){
                System.err.println(ex.getMessage()); 
            }
        } catch (NumberFormatException ex){
            System.err.println("Please enter a number" + ex.getMessage());
        } finally {
            closeStatement(properCase);
        }
    }

    public void callAddParticipant(){        
        CallableStatement properCase = null;
        try {
            conn.setAutoCommit(false); 
            properCase = conn.prepareCall("{ ? = CALL add_participant( ?, ?, ?, ?, ?, ?, ? ) }");
        
            int account_id = readInt(sc, "Enter account id: ");
            String first = readString(sc, "Enter first name: ", 30, false);
            String middle = readString(sc, "Enter middle name (if applicable, press enter if not): ", 30, true);
            String last = readString(sc, "Enter last name: ", 30, false);
            String country = readString(sc, "Enter birth country: ", 3, false);
            java.util.Date dob = readDate(sc, "Enter date of birth (in this format - YYYY-MM-DD): ", "yyyy-MM-dd");
            String gender = readString(sc, "Enter gender (M/F): ", 1, false);

            Boolean rReturn;

            properCase.registerOutParameter(1, Types.BIT);
            properCase.setInt(2, account_id);
            properCase.setString(3, first);
            properCase.setString(4, middle);
            properCase.setString(5, last);
            properCase.setString(6, country);
            properCase.setDate(7, new Date(dob.getTime()));
            properCase.setString(8, gender);

            properCase.execute();

            rReturn = properCase.getBoolean(1);

            if(rReturn){
                System.out.println("Participant was added successfully!");
            } else {
                System.out.println("Participant could not be created...");
            }
            conn.commit(); 

        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e){
                System.err.println(ex.getMessage()); 
            }
        } finally {
            closeStatement(properCase);
        }
    }

    public void callRemoveParticipant() {
        CallableStatement properCase = null;
        try {
            conn.setAutoCommit(false); 
            properCase = conn.prepareCall("{ CALL remove_participant( ? ) }");

            String choice1 = readString(sc, "Would you like to remove ALL or SELECTED participants? Type all or selected:", 8,
                    false);

            switch (choice1) {
                case "all":
                    String confirm = readString(sc, "Please confirm that you want to remove all participants by typing either yes or no: ", 3, false);
                    switch (confirm) {
                        case "Yes":
                            PreparedStatement st = conn.prepareStatement("DELETE FROM PARTICIPANT");
                            st.execute();
                            System.out.println("All participants removed successfully");
                            st.close();
                            break;
                        case "No":
                            System.out.println("We will not be removing participants");
                            break;
                        default:
                            break;
                    }
                    break;
                case "selected":
                    PreparedStatement st = conn.prepareStatement("SELECT PARTICIPANT_ID FROM PARTICIPANT");
                    ResultSet rs = st.executeQuery();
                    ArrayList<Integer> pids = new ArrayList<Integer>();
                    while (rs.next()) {
                        pids.add(rs.getInt("participant_id"));
                    }

                    for (Integer p : pids) {
                        System.out.println("Participant: " + p);

                        boolean validInput = false;

                        int confirm_id = 0;
                        do {
                            confirm_id = readInt(sc, "Please enter the participant id to confirm deletion or 0 to skip the current record or -1 to cancel: ");

                            if (confirm_id == p.intValue() || confirm_id == -1 || confirm_id == 0) {
                                validInput = true;
                            }
                        } while (!validInput);

                        if (confirm_id == p.intValue()) {
                            properCase.setInt(1, confirm_id);
                            properCase.execute();
                            System.out.println("Removed"); 
                        } else if (confirm_id == 0) {
                            continue;
                        } else if (confirm_id == -1) {
                            break;
                        }
                    }
                    st.close();
                    rs.close();
                    break;
                default:
                    System.out.println("Invalid remove option");
                    break;
            }

            conn.commit();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } 
    }

    public void callAddTeamMember(){        
        CallableStatement properCase = null;
        try {
            conn.setAutoCommit(false); 
            properCase = conn.prepareCall("{ ? = CALL add_team_member ( ?, ? ) }");
        
            int team_id = readInt(sc, "Enter team id: ");
            int participant_id = readInt(sc, "Enter participant id: ");
            
            Boolean rReturn;

            properCase.registerOutParameter(1, Types.BIT);
            properCase.setInt(2, team_id);
            properCase.setInt(3, participant_id);
            
            properCase.execute();

            rReturn = properCase.getBoolean(1);

            if(rReturn){
                System.out.println("Team member was added successfully!");
            } else {
                System.out.println("Team member could not be created...");
            }
            conn.commit(); 
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());																																			 
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e){
                System.err.println(ex.getMessage()); 
            }
        } finally {
            closeStatement(properCase);
        }
    }

    public void callRemoveTeamMember(){        
        CallableStatement properCase = null;
        PreparedStatement st = null; 
        int num = 0;
        
        ArrayList<Integer> valid = new ArrayList<>(); 
        try {
            conn.setAutoCommit(false); 

            int team_id = readInt(sc, "Please enter team id: ");

            st = conn.prepareStatement("SELECT * FROM olympic_schema.team_members tm WHERE tm.team = ? ORDER BY participant ASC"); 
            st.setInt(1, team_id); 

            ResultSet rs = st.executeQuery(); 

            int pos = rs.getRow(); 
            int ti, pi = 0;

            System.out.println("----------");
            System.out.println("team" + "   " + "participant");

            while(rs.next()){
                //pos = rs.getRow(); 
                ti = rs.getInt("team"); 
                pi = rs.getInt("participant");
                num++; 
                valid.add(pi);
                System.out.println(ti + "     " + pi); 
            }

            if(valid.size() != 0){
                int remove_pid = readInt(sc, "Would you like to remove a participant? Press participant_id to remove, or press -1:");

                if(valid.contains(remove_pid)){
                    String confirmation = readString(sc, "Please confirm by typing yes if you would like to remove, or no if not: ", 5, false);

                    if(confirmation.equals("Yes") || confirmation.equals("yes")){
                        properCase = conn.prepareCall("{ CALL remove_team_member ( ?, ? ) }");
                        
                        properCase.setInt(1, remove_pid);
                        properCase.setInt(2, team_id);
                        
                        properCase.execute();

                        System.out.println("Removed team member\n");
                        conn.commit(); 
                    } 
                }
                rs.close();
            } else {
                System.out.println("No participants are currently members of the Team");
            }

        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
            try {
                conn.rollback();
            } catch (SQLException e){
                System.err.println(ex.getMessage()); 
            }
        } finally {
            closeStatement(st);
            closeStatement(properCase);
        }
    }

    public void callRegisterTeam(){        
        CallableStatement properCase = null;
        try {
            conn.setAutoCommit(false); 

            properCase = conn.prepareCall("{ ? = CALL register_team ( ?, ?, ?, ?, ? ) }");
        
            String olympiad = readString(sc, "Enter olympiad: ", 30, false);
            int sport_id = readInt(sc, "Enter sport id: ");
            int coach_id = readInt(sc, "Enter coach id: ");
            String country_code = readString(sc, "Enter country code: ", 3, false);
            String gender = readString(sc, "Enter gender: (M/F/X)", 1, false);

            Boolean rReturn;

            properCase.registerOutParameter(1, Types.BIT);
            properCase.setString(2, olympiad);
            properCase.setInt(3, sport_id);
            properCase.setInt(4, coach_id);
            properCase.setString(5, country_code);
            properCase.setString(6, gender);

            properCase.execute();

            rReturn = properCase.getBoolean(1);

            if(rReturn){
                System.out.println("Team was registered successfully!");
            } else {
                System.out.println("Team could not be registered...");
            }
            conn.commit(); 

        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e){
                System.err.println(ex.getMessage()); 
            }
        } finally {
            closeStatement(properCase);
        }
    }

    public void addEvent(){        
        CallableStatement properCase = null;
        try {
            conn.setAutoCommit(false); 

            properCase = conn.prepareCall("{ CALL add_event ( ?, ?, ?, ?, ? ) }");
        
            String venue_id = readString(sc, "Enter venue id: ", 30, false);
            String olympiad_id = readString(sc, "Enter olympiad id: ", 30, false);
            int sport_id = readInt(sc, "Enter sport id: ");
            String gender = readString(sc, "Enter event gender: ", 1, false);
            Timestamp date = readTimestamp(sc, "Enter event date (yyyy-mm-dd hh:mm:ss)");                      
            
            properCase.setString(1, venue_id);
            properCase.setString(2, olympiad_id);
            properCase.setInt(3, sport_id);
            properCase.setString(4, gender);
            properCase.setTimestamp(5, date);
            
            properCase.execute();

            System.out.println("Added team to event\n");
            conn.commit(); 
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e){
                System.err.println(ex.getMessage()); 
            }
        } finally {
            closeStatement(properCase);
        }
    }

    public void addTeamToEvent(){        
        CallableStatement properCase = null;
        try {
            conn.setAutoCommit(false); 

            properCase = conn.prepareCall("{ CALL add_team_to_event ( ?, ? ) }");
        
            int event_id = readInt(sc, "Enter event id: ");
            int team_id = readInt(sc, "Enter team id: ");
            
            properCase.setInt(1, event_id);
            properCase.setInt(2, team_id);
            
            properCase.execute();

            System.out.println("Added team to event\n");
            conn.commit(); 
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e){
                System.err.println(ex.getMessage()); 
            }
        } finally {
            closeStatement(properCase);
        }
    }


    //--------------------------------------------------------------------------------------
    // need to add medal considerations and no events considerations
    public void addEventOutcome(){        
        CallableStatement properCase = null;
        CallableStatement checkEvents = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false); 

            boolean keepAdding = true;
            while(keepAdding){
                
                checkEvents = conn.prepareCall("SELECT check_num_events()");
                rs = checkEvents.executeQuery();

                boolean eventsNum = rs.getBoolean("check_event");
                if(eventsNum == false){
                    System.out.println("No Outcomes were added since there are no Events");
                }


                properCase = conn.prepareCall("CALL add_team_to_event ( ?, ?, ?) ");
            
                int event_id = readInt(sc, "Enter event id: ");

                if(event_id == -1){
                    System.out.println("Returning to main menu");
                    break;
                }
                
                int team_id = readInt(sc, "Enter team id: ");
                int position = readInt(sc, "Enter position: ");

                if(event_id == -1 || team_id == -1 || position == -1){
                    System.out.println("User provided invalid tuple. ");
                    continue;
                }else{
                    properCase.setInt(1, event_id);
                    properCase.setInt(2, team_id);
                    properCase.setInt(3, position);
                    
                    properCase.execute();
                    System.out.println("Added team to event\n");
                }
            }
            
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } finally {
            closeRS(rs);
            closeStatement(properCase);
            closeStatement(checkEvents);
        }
    }

    public void disqualifyTeam(){        

        CallableStatement properCase = null;
        try {
            conn.setAutoCommit(false); 

            properCase = conn.prepareCall("CALL disqualify_team ( ? )");

            int team_id = readInt(sc, "Enter team id: ");

            properCase.setInt(1, team_id);

            properCase.execute();

            System.out.println("Disqualifying team\n");
            conn.commit(); 
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e){
                System.err.println(ex.getMessage()); 
            }
        } finally {
            closeStatement(properCase);
        }
    }

    public void listVenuesInOlympiad(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM list_venues_in_olympiad ( ? )");
        
            String olympiad_id = readString(sc, "Enter olympiad id: ", 30, false);
            
            properCase.setString(1, olympiad_id);
            
            rs = properCase.executeQuery();
            System.out.print("Venues in olympiad " + olympiad_id + ":  \n\n\n");

            StringBuffer output = new StringBuffer("");
            output.append(rPad("Venue", 40));
            output.append(rPad("Capacity", 10)).append("\n");

            int i = 0;
            while(rs.next() ){
                String venueName = rs.getString("venue_name");
                int capacity = rs.getInt("capacity");

                output.append(rPad(venueName, 40));
                output.append(rPad(String.valueOf(capacity), 10)).append("\n");
                i++;
            }

            if (i == 0) {
                System.out.println("No venues found for olympiad " + olympiad_id + "\n");
            } else {
                System.out.println(output.toString());
            }


            System.out.println("listed venues\n");
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } finally {
            closeRS(rs); 
            closeStatement(properCase);
        }
    }

    public void listEventsOfOlympiad(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM list_events_of_olympiad ( ? )");
        
            String olympiad_id = readString(sc, "Enter olympiad id: ", 30, false);

            properCase.setString(1, olympiad_id);
            
            rs = properCase.executeQuery();
            System.out.print("Events of olympiad " + olympiad_id + ":  \n\n\n");

            StringBuffer output = new StringBuffer("");
            output.append(rPad("Event ID", 20));
            output.append(rPad("Venue", 35));
            output.append(rPad("Olympiad Num", 20));
            output.append(rPad("Sport ID", 20));
            output.append(rPad("Event Gender", 20));
            output.append(rPad("Event Date", 20)).append("\n");
           
            while(rs.next() ){
                int event_id = rs.getInt("event_id");
                String venue = rs.getString("venue");
                String olympiad_num = rs.getString("olympiad_num");
                int sport = rs.getInt("sport");
                String gender = rs.getString("gender");
                Timestamp date = rs.getTimestamp("date");

                output.append(rPad(String.valueOf(event_id), 20));
                output.append(rPad(venue, 35));
                output.append(rPad(olympiad_num, 20));
                output.append(rPad(String.valueOf(sport), 20));
                output.append(rPad(gender, 20));
                output.append(rPad(date.toString(), 20)).append("\n");
            }

            System.out.println(output.toString());

            System.out.println("Listed events in olympiad\n");
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } finally {
            closeRS(rs);
            closeStatement(properCase);
        }
    }

    public void listTeamsInEvent(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM listteamsinevent ( ? )");
        
            int event_id = readInt(sc, "Enter event id: ");

            properCase.setInt(1, event_id);
            
            rs = properCase.executeQuery();
            System.out.print("Teams in event " + event_id + ":  \n\n\n");

            StringBuffer output = new StringBuffer("");
            output.append(rPad("Team ID", 20)).append("\n");

            while(rs.next() ){
                int teamID = rs.getInt("team_id");
                output.append(rPad(String.valueOf(teamID), 10)).append("\n");
            }

            System.out.println(output.toString()); 

            System.out.println("listed venues\n");
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } finally {
            closeRS(rs);
            closeStatement(properCase);
        }
    }

    public void showPlacementsInEvent(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM show_placements_in_event( ? )");
        
            int event_id = readInt(sc, "Enter event id: ");

            properCase.setInt(1, event_id);
            
            rs = properCase.executeQuery();
            System.out.print("Placements in event " + event_id + ":  \n\n\n");

            StringBuffer output = new StringBuffer("");
            output.append(rPad("Event ID", 20));
            output.append(rPad("Team ID", 20));
            output.append(rPad("Gender", 30));
            output.append(rPad("Event Date", 10)).append("\n");

            while(rs.next() ){

                int event = rs.getInt("event");
                int team = rs.getInt("team");
                String medal = rs.getString("medal");
                int position = rs.getInt("position_id");

                output.append(rPad(String.valueOf(event), 20));
                output.append(rPad(String.valueOf(team), 20));
                output.append(rPad(medal, 30));
                output.append(rPad(String.valueOf(position), 10)).append("\n");
            }
            System.out.println(output.toString());
            System.out.println("Listed placements in event\n");
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
            ex.printStackTrace();
        } finally {
            closeRS(rs);
            closeStatement(properCase);
        }
    }

    public void listParticipantsOnTeam(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM list_participants_on_team ( ? )");
        
            int team_id = readInt(sc, "Enter team id: ");
            properCase.setInt(1, team_id);
            
            rs = properCase.executeQuery();
            System.out.print("Events of olympiad " + team_id + ":  \n\n\n");

            StringBuffer output = new StringBuffer("");
            output.append(rPad("Participant Id", 20));
            output.append(rPad("Account Id", 20));
            output.append(rPad("First Name", 30));
            output.append(rPad("Middle Name", 10));
            output.append(rPad("Last Name", 30));
            output.append(rPad("Birth Country", 20));
            output.append(rPad("Date Of Birth", 20));
            output.append(rPad("Gender", 20)).append("\n");

            int i = 0;
            while (rs.next()) {
                int participant_id = rs.getInt("participant_id");
                int account = rs.getInt("account");
                String first_name = rs.getString("first_name");
                String middle_name = rs.getString("middle_name");
                String last_name = rs.getString("last_name");
                String birthCountry = rs.getString("birth_country");
                Timestamp dob = rs.getTimestamp("dob");
                String gender = rs.getString("gender");

                output.append(rPad(String.valueOf(participant_id), 20));
                output.append(rPad(String.valueOf(account), 20));
                output.append(rPad(first_name, 30));
                output.append(rPad(middle_name, 10));
                output.append(rPad(last_name, 30));
                output.append(rPad(birthCountry, 20));
                output.append(rPad(dob.toString(), 20));
                output.append(rPad(gender, 20)).append("\n");

                i++;
            }

            if (i == 0) {
                System.out
                        .println("\n\nNo participants added to the team " + team_id + " yet\n");
            } else {
                System.out.println(output.toString());
            }

            System.out.println("Listed participants on team\n");
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println(ex.getMessage()); 
        } finally {
            closeRS(rs);
            closeStatement(properCase);
        }
    }

    public void listCountryPlacementsInOlympiad(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        int num = 0;
        StringBuilder sb = new StringBuilder();
        
        try {
            properCase = conn.prepareCall("SELECT * FROM list_country_placements_in_olympiad ( ?, ? )");
        
            String olympiad_id = readString(sc, "Enter olympiad id: ", 30, false);
            String country_code = readString(sc, "Enter country code: ", 3, false);

            properCase.setString(1, olympiad_id);
            properCase.setString(2, country_code);
            
            rs = properCase.executeQuery();
            System.out.print(country_code+ " placements in Olympiad" + olympiad_id + ":  \n\n\n");
           
            while(rs.next() ){
                num++; 

                int event = rs.getInt("event_id");
                int team = rs.getInt("team");
                String medal = rs.getString("medal");
                int position = rs.getInt("position_id");

                sb.append("Event ID is : " + event + "\n");
                sb.append("Team ID is: "+ team + "\n");
                sb.append("Event gender is: "+ medal + "\n");
                sb.append("Event date is: "+ position + "\n");
            }

            if(num == 0){
                System.out.println("No placements found.");
            } else {
                System.out.println(sb.toString());
            }

            System.out.println("Listed country placements in olympiad\n");
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } finally {
            closeRS(rs);
            closeStatement(properCase);
        }
    }

    public void callCountryRankings() {
        Statement st = null;
        try {
            st = conn.createStatement();
    
            ResultSet rs = st.executeQuery("SELECT * FROM country_rankings ORDER BY participation_rank");
    
            StringBuffer output = new StringBuffer("");
            output.append(rPad("Country Code", 20));
            output.append(rPad("Country Name", 40));
            output.append(rPad("Olympiad Count", 20));
            output.append(rPad("Participation Rank", 20)).append("\n");
    
            int i = 0;
            while (rs.next()) {
                output.append(rPad(rs.getString("country_code"), 20));
                output.append(rPad(rs.getString("country_name"), 40));
                output.append(rPad(rs.getString("olympiad_count"), 40));
                output.append(rPad(rs.getString("participation_rank"), 20)).append("\n");
    
                i++;
            }
    
            if (i == 0) {
                System.out.println("No records found for country rankings");
            } else {
                System.out.println(output.toString());
            }
            rs.close(); 
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println(ex.getMessage()); 
        } finally {
            closeStatement(st);
        }
        
    }

    //have to add event outcome first before running this 
    public void callMostSuccessfulParticipantsInOlympiad() {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("SELECT * FROM most_successful_participants_in_olympiad( ?, ?)");

            String olympiadNum = readString(sc, "Olympiad Num: ", 30, false);
            st.setString(1, olympiadNum);
            int k = readInt(sc, "k:");
            st.setInt(2, k);

            ResultSet rs = st.executeQuery();
            StringBuffer output = new StringBuffer("");
            output.append(rPad("Participant Id", 20));
            output.append(rPad("Total points", 20)).append("\n");

            int i = 0;
            while (rs.next()) {
                output.append(rPad(rs.getString("participant_id"), 20));
                output.append(rPad(rs.getString("total_points"), 20)).append("\n");

                i++;
            }

            if (i == 0) {
                System.out
                        .println("\n\nNo participants found in Olympiad - " + olympiadNum + "\n");
            } else {
                System.out.println(output.toString());
            }
            rs.close();
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println(ex.getMessage()); 
        } finally {
            closeStatement(st);
        }
    }

    public void listAthletePlacement(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        int num = 0;
        StringBuilder sb = new StringBuilder(); 
        
        try {
            properCase = conn.prepareCall("SELECT * FROM list_athlete_placement ( ? )");
        
            int participant_id = readInt(sc, "Enter participant id: "); 
            
            properCase.setInt(1, participant_id);
            
            rs = properCase.executeQuery();
            System.out.print("Placements in event " + participant_id + ":  \n\n\n");
        
            while(rs.next() ){
                num++; 

                int event = rs.getInt("event_id");
                int team = rs.getInt("team");
                String medal = rs.getString("medal");
                int position = rs.getInt("position_id");

                sb.append("Event ID is : " + event + "\n");
                sb.append("Team ID is: "+ team + "\n");
                sb.append("Event medal is: "+ medal + "\n");
                sb.append("Event position is: "+ position + "\n");
            }

            if(num == 0){
                System.out.println("No placements found.");
            } else {
                System.out.println(sb.toString());
            }

            System.out.println("Listed athlete placements in olympics\n");
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } finally {
            closeRS(rs);
            closeStatement(properCase);
        }
    }

    public void topSports(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * top_sports ( ?, ?)");
        
            int x = readInt(sc, "Enter the number of years to search (x): ");
            int k = readInt(sc, "Enter the number of teams to display (k): ");
            
            properCase.setInt(1, x);
            properCase.setInt(2, k);
            
            rs = properCase.executeQuery();
            System.out.print("Top " + k + "sports in last " + x + " years:  \n\n\n");
           
            /*
             * sport_id   INTEGER,
            sport_name VARCHAR(30),
            team_count BIGINT

             */
            while(rs.next() ){

                int sportID = rs.getInt("sport_id");
                String sportName = rs.getString("sport_name");
                int teamCount = rs.getInt("team_count");

                System.out.println("Sport ID is: " + sportID);
                System.out.println("Sport Name is: "+ sportName);
                System.out.println("Number of Teams is: "+ teamCount); 
                System.out.println();
            }

            System.out.println("Listed top sports in olympics\n");
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } finally {
            closeRS(rs);
            closeStatement(properCase);
        }
    }

    public void connectedCoaches(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM connected_coaches ( ?, ?)");

            int c1 = readInt(sc, "Enter the first coach's ID (c1): ");

            int c2 = readInt(sc, "Enter the second coach's ID (c2): ");

            properCase.setInt(1, c1);
            properCase.setInt(2, c2);
            
            rs = properCase.executeQuery();
            while(rs.next()){
                String connection = rs.getString("connect_string");
                System.out.println("Coach connection: " + connection);
            }
            
            System.out.println("Listed coach connections in olympics\n");
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } finally {
            closeRS(rs);
            closeStatement(properCase);
        }
    }

    public void exit(){
        if (conn != null) {
            try {
                conn.close();
                sc.close();
                br.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e){
                System.out.println("IOException e");
            }
        }
    }
    
}
