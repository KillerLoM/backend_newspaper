package com.example.backend.MenuBar;
import com.example.backend.DataBaseService.DbFunction;
import com.example.backend.Newspapers.category;
import jdk.jfr.Category;
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
        Connection conn = db.connect_to_db("Db_Server","postgres","123456789");
        String src ="https://tuoitre.vn/";
        Document webPage = Jsoup.connect(src).get();
        Elements categories = webPage.select("div.header__nav div.container div.header__nav-flex ul.menu-nav li");
        for(Element category: categories) {
            String title = category.select("a").attr("title");
            db.insert_category(conn,"table_category",title);
        }
        conn.close();
    }
    public static LinkedList<category> getMenuBar() {
        DbFunction db = new DbFunction();
        Connection conn = db.connect_to_db("Db_Server", "postgres", "123456789");
        LinkedList<category> linkedList = new LinkedList<>();
        Statement statement;
        ResultSet rs = null;
        try {
            String query = String.format("select * from table_category");
            statement = conn.createStatement();
            category Category = new category();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                Category = category.build(
                        rs.getInt("id"),
                        rs.getString("category")
                );
                linkedList.add(Category);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return linkedList;
    }
}
