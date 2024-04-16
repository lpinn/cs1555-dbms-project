import java.util.NoSuchElementException;
import java.util.Scanner;


/*
 * databaseConnectionConfig and helper methods originally writteb by Brian Nixon (TA CS 1555)
 */
public class OlympicDB{
    public void main(String args[]){
        System.out.println("Welcome to OlympicDB!");

    }
    private String user = "postgres";
    private String pass = "";
    private static final String USER_REQUEST_STR = "Enter the username for connecting to the database: ";
    private static final String PASS_REQUEST_STR = "Enter the password for connecting to the database: ";

    public void DatabaseConnectionConfig() {
        // Note scanner is passed as an arg to close after
        // If scanner is closed in the method, System.in is also closed causing the second method call to fail
        // *Fun* little java quirks
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
        // If username is null, don't update the current value
        if (username != null)
            this.user = username;
    }

    private void requestPassword(Scanner scanner ) {
        String pw = requestUserInput(PASS_REQUEST_STR, scanner);
        // If password is null, don't update the current value
        if (pw != null)
            this.pass = pw;
    }

}
