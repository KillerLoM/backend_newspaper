package com.example.backend.MenuBar;
import com.example.backend.DataBaseService.DbFunction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class GetCategory {
    public static void getCategory() throws IOException, SQLException {
        DbFunction db = new DbFunction();
        Connection conn = db.connect_to_db("New_Db","postgres","123456789");
        String src ="https://tuoitre.vn/";
        Document webPage = Jsoup.connect(src).get();
        Elements categories = webPage.select("div.header__nav div.container div.header__nav-flex ul.menu-nav li");
        for(Element category: categories) {
            String title = category.select("a").attr("title");
            db.insert_category(conn,"data_categories",title);
        }
        conn.close();
    }
    public static LinkedList getMenuBar(){
        DbFunction db = new DbFunction();
        Connection conn = db.connect_to_db("New_Db","postgres","123456789");
        LinkedList linkedList = new LinkedList();
        Statement statement ;
        ResultSet rs = null;
        try{
            String query = String.format("select * from data_categories");
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while(rs.next()){
                linkedList.add(rs.getString("category"));
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return linkedList;
    }

}
