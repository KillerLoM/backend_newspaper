package com.example.backend.Controller;
import com.example.backend.DataBaseService.DbFunction;
import com.example.backend.Newspapers.Titles;
import com.example.backend.SourceNewspapers.GetData;
import com.example.backend.Newspapers.newspaper;
import com.example.backend.MenuBar.GetCategory;
import com.example.backend.Newspaper.GetNewspaper;
import com.example.backend.Response.NewspaperResponse;
import com.example.backend.Newspapers.category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.links.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@RestController
public class MainController {

//    public static int i =0;

    public static DbFunction db = new DbFunction();
    public static Connection conn = db.connect_to_db("Db_Server", "postgres", "123456789");
    @Autowired
    @RequestMapping(value ="/get/menubar", method = RequestMethod.GET, produces = "application/json")
    @Operation(summary = "Get the menu bar from tuoitre.vn")
    public static List<category> getBarCategory() throws SQLException, IOException {
        GetCategory.getCategory(conn, db);
        return GetCategory.getMenuBar(conn, db);
    }
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @Operation(summary = "API help you filter the latest newspapers as category from tuoitre.vn")
    public static NewspaperResponse getNewspaperByCategory(
            @Parameter(description = "If you want to search for the category that you like, you will type it on the input category form. You must to type " +"the id of category as: 0 and 1: For the lastest new on all categories" +" 2: For Video" +" 3: For Hot News" +" 4: For Word News" +" 5: For Law News" +"6: For Bussines News" +"7: For Technology News" +" 8: For Car News" +" 9: For Travelling News" +"10: For Young's life style News" +"11: For Cultural News" +"12: For Entertaiments News" +"13: For Sport News" +"14: For Education News" +"15: For Home & Landry News" +"16: For Health News" +"17: For Fake/Real News" +" 18: For Reader News" )
            @RequestParam(required = false, name = "id") Integer id,
            @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
            @RequestParam(required = false, name = "size", defaultValue = "1") Integer size) throws IOException, SQLException {

        if (id == null || id == 0 || id == 1) {
            return GetNewspaper.getNewspapers_by_category(page, size, conn, db);
        } else {
            return GetNewspaper.getNewspapers_by_category(id, page, size, conn, db);
        }
        }
    @GetMapping(value = "/{category}", produces = "application/json")
    @Operation(summary = "API help the user chose the category that they like")
    public static NewspaperResponse getNewspapersByUser(@PathVariable("category")String category,
                                                        @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(required = false, name = "size", defaultValue = "1") Integer size) throws IOException, SQLException
    {
        return GetNewspaper.getNewspapers_by_user(category, page, size,conn, db);
    }

    @RequestMapping(value ="/newspaper/{code}", method = RequestMethod.GET, produces = "application/json")
    @Operation(summary = "Get the details about newspaper with code")
    public static Titles data1(@PathVariable("code")String code) throws IOException{
        return GetData.newspaper(code, conn, db);
    }
}
