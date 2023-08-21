package com.example.backend.SourceNewspapers;
import com.example.backend.DataBaseService.DbFunction;
import com.example.backend.Newspapers.Titles;
import com.example.backend.Newspapers.newspaper;
import com.example.backend.ErrorHandle.ErrorLogger;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.HTML;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.text.SimpleDateFormat;

public class GetData {
    public static String data() throws IOException, ParseException, SQLException {
//        String HTMLCode ="";
        LinkedList<String>linkedList = new LinkedList<>();
        Document document = Jsoup.connect("https://tuoitre.vn/tin-moi-nhat.htm").get();
        Elements nodeElement = document.select("div.box-category-item");
        Elements links = document.select("div .list__listing-main a.box-category-link-title[href]");
        for(Element link: nodeElement){
            String code = link.select("div.box-category-content div.box-content-title, h3.box-title-text a.box-category-link-title").attr("data-id");
            String type = link.select("div.box-category-content a.box-category-category").text();
            String heading = link.select("div.box-content-title h3.box-title-text").text();  //Tiêu đề
            String description = link.select("div.box-category-content p[class *=box-category-sapo]").text(); //Mô tả
            String image = link.select("img.box-category-avatar").attr("src"); //ảnh timeneail
            String href = link.select("div.box-category-item a").attr("href"); //Nguồn
            String time = link.select("div.box-category-item span.time-ago-last-news").attr("title");
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            ZonedDateTime zonedDateTime = LocalDateTime.parse(time, formatter).atZone(ZoneId.systemDefault());
            int day = zonedDateTime.getDayOfMonth();
            int month = zonedDateTime.getMonthValue();
            int year = zonedDateTime.getYear();
            int hour = zonedDateTime.getHour();
            int minute = zonedDateTime.getMinute();
            int second = zonedDateTime.getSecond();
//            System.out.println(code + "\n" + heading +"\n" + description +"\n" + image +"\n" + day +"/" +month +"/" + year +"\n" + href  +"\n" + type );
            DbFunction db = new DbFunction();
            Connection conn = db.connect_to_db("New_Db","postgres","123456789");
            db.insert_link(conn,"data_links",code,type,href,heading,description,image,day,month,year,hour,minute,second);
            conn.close();

        }
        return nodeElement.toString();
    }
    public static Titles newspaper(String code){
        Titles titles = new Titles();
        DbFunction db = new DbFunction();
        Connection conn = db.connect_to_db("New_Db","postgres","123456789");
        Statement statement;
        ResultSet rs = null;
        try{
            String query = String.format("select * from data_newspapers where code = '%s'",code);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(rs.next()){
                 titles = Titles.build(
                        rs.getString("category"),
                        rs.getString("time"),
                        rs.getString("heading"),
                        rs.getString("subHeading"),
                        rs.getString("content")
                );
            }

        }catch (Exception e) {
            ErrorLogger.logError(e);
        }
        return titles;
    }
}
