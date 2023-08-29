package com.example.backend.SourceNewspapers;
import com.example.backend.DataBaseService.DbFunction;
import com.example.backend.Newspapers.Titles;
import com.example.backend.Newspapers.newspaper;
import com.example.backend.ErrorHandle.ErrorLogger;
import io.swagger.v3.oas.models.links.Link;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.HTML;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetData {
    public static String data(Connection conn, DbFunction db) throws IOException, ParseException, SQLException {


        String HTMLCode ="";
        LinkedList<String>linkedList = new LinkedList<>();
        Document document = Jsoup.connect("https://tuoitre.vn/tin-moi-nhat.htm").get();
        Elements nodeElement = document.select("div.box-category-item");
        Elements links = document.select("div .list__listing-main a.box-category-link-title[href]");
        for(Element link: nodeElement) {
            String code = link.select("div.box-category-content div.box-content-title, h3.box-title-text a.box-category-link-title").attr("data-id");
            String type = link.select("div.box-category-content a.box-category-category").text();
            String heading = link.select("div.box-content-title h3.box-title-text").text();  //Tiêu đề
            String description = link.select("div.box-category-content p[class *=box-category-sapo]").text(); //Mô tả
            Element image = link.selectFirst("div.box-category-item a[class=box-category-link-with-avatar img-resize]");
            String check = "<video .*>.*?</video>";
            Pattern pattern = Pattern.compile(check, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(image.toString());
            String img = "";
            if (matcher.find()) {
                img = (image.select("video[class*=lozad-video] source").attr("data-src"));
            }
            else {
                img = image.select("img.box-category-avatar").attr("src");
            }
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

            ZonedDateTime zonedDateTime1 = ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.of("UTC"));
            Timestamp timestamp = Timestamp.from(zonedDateTime.toInstant());
            db.insert_link(conn,"table_links",code,type,href,heading,description,img,day,month,year,hour,minute,second,timestamp);

        }

        return nodeElement.toString();

    }
    public static Titles newspaper(String code, Connection conn, DbFunction db){
        Titles titles = new Titles() ;
        LinkedList <Titles> linkedList = new LinkedList<>();
        Statement statement;
        ResultSet rs = null;
        try{
            String query = String.format("select * from table_newspapers where code = '%s'",code);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(rs.next()){
                 titles = Titles.build(
                         rs.getString("code"),
                        rs.getString("category"),
                        rs.getString("time"),
                        rs.getString("heading"),
                        rs.getString("subHeading"),
                         rs.getString("content")
                );
            }
            linkedList.add(titles);

        }catch (Exception e) {
            ErrorLogger.logError(e);
        }
        return linkedList.get(0);
    }
}
