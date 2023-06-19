package com.example.filemaneger.controller;


import com.example.filemaneger.file.domain.FilesEntity;
import com.example.filemaneger.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



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
    public String search(Model model,
                         @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                         @RequestParam(value="search", defaultValue = "") String search){

       Page<FilesEntity> searchedFileList = fileService.getSearchList(search, (pageNumber - 1));


       model.addAttribute("list", searchedFileList);
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

    @GetMapping("/fileListPage")
    public String fileList(Model model,
                           @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber){

        Page<FilesEntity> fileList = fileService.getPagingList(pageNumber - 1);

        model.addAttribute("list", fileList);
        model.addAttribute("title", "fileList");

        return "pages/fileList";
    }




}
