package com.example.backend.Newspaper;
import com.example.backend.DataBaseService.DbFunction;
import com.example.backend.Newspapers.newspaper;
import com.example.backend.ErrorHandle.ErrorLogger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetNewspaper {

    public static void readData() {
        String HTML = "";
        DbFunction db = new DbFunction();
        Connection conn = db.connect_to_db("New_Db", "postgres", "123456789");
        Statement statement;
        ResultSet rs = null;
        if (rs!=null){
            try {
                String query = String.format("select * from %s order by hour desc, minute desc, second desc, day desc, month desc, year desc", "data_links");
                statement = conn.createStatement();
                rs = statement.executeQuery(query);
                if (rs != null) {
                    Objects.requireNonNull(rs);
                }
                while (rs.next()) {
                    String src = "https://tuoitre.vn/";
                    Document webPage = Jsoup.connect(src + rs.getString("link")).get();
                    Element content = webPage.selectFirst("div .detail-cmain");
                    Element timeCode = webPage.selectFirst("div .detail__section div .detail-time ");
                    Elements link = webPage.select("div[class*=VCSortableInPreviewMode]");
                    String type = (Objects.requireNonNull(webPage.selectFirst("div[class *=detail__] div.detail-top div.detail-cate a"))).text();
                    link.remove();
                    Elements a = webPage.select("a[href*=/]");
                    for (Element links : a) {
                        Element p = new Element("p");
                        TextNode textNode = new TextNode(links.text());
                        links.replaceWith(textNode);
                    }
                    HTML = "------------------------------" + content.toString() + timeCode.toString();
                    String check = "<div type=\"RelatedOneNews\" class=\"VCSortableInPreviewMode\">.*?</div>";
                    Pattern pattern = Pattern.compile(check, Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(HTML);
                    HTML = matcher.replaceAll("");
                    db.insertNewspapers(conn, "data_newspapers", rs.getString("code"), timeCode.toString(), type, rs.getString("heading"), rs.getString("description"), HTML);
                }
            }catch (Exception e) {
                ErrorLogger.logError(e);
                System.out.println(e);
            }
        }
    }

    public static List<newspaper> getNewspapers_by_category(String type) {
        LinkedList linkedList = new LinkedList();
        Statement statement;
        ResultSet rs = null;
        DbFunction db = new DbFunction();
        Connection conn = db.connect_to_db("New_Db", "postgres", "123456789");
        try {

            if ( Objects.equals(type, "Trang chủ") ) {
                String query1 = String.format("select * from data_links ");
                statement = conn.createStatement();
                rs = statement.executeQuery(query1);
                while (rs.next()) {
                    newspaper Newspapers = newspaper.build(
                            rs.getString("code"),
                            rs.getString("category"),
                            rs.getInt("hour"),
                            rs.getInt("minute"),
                            rs.getInt("second"),
                            rs.getInt("day"),
                            rs.getInt("month"),
                            rs.getInt("year"),
                            rs.getString("heading"),
                            rs.getString("description"),
                            rs.getString("img")
                    );
                    linkedList.add(Newspapers);
                }
            } else if (!Objects.equals(type, "Trang chủ")) {
                String query = String.format("select * from data_links where category = '%s'", type);
                statement = conn.createStatement();
                rs = statement.executeQuery(query);
                while (rs.next()) {
                    newspaper Newspapers = newspaper.build(
                            rs.getString("code"),
                            rs.getString("category"),
                            rs.getInt("hour"),
                            rs.getInt("minute"),
                            rs.getInt("second"),
                            rs.getInt("day"),
                            rs.getInt("month"),
                            rs.getInt("year"),
                            rs.getString("heading"),
                            rs.getString("description"),
                            rs.getString("img")
                    );
                    linkedList.add(Newspapers);
                }
            }
        } catch (Exception e) {
            ErrorLogger.logError(e);
        }
        return linkedList;
    }

    public static List<newspaper> getNewspapers_by_category() throws SQLException {
        LinkedList linkedList = new LinkedList();
        Statement statement;
        ResultSet rs = null;
        DbFunction db = new DbFunction();
        Connection conn = db.connect_to_db("New_Db", "postgres", "123456789");
        try {
            String query1 = String.format("select * from data_links ");
            statement = conn.createStatement();
            rs = statement.executeQuery(query1);
            while (rs.next()) {
                newspaper Newspapers = newspaper.build(
                        rs.getString("code"),
                        rs.getString("category"),
                        rs.getInt("hour"),
                        rs.getInt("minute"),
                        rs.getInt("second"),
                        rs.getInt("day"),
                        rs.getInt("month"),
                        rs.getInt("year"),
                        rs.getString("heading"),
                        rs.getString("description"),
                        rs.getString("img")
                );
                linkedList.add(Newspapers);
            }
        }
        catch (Exception e) {
            ErrorLogger.logError(e);
        }
        return linkedList;
}
}
