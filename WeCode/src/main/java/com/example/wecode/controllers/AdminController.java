package com.example.wecode.controllers;

import com.example.wecode.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class AdminController {
    private final UserService userServ;
    private final CompanyService companyService;

    private final CategoryService categoryService;
    private final FeedBackService feedBackService;
    private final LanguagesService languagesService;
    private final SkillsService skillsService;

    private final ChatService chatService;

    public AdminController(UserService userServ, CompanyService companyService, CategoryService categoryService, FeedBackService feedBackService, LanguagesService languagesService, SkillsService skillsService, ChatService chatService) {
        this.userServ = userServ;
        this.companyService = companyService;
        this.categoryService = categoryService;
        this.feedBackService = feedBackService;
        this.languagesService = languagesService;
        this.skillsService = skillsService;
        this.chatService = chatService;
    }

    @GetMapping("/adminshowcompanies")
    public String adminshowcompanies(Model model, HttpSession session){
        int x = 0;
        if (session.getAttribute("user_id")!=null){x=1;}
        if (session.getAttribute("user_id")==null){x=0;}
        model.addAttribute("x" , x);
        model.addAttribute("allcompanies",companyService.allCompanies());


        return "adminshowcompanies.jsp";

    }
    @GetMapping("/adminshowusers")
    public String adminshowusers(Model model, HttpSession session){
        model.addAttribute("allusers",userServ.allUsers());
        int x = 0;
        if (session.getAttribute("user_id")!=null){x=1;}
        if (session.getAttribute("user_id")==null){x=0;}
        model.addAttribute("x" , x);
        return "adminshowusers.jsp";

    }

    @GetMapping("/deletecompany/{id}")
    public String deletecompany(@PathVariable("id") Long id){
        companyService.deleteCompanyById(id);
        return "redirect:/adminshowcompanies";

    }

    @GetMapping("/deleteuser/{id}")
    public String deleteuser(@PathVariable("id") Long id){
        userServ.deleteUserById(id);
        return "redirect:/adminshowusers";

    }













}
