package com.example.backend.Controller;
import com.example.backend.Newspapers.Titles;
import com.example.backend.SourceNewspapers.GetData;
import com.example.backend.Newspapers.newspaper;
import com.example.backend.MenuBar.GetCategory;
import com.example.backend.Newspaper.GetNewspaper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.links.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@RestController
public class MainController {
    @Autowired
//    @RequestMapping(value ="/", method = RequestMethod.GET, produces = "application/json")
//    @Operation(summary = "Get all the newspaper that had been stored on database and source from tuoitre.vn")
////    @GetMapping("/")
//    public static List<newspaper> getLastsNewspapers() throws IOException, ParseException, SQLException {
////        GetData.data();
////        GetNewspaper.readData();
////        GetCategory.getCategory();
//        return GetData.getNewspapers();
//    }
    @RequestMapping(value ="/get/menubar", method = RequestMethod.GET, produces = "application/json")
    @Operation(summary = "Get the menu bar from tuoitre.vn")
    public static List<String> getBarCategory() throws SQLException, IOException {
        GetCategory.getCategory();
        return GetCategory.getMenuBar();
    }
    @GetMapping (value = "/", produces = "application/json")
    @Operation(summary = "API help you filter the latest newspapers as category from tuoitre.vn")
    public static List<newspaper> getNewspaper_by_category(@Parameter(description = "If you want to search for the category that you like, you will type it on the input category form")
                                                               @RequestParam(required = false,name = "category") String category) throws IOException, SQLException

    {
        // Xử lý logic ở đây
        if (category == null || category.isEmpty()) {
            return GetNewspaper.getNewspapers_by_category();
        }
        else{
            return GetNewspaper.getNewspapers_by_category(category);
        }

    }

    @RequestMapping(value ="/newspaper/{code}", method = RequestMethod.GET, produces = "application/json")
    @Operation(summary = "Get the details about newspaper as code")
    public static Titles data(@PathVariable("code")String code) throws IOException{

        return GetData.newspaper(code);
    }
}
