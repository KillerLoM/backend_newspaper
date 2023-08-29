package com.example.backend.Newspaper;
import com.example.backend.DataBaseService.DbFunction;
import com.example.backend.Newspapers.Titles;
import com.example.backend.Newspapers.newspaper;
import com.example.backend.ErrorHandle.ErrorLogger;
import com.example.backend.Response.NewspaperResponse;
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

    public static void readData(Connection conn, DbFunction db) throws SQLException {

        Titles titles = new Titles() ;
        String HTML = "";
        Statement statement;
        ResultSet rs = null;
        LinkedList<Titles> linkedList = new LinkedList();
        LinkedList<String> delete = new LinkedList();
            try {
                String query = String.format("select * from %s order by created_at desc ", "table_links");
                statement = conn.createStatement();
                rs = statement.executeQuery(query);
                while (rs.next() && db.check_by_id(conn,"table_newspapers",rs.getString("code"))) {
                    String src = "https://tuoitre.vn/";
                    Document webPage = Jsoup.connect(src + rs.getString("link")).get();
                    Element content = webPage.selectFirst("div .detail-cmain");
                    String timeCode = webPage.select("div .detail__section div .detail-time ").text();
                    Elements link = webPage.select("div[class*=VCSortableInPreviewMode]");
                    String type = webPage.select("div[class *=detail__cmain] div.detail-top div.detail-cate a").attr("title");
                    link.remove();
                    Elements a = webPage.select("a[href*=/]");
                    for (Element links : a) {
                        Element p = new Element("p");
                        TextNode textNode = new TextNode(links.text());
                        links.replaceWith(textNode);
                    }
                    if(content == null){
                        delete.add(rs.getString("code"));
                        continue;
                    }

                    HTML = content.toString();
                    String check = "<div type=\"RelatedOneNews\" class=\"VCSortableInPreviewMode\">.*?</div>";
                    Pattern pattern = Pattern.compile(check, Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(HTML);
                    HTML = matcher.replaceAll("");

                    HTML.replace("/n", "");
                    titles = Titles.build(
                            rs.getString("code"),
                            type,
                            timeCode,
                            rs.getString("heading"),
                            rs.getString("description"),
                            HTML
                    );
                    linkedList.add(titles);
                }

            }catch (Exception e) {
                ErrorLogger.logError(e);
                System.out.println(e);
            }

        while(!linkedList.isEmpty()){
            Titles temp = (linkedList.pop());

            db.insertNewspapers(conn, "table_newspapers", temp.getCode(),temp.getTime(),temp.getCategory(),temp.getHeading(),temp.getSubHeading(),temp.getContent());
            System.out.println(temp);
        }
        while(!delete.isEmpty()){
            String temp = String.valueOf(delete.pop());
            db.delete_row_by_code(conn,"table_links",temp);
        }
       }

    public static NewspaperResponse getNewspapers_by_category(int id, int page, int size, Connection conn, DbFunction db) {
        int skip = page * size;
        LinkedList<newspaper> newspapers = new LinkedList<>();
        Statement statement;
        ResultSet rs = null;
        NewspaperResponse response = null;
        try {
                if (id >1 ){
                    String categoryTemp ="";
                    String qeury1 = String.format("select * from table_category where id = %d",id);
                    statement = conn.createStatement();
                    rs = statement.executeQuery(qeury1);
                    if (rs.next()){
                        categoryTemp = rs.getString("category") ;
                        if (Objects.equals(categoryTemp, "Bạn đọc")) categoryTemp = "Bạn đọc làm báo";
                    }

                    String query = String.format("select * from table_links where category = '%s' order by created_at desc OFFSET %d limit %d", categoryTemp, skip, size);
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
                        newspapers.add(Newspapers);
                    }
                    String countQuery = String.format("SELECT COUNT(*) FROM table_links where category = '%s'",categoryTemp);
                    statement = conn.createStatement();
                    rs = statement.executeQuery(countQuery);
                    int totalCount = 0;
                    if (rs.next()) {
                        totalCount = rs.getInt(1);
                    }
                    response = new NewspaperResponse();
                    response.setNewspapers(newspapers);
                    response.setPageIndex(page);
                    response.setPageSize(size);
                    response.setTotal(totalCount);
            }
        } catch (Exception e) {
            ErrorLogger.logError(e);
        }
        return response;
    }

    public static NewspaperResponse getNewspapers_by_category(int page, int size, Connection conn, DbFunction db) throws SQLException {
        int skip = page * size;
        LinkedList<newspaper> newspapers = new LinkedList<>();
        Statement statement;
        ResultSet rs = null;
        NewspaperResponse response = null;
        try {
            String query1 = String.format("select * from table_links order by created_at desc OFFSET %d limit %d", skip, size);
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
                newspapers.add(Newspapers);
            }
            String countQuery = "SELECT COUNT(*) FROM table_links";
            statement = conn.createStatement();
            rs = statement.executeQuery(countQuery);
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            response = new NewspaperResponse();
            response.setNewspapers(newspapers);
            response.setPageIndex(page);
            response.setPageSize(size);
            response.setTotal(totalCount);
        } catch (Exception e) {
            ErrorLogger.logError(e);
        }
        return response;
    }
    public static NewspaperResponse getNewspapers_by_user(String category, int page, int size, Connection conn, DbFunction db) {
        int skip = page * size;
        LinkedList<newspaper> newspapers = new LinkedList<>();
        Statement statement;
        ResultSet rs = null;
        NewspaperResponse response = null;
        try {
                String query = String.format("select * from table_links where category = '%s' order by created_at desc OFFSET %d limit %d", category, skip, size);
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
                    newspapers.add(Newspapers);
                }
                String countQuery = String.format("SELECT COUNT(*) FROM table_links where category = '%s'",category);
                statement = conn.createStatement();
                rs = statement.executeQuery(countQuery);
                int totalCount = 0;
                if (rs.next()) {
                    totalCount = rs.getInt(1);
                }
                response = new NewspaperResponse();
                response.setNewspapers(newspapers);
                response.setPageIndex(page);
                response.setPageSize(size);
                response.setTotal(totalCount);
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            ErrorLogger.logError(e);
        }
        return response;
    }
}
