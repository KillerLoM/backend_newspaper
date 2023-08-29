package com.example.backend.DataBaseService;
import com.example.backend.ErrorHandle.ErrorLogger;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;
public class DbFunction {
    public Connection connect_to_db(String dbName, String user, String pwd){
        Connection conn = null;
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbName, user, pwd);
            if(conn!=null){
                System.out.println("Connection to Database Established");
            }
            else System.out.println("Connection Failed");
        }catch (Exception e) {
            ErrorLogger.logError(e);
        }
        return conn;
    }
    public void createTable(Connection conn, String table_name){
        Statement statement;
        try{
            String query = "create table " + table_name + "(code varchar(100), category varchar(200), link text,heading text, description text, img text, day int, month int, year int,hour int, minute int, second int, created_at timestamp,  primary key(code, category));";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");
        }catch (Exception e) {
            ErrorLogger.logError(e);
        }
    }
    public void createTableData(Connection conn, String table_name){
        Statement statement;
        try{
            String query = "create table " + table_name + "(code varchar(100), time text, category varchar(200), heading text, subHeading text, content text,  primary key(code, category));";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");
        }catch (Exception e) {
            ErrorLogger.logError(e);
            System.out.println(e);
        }
    }
    public void insertNewspapers(Connection conn, String table_name, String code, String time, String category, String heading, String subHeading, String content){
        PreparedStatement statement = null;
        try {

                String query = "INSERT INTO " + table_name + "(code, category,time,heading, subHeading,content) VALUES (?, ?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(query);
                statement.setString(1, code);
                statement.setString(2, category);
                statement.setString(3, time);
                statement.setString(4,heading);
                statement.setString(5, subHeading);
                statement.setString(6, content);
                statement.executeUpdate();
                System.out.println("Row Inserted");
        } catch (Exception e) {
            ErrorLogger.logError(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    ErrorLogger.logError(e);
                }
            }
        }
    }
    public boolean check_by_category(Connection conn, String table_name, String category) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = String.format("select * from %s where category = '%s'  ", table_name,category);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            return !rs.next();
        }
        catch (SQLException e) {
            ErrorLogger.logError(e);
            throw new RuntimeException(e);
        }
    }
    public void insert_category(Connection conn, String table_name, String category){
        Statement statement;
        try {
            if (check_by_category(conn, table_name, category)) {
                String query = "INSERT INTO " + table_name + "(category) VALUES (?)";
                PreparedStatement statement1 = conn.prepareStatement(query);
                statement1.setString(1, category);
                statement1.executeUpdate();
                System.out.println("Row Inserted");
            } else {
            }
        } catch (Exception e) {
            ErrorLogger.logError(e);
        }
    }
    public void insert_link(Connection conn, String table_name, String code, String category, String link,String heading, String description, String img, int day, int month, int year, int hour, int minute, int second, Timestamp created_at){
        PreparedStatement statement = null;
        try {
            if (check_by_id(conn, table_name, code)) {
                String query = "INSERT INTO " + table_name + "(code, category,link,heading, description, img, day, month, year, hour, minute, second, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(query);
                statement.setString(1, code);
                statement.setString(2, category);
                statement.setString(3, link);
                statement.setString(4,heading);
                statement.setString(5, description);
                statement.setString(6, img);
                statement.setInt(7, day);
                statement.setInt(8, month);
                statement.setInt(9, year);
                statement.setInt(10, hour);
                statement.setInt(11, minute);
                statement.setInt(12, second);
                statement.setTimestamp(13, created_at);
                statement.executeUpdate();
                System.out.println("Row Inserted");
            } else {

            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    ErrorLogger.logError(e);
                }
            }
        }
    }

    public boolean check_by_id(Connection conn, String table_name, String code){
        Statement statement;
        ResultSet rs ;
        try{
            String query = String.format("select * from %s where code = '%s'  ",table_name,code);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            return !rs.next();
        } catch (SQLException e) {
            ErrorLogger.logError(e);
            throw new RuntimeException(e);
        }
    }
//    public String readLink(Connection conn, String table_name){
//        Statement statement;
//        ResultSet rs = null;
//        try{
//            String query = String.format("select * from %s ", table_name);
//            statement = conn.createStatement();
//            rs = statement.executeQuery(query);
//            while(rs.next()){
//                return rs.getString("link");
//            }
//        }catch(Exception e){
//            System.out.println(e);
//        }
//        return table_name;
//    }
public void createTableCategory(Connection conn, String table_name) {
    Statement statement;
    try {
        String query = "create table " + table_name + "(id serial primary key, category varchar(200));";
        statement = conn.createStatement();
        statement.executeUpdate(query);
        System.out.println("Table Created");
    } catch (Exception e) {
        System.out.println(e);
    }
}
    public void delete_row_by_code(Connection conn, String table_name, String code){
        Statement statement;
        try{
            String query = String.format("delete from %s where code = '%s'", table_name,code);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("This newspaper can not be handle so it has to be deleted");
        }
        catch(Exception e){
            ErrorLogger.logError(e);
        }
    }
}
