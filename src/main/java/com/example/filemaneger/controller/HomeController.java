package com.example.filemaneger.controller;


import com.example.filemaneger.file.domain.FilesEntity;
import com.example.filemaneger.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    private FileService fileService;

    @RequestMapping("/layout/index")
    public String toolBar(Model model) {
        model.addAttribute("title", "home");
        return "layout/index";
    }

    @RequestMapping("/filesPage")
    public String files(Model model){
        model.addAttribute("title", "files");
        return "pages/files";
    }

   @RequestMapping("/searchPage")
    public String search(Model model){
        model.addAttribute("title", "search");
        return "pages/search";
    }
   @RequestMapping("/settingPage")
    public String setting(Model model){
        model.addAttribute("title", "setting");
        return "pages/setting";
    }
    @RequestMapping("/dashBoardPage")
    public String dashBoard(Model model){
        model.addAttribute("title", "dashboard");
        return "pages/dashBoard";
    }

    @RequestMapping("/fileListPage")
    public String fileList(Model model){

        model.addAttribute("title", "fileList");

        return "pages/fileList";
    }



}
