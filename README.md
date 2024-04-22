# cs1555-dbms-project
### Leela Pinnamaraju, Sarah Gould (aspiringBaristas)

### Phases 2, 3, and 4

## Instructions for using the OlympicDB Java Application:

1. First ensure JDBC Driver is loaded properly.
2. Compile and run client using the CLI.
    ```
    javac -cp "postgresql-42.7.3.jar;." OlympicDB.java
    java -cp "postgresql-42.7.3.jar;." OlympicDB      
    ```
3. After you enter the program, you will be presented with a menu of operations: 
    ```
        --------------------------------------------------
        Welcome to the Olympic DB, how may we help?
        --------------------------------------------------
        1. connect to db 
        2. create account 
        3. remove account 
        4. add participant 
        5. remove participant 
        6. add team member 
        7. remove team member 
        8. register team
        9. add event
        10. add team event
        11. add event outcome
        12. disqualify team
        13. list venues in olympiad
        14. list events of olympiad
        15. list teams in event
        16. show placements in event
        17. list participants on team
        18. list country placements in olympiad
        19. list athlete placement
        20. country rankings
        21. most successful participants in olympiad
        22. top sports
        23. connected coaches
        24. exit
        --------------------------------------------------
    
    ```
4.  To run operations 2-23 you must first connect to the DB by pressing 1. 
    You will then enter your username and password for postgres.
    ```    
        You are connecting to the system!
        Enter username: postgres
        Enter password: Tr@um@-$ql
        Connected to the database successfully.
    ```

5.  You may now enter the various commands to access the operations. 
