/*
    Geoffrey Hughes
    002306123
    ghughes@chapman.edu
    Prof. Rene German
    CPSC 408 - Database Management
    Spring 2019
    Assignment 3

    This class connects to the Google Cloud server with the appropriate permissions,
        reads in a CSV file containing tuples, and inserts those tuples into their appropriate tables
        within the database. Run the python CSV file generator before running this code.
 */


package com.geoffreyhughes;
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class DB {

    public Connection conn = null;
    private String mySQLdriver = "com.mysql.cj.jdbc.Driver";
    //You gonna need to delete the driver and reload it java:6.0.6 so its without the cj

    //private String mySQLconnection = "jdbc:mysql://206.211.151.0/24:3306/assignment3";

    //private String mySQLconnection = "jdbc:mysql://206.211.151.23:3306/assignment3?useSSL=false";

    private String mySQLconnection = "jdbc:mysql://35.203.181.112:3306/assignment3?useSSL=false";




    private String mySQLusername = "user";
    private String mySQLpassword = "chapman";


    // DB Constructor - also executes all of the functions to upload the CSV tuples to the Google Cloud
    public DB() {

        conn = null;
        connectDB();
        createTablesDDL();
        readCSVtoTablesDML(promptCSV());
        System.out.println("Upload complete.");
    }


    // Connects to my Google Cloud server using the url, username, and password
    private void connectDB()
    {
        try {
            System.out.println("ATTEMPTING TO ESTABLISH A CONNECTION...");
            Class.forName(mySQLdriver);
            conn = DriverManager.getConnection(mySQLconnection, mySQLusername, mySQLpassword);
            System.out.println("CONNECTED TO DB!");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR CONNECTING TO DB");
        }
    }


    // Creates the 5 tables used in the MySQL database in Google Cloud
    private void createTablesDDL() {

        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            //stmt.execute("DROP TABLE Books");

            stmt.execute("CREATE TABLE IF NOT EXISTS Users(UserID int NOT NULL AUTO_INCREMENT, Username varchar(40) NOT NULL, BookID int, PRIMARY KEY (UserID))");

            stmt.execute("CREATE TABLE IF NOT EXISTS Books(BookID int NOT NULL AUTO_INCREMENT, Title varchar(40) NOT NULL, AuthorID int NOT NULL, GenreID int NOT NULL, PRIMARY KEY (BookID))");

            stmt.execute("CREATE TABLE IF NOT EXISTS Authors(AuthorID int NOT NULL AUTO_INCREMENT, FirstName varchar(25) NOT NULL, LastName varchar(25), Age int NOT NULL, NativeLanguage varchar(20), PRIMARY KEY (AuthorID))");

            stmt.execute("CREATE TABLE IF NOT EXISTS Genres(GenreID int NOT NULL AUTO_INCREMENT, Genre varchar(30) NOT NULL, AboutGenre varchar(50), PRIMARY KEY (GenreID))");

            stmt.execute("CREATE TABLE IF NOT EXISTS Publishers(PublisherID int NOT NULL AUTO_INCREMENT, Publisher varchar(30) NOT NULL, YearEstablished int, PRIMARY KEY (PublisherID))");

            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // Reads in from a CSV file and executes SQL to INSERT the CSV data appropriately
    private void readCSVtoTablesDML(String csvFile)
    {
        String first_name;
        String last_name;
        String title;

        try {
            Scanner scan = new Scanner(new File(csvFile));
            scan.useDelimiter(",");
            while(scan.hasNext())
            {
                first_name = scan.next();
                last_name = scan.next();
                title = scan.next();


                java.sql.PreparedStatement insertBook = conn.prepareStatement("INSERT INTO Books," +
                                "(BookID, FirstName, LastName, Age, NativeLangauge) VALUES (?, ?, ?, ?, ?)");

                insertBook.setString(2, first_name);
                insertBook.setString(3, last_name);
                insertBook.setString(4, "5");
                insertBook.setString(5, title);


                /*
                java.sql.PreparedStatement insertLastName = conn.prepareStatement("INSERT INTO LastNames" +
                        "(last_name) VALUE (?)");

                insertLastName.setString(1, last_name);

                PreparedStatement insertTitle = conn.prepareStatement("INSERT INTO Titles" +
                        "(title) VALUE (?)");

                insertTitle.setString(1, title);
                */

            }

            scan.close();

        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    // Prompts the user for a CSV file with which to extract data for use in readCSVtoTablesDML(String csvFile)
    private String promptCSV()
    {
        System.out.println("Please enter the entire csv file name (with extension): ");


        Scanner input = new Scanner(System.in);
        String csv = input.nextLine();
        System.out.println("You entered file " + csv);

        return csv;
    }


}


