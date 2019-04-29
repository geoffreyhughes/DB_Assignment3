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
            stmt.execute("CREATE TABLE IF NOT EXISTS Books(bookID int PRIMARY KEY NOT NULL IDENTITY, authorID int NOT NULL IDENTITY, titleID int);");
            stmt.execute("CREATE TABLE IF NOT EXISTS Authors(authorID int PRIMARY KEY NOT NULL IDENTITY, firstNameID int NOT NULL IDENTITY, lastNameID int NOT NULL IDENTITY);");
            stmt.execute("CREATE TABLE IF NOT EXISTS FirstNames(firstNameID int PRIMARY KEY NOT NULL IDENTITY, firstName varchar(25));");
            stmt.execute("CREATE TABLE IF NOT EXISTS LastNames(lastNameID int PRIMARY KEY NOT NULL IDENTITY, lastName varchar(25));");
            stmt.execute("CREATE TABLE IF NOT EXISTS Titles(titleID int PRIMARY KEY NOT NULL IDENTITY, title varchar(35));");


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

                java.sql.PreparedStatement insertFirstName = conn.prepareStatement("INSERT INTO FirstNames" +
                        "(firstName) VALUE (?)");

                insertFirstName.setString(1, first_name);

                java.sql.PreparedStatement insertLastName = conn.prepareStatement("INSERT INTO FirstNames" +
                        "(firstName) VALUE (?)");

                insertLastName.setString(1, last_name);

                PreparedStatement insertTitle = conn.prepareStatement("INSERT INTO FirstNames" +
                        "(firstName) VALUE (?)");

                insertTitle.setString(1, title);

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


