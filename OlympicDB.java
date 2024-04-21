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
    private String menuOptions; 

    private BufferedReader br;

    public OlympicDB(BufferedReader br){
        this.br = br; 
    }   

    public static void main(String[] args){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        OlympicDB od = new OlympicDB(br); 

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
                choice = br.readLine();
            } catch (IOException e){
                System.out.println("Issue with capturing input"); 
            }
            
            switch (choice) {
                case "1":
                    System.out.println("You are connecting to the system!"); 
                    break;

                case "2":
                    System.out.println("Creating a new account!");
                    break;

                case "3":
                    System.out.println("Removing an account...");
                    break;

                case "4":
                    System.out.println("Adding a new participant!");
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

    
}
