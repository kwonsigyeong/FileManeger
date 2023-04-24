package com.example.filemaneger.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/layout/index")
    public String toolBar() {
        return "layout/index";
    }

    @RequestMapping("/filesPage")
    public String files(){
        return "pages/files";
    }

   @RequestMapping("/searchPage")
    public String search(){
        return "pages/search";
    }
   @RequestMapping("/settingPage")
    public String setting(){
        return "pages/setting";
    }
    @RequestMapping("/dashBoardPage")
    public String dashBoard(){
        return "pages/dashBoard";
    }



}
