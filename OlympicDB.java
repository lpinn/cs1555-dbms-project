import java.util.Properties;
import java.util.ArrayList;

import java.sql.*;
import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;

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
        OlympicDB od = new OlympicDB(br, "postgres", "Tr@um@-$ql"); 

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
                        od.addEvent();
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
                    System.out.println("Displaying country rankings...");
                    break;

                case "21":
                    System.out.println("Listing most successful participants in an Olympiad...");
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

    public void callCreateAccount() {        
        CallableStatement properCase = null;
        try {
            properCase = conn.prepareCall("{ ? = CALL create_account( ?, ?, ? ) }");
        
            System.out.print("Enter username: ");
            String username = br.readLine();
            System.out.print("Enter password: ");
            String password = br.readLine();
            System.out.print("Enter role: ");
            String role = br.readLine();

            if(username.length() == 0 || password.length() == 0 || role.length() == 0){
                throw new Exception("Input for one or more variables was empty, please input actual information.");
            }

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
											   
																																												 
        } catch (SQLException ex) {
            System.err.println(ex.getMessage()); 
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception ex){
            System.err.println(ex.getMessage());
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
											   
																																												 
        } catch (SQLException ex) {
            System.err.println(ex.getMessage()); 
        } catch (IOException ex) {
            System.err.println("IO Exception E" + ex.getMessage());
        } catch (NumberFormatException ex){
            System.err.println("Please enter a number" + ex.getMessage());
        }
    }

    public void callAddParticipant(){        
        CallableStatement properCase = null;
        try {
            properCase = conn.prepareCall("{ ? = CALL add_participant( ?, ?, ?, ?, ?, ?, ? ) }");
        
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date dob = sdf.parse(t);

            System.out.print("Enter gender (M/F): ");
            String gender = br.readLine();

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
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("Illegal Argument Exception E " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println(ex.getMessage()); 
        } catch (IOException ex) {
            System.err.println("IO Exception E " + ex.getMessage());
        } catch (ParseException ex) {
            System.err.println("Parse Exception E " + ex.getMessage());
        }
    }

    public void callRemoveParticipant(){        
        CallableStatement properCase = null;
        try {
            properCase = conn.prepareCall("{ CALL remove_participant( ? ) }");
        
            System.out.print("Enter participant id: ");
            int id = Integer.parseInt(br.readLine());

            properCase.setInt(1, id);

            properCase.execute();

            System.out.println("Removed participant"); 

        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
											   
																																												 
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } catch (IOException ex) {
            System.err.println("IO Exception E" + ex.getMessage());
        }
    }

    public void callAddTeamMember(){        
        CallableStatement properCase = null;
        try {
            properCase = conn.prepareCall("{ ? = CALL add_team_member ( ?, ? ) }");
        
            System.out.print("Enter team id: ");
            int team_id = Integer.parseInt(br.readLine());
            System.out.print("Enter participant id: ");
            int participant_id = Integer.parseInt(br.readLine());
            
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

        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
											   
																																												 
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } catch (IOException ex) {
            System.err.println("IO Exception E" + ex.getMessage());
        }
    }

    public void callRemoveTeamMember(){        
        CallableStatement properCase = null;
        PreparedStatement st = null; 
        int num = 0;
        
        ArrayList<Integer> valid = new ArrayList<>(); 
        try {
            System.out.print("Please enter a team_id: "); 
            int team_id = Integer.parseInt(br.readLine());

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
                System.out.println("Would you like to remove a participant? Press participant_id to remove, or press -1:"); 
                int remove_pid = Integer.parseInt(br.readLine());

                if(valid.contains(remove_pid)){
                    System.out.println("Please confirm by typing yes if you would like to remove, or no if not: "); 
                    String confirmation = (br.readLine());

                    if(confirmation.equals("Yes") || confirmation.equals("yes")){
                        properCase = conn.prepareCall("{ CALL remove_team_member ( ?, ? ) }");
                        
                        properCase.setInt(1, remove_pid);
                        properCase.setInt(2, team_id);
                        
                        properCase.execute();

                        System.out.println("Removed team member\n");
                    } 
                }
            } else {
                System.out.println("No participants are currently members of the Team");
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

    public void callRegisterTeam(){        
        CallableStatement properCase = null;
        try {
            properCase = conn.prepareCall("{ ? = CALL register_team ( ?, ?, ?, ?, ? ) }");
        
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

    public void addEvent(){        
        CallableStatement properCase = null;
        try {
            properCase = conn.prepareCall("{ CALL add_event ( ?, ?, ?, ?, ? ) }");
        
            System.out.println("Enter venue id: ");
            int venue_id = Integer.parseInt(br.readLine());
            System.out.println("Enter olympiad id: ");
            int olympiad_id = Integer.parseInt(br.readLine());
            System.out.println("Enter sport id: ");
            int sport_id = Integer.parseInt(br.readLine());
            System.out.println("Enter event gender: ");
            String gender = br.readLine();
            System.out.println("Enter event date (yyyy-mm-dd hh:mm:ss)");
            Timestamp date = Timestamp.valueOf(br.readLine());
            
            properCase.setInt(1, venue_id);
            properCase.setInt(2, olympiad_id);
            properCase.setInt(3, sport_id);
            properCase.setString(4, gender);
            properCase.setTimestamp(5, date);
            
            properCase.execute();

            System.out.println("Added team to event\n");
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

    public void addTeamToEvent(){        
        CallableStatement properCase = null;
        try {
            properCase = conn.prepareCall("{ CALL add_team_to_event ( ?, ? ) }");
        
            System.out.println("Enter event id: ");
            int event_id = Integer.parseInt(br.readLine());
            System.out.println("Enter team id: ");
            int team_id = Integer.parseInt(br.readLine());
            
            
            properCase.setInt(1, event_id);
            properCase.setInt(2, team_id);
            
            properCase.execute();

            System.out.println("Added team to event\n");
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


    //--------------------------------------------------------------------------------------
    // need to add medal considerations and no events considerations
    public void addEventOutcome(){        
        CallableStatement properCase = null;
        CallableStatement checkEvents = null;
        ResultSet rs = null;
        try {
            boolean keepAdding = true;
            while(keepAdding){
                
                checkEvents = conn.prepareCall("{SELECT * FROM check_num_events()}");
                rs = checkEvents.executeQuery();

                boolean eventsNum = rs.getBoolean("check_event");
                if(eventsNum == false){
                    System.out.println("No Outcomes were added since there are no Events");
                }


                properCase = conn.prepareCall("{ CALL add_team_to_event ( ?, ?, ?) }");
            
                System.out.println("Enter event id: ");
                int event_id = Integer.parseInt(br.readLine());

                if(event_id == -1){
                    System.out.println("Returning to main menu");
                    break;
                }
                System.out.println("Enter team id: ");
                int team_id = Integer.parseInt(br.readLine());
                System.out.println("Enter position: ");
                int position = Integer.parseInt(br.readLine());

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
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } catch (IOException ex) {
            System.err.println("IO Exception E" + ex.getMessage());
        }
    }

    public void disqualifyTeam(){        
        CallableStatement properCase = null;
        try {
            properCase = conn.prepareCall("{ CALL disqualify_team ( ?) }");
        
        
            System.out.println("Enter team id: ");
            int team_id = Integer.parseInt(br.readLine());
            
            properCase.setInt(1, team_id);
            
            properCase.execute();

            System.out.println("Disqualifying team\n");
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

    public void listVenuesInOlympiad(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM list_venues_in_olympiad ( ? )");
        
        
            System.out.println("Enter olympiad id: ");
            int olympiad_id = Integer.parseInt(br.readLine());
            
            properCase.setInt(1, olympiad_id);
            
            rs = properCase.executeQuery();
            System.out.print("Venues in olympiad " + olympiad_id + ":  \n\n\n");
            while(rs.next() ){
                String venueName = rs.getString("venue_name");
                int capacity = rs.getInt("capacity");

                System.out.println("Venue name is : " + venueName);
                System.out.println("Venue capacity is : "+ capacity);
                System.out.println();
            }

            System.out.println("listed venues\n");
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

    public void listEventsOfOlympiad(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM list_events_of_olympiad ( ?)");
        
        
            System.out.println("Enter olympiad id: ");
            int olympiad_id = Integer.parseInt(br.readLine());
            
            properCase.setInt(1, olympiad_id);
            
            rs = properCase.executeQuery();
            System.out.print("Events of olympiad " + olympiad_id + ":  \n\n\n");
           
        
            while(rs.next() ){
                int event_id = rs.getInt("event_id");
                String venue = rs.getString("venue");
                String olympiad_num = rs.getString("olympiad_num");
                int sport = rs.getInt("sport");
                String gender = rs.getString("gender");
                Timestamp date = rs.getTimestamp("date");

                System.out.println("Event ID is : " + event_id);
                System.out.println("Venue name is : "+ venue);
                System.out.println("Olympiad number is: "+ olympiad_num);
                System.out.println("Sport ID is: "+ sport);
                System.out.println("Event gender is: "+ gender);
                System.out.println("Event date is: "+ date.toString());
                System.out.println();
            }

            System.out.println("Listed events in olympiad\n");
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

    public void listTeamsInEvent(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM list_teams_in_event ( ?) ");
        
        
            System.out.println("Enter event id: ");
            int event_id = Integer.parseInt(br.readLine());
            
            properCase.setInt(1, event_id);
            
            rs = properCase.executeQuery();
            System.out.print("Teams in event " + event_id + ":  \n\n\n");
            while(rs.next() ){
                int teamID = rs.getInt("team_id");


                System.out.println("Team ID is : "+ teamID);
                System.out.println();
            }

            System.out.println("listed venues\n");
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

    public void showPlacementsInEvent(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM show_placements_in_event ( ?)");
        
            System.out.println("Enter event id: ");
            int event_id = Integer.parseInt(br.readLine());
            
            properCase.setInt(1, event_id);
            
            rs = properCase.executeQuery();
            System.out.print("Placements in event " + event_id + ":  \n\n\n");
           
        
            while(rs.next() ){

                int event = rs.getInt("event");
                int team = rs.getInt("team");
                String medal = rs.getString("medal");
                int position = rs.getInt("position_id");

                System.out.println("Event ID is : " + event);
                System.out.println("Team ID is: "+ team);
                System.out.println("Event medal is: "+ medal);
                System.out.println("Event date is: "+ position);
                System.out.println();
            }

            System.out.println("Listed placements in event\n");
        } catch (NoSuchElementException ex) {
            System.err.println("No lines were read from user input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } catch (IOException ex) {
            System.err.println("IO Exception E" + ex.getMessage());
        }
    }

    public void listParticipantsOnTeam(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM list_participants_on_team ( ? )");
        
        
            System.out.println("Enter team id: ");
            int team_id = Integer.parseInt(br.readLine());
            
            properCase.setInt(1, team_id);
            
            rs = properCase.executeQuery();
            System.out.print("Events of olympiad " + team_id + ":  \n\n\n");
           /*
            *         participant_id INTEGER,
        account INTEGER,
        first_name VARCHAR(30),
        middle_name VARCHAR(30),
        last_name VARCHAR(30),
        birth_country CHAR(3),
        dob TIMESTAMP,
        gender olympic_schema.participant_gender_check
            */
        
            while(rs.next() ){
                int participant_id = rs.getInt("participant_id");
                int account = rs.getInt("account");
                String first_name = rs.getString("first_name");
                String middle_name = rs.getString("middle_name");
                String last_name = rs.getString("last_name");
                String birthCountry = rs.getString("birth_country");
                Timestamp dob = rs.getTimestamp("dob");
                String gender = rs.getString("gender");
               

                System.out.println("Participant ID is : " + participant_id);
                System.out.println("Account is : " + account);
                System.out.println("First name is : "+ first_name);
                System.out.println("Middle name is : "+ middle_name);
                System.out.println("Last name is : "+ last_name);
                System.out.println("Birth Country is: "+ birthCountry);
                System.out.println("Event date is: "+ dob.toString());
                System.out.println("Event gender is: "+ gender);
                System.out.println();
            }

            System.out.println("Listed participants on team\n");
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

    public void listCountryPlacementsInOlympiad(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        int num = 0;
        StringBuilder sb = new StringBuilder();
        
        try {
            properCase = conn.prepareCall("SELECT * FROM list_country_placements_in_olympiad ( ?, ? )");
        
        
            System.out.println("Enter olympiad id: ");
            String olympiad_id = (br.readLine());
            System.out.println("Enter country code: ");
            String country_code = br.readLine();
            

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
        } catch (IOException ex) {
            System.err.println("IO Exception E" + ex.getMessage());
        }
    }
    public void listAthletePlacement(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        int num = 0;
        StringBuilder sb = new StringBuilder(); 
        
        try {
            properCase = conn.prepareCall("SELECT * FROM list_athlete_placement ( ? )");
        
        
            System.out.println("Enter participant id: ");
            int participant_id = Integer.parseInt(br.readLine());
            
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
        } catch (IOException ex) {
            System.err.println("IO Exception E" + ex.getMessage());
        }
    }

    public void topSports(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM top_sports ( ?, ?) ");
        
        
            System.out.println("Enter the number of years to search (x): ");
            int x = Integer.parseInt(br.readLine());

            System.out.println("Enter the number of teams to display (k): ");
            int k = Integer.parseInt(br.readLine());
            
            properCase.setInt(1, x);
            properCase.setInt(2, k);
            
            rs = properCase.executeQuery();
            System.out.print("Top " + k + " sports in last " + x + " years:  \n\n\n");
           
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
        } catch (IllegalArgumentException ex) {
            System.err.println("The scanner was likely closed before reading the user's input, please try again " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQL Exception E " + ex.getMessage()); 
        } catch (IOException ex) {
            System.err.println("IO Exception E" + ex.getMessage());
        }
    }

    public void connectedCoaches(){        
        CallableStatement properCase = null;
        ResultSet rs = null;
        
        try {
            properCase = conn.prepareCall("SELECT * FROM connected_coaches( ?, ?) ");
        
        
            System.out.println("Enter the first coach's ID (c1): ");
            int c1 = Integer.parseInt(br.readLine());

            System.out.println("Enter the second coach's ID (c2): ");
            int c2 = Integer.parseInt(br.readLine());
            
            properCase.setInt(1, c1);
            properCase.setInt(2, c2);
            
            
            rs = properCase.executeQuery();
            while(rs.next() ){
                String connection = rs.getString("connect_string");
                System.out.println("Coach connection: " + connection);
            
            }

            System.out.println("Listed top sports in olympics\n");
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
