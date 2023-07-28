package com.example.wecode.controllers;

import com.example.wecode.models.Company;
import com.example.wecode.models.LoginCompany;
import com.example.wecode.models.Skills;
import com.example.wecode.services.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CompanyController {
    private final UserService userServ;
    private final CompanyService companyService;

    private final CategoryService categoryService;
    private final FeedBackService feedBackService;
    private final LanguagesService languagesService;
    private final SkillsService skillsService;

    private final ChatService chatService;

    public CompanyController(UserService userServ, CompanyService companyService, CategoryService categoryService, FeedBackService feedBackService, LanguagesService languagesService, SkillsService skillsService, ChatService chatService) {
        this.userServ = userServ;
        this.companyService = companyService;
        this.categoryService = categoryService;
        this.feedBackService = feedBackService;
        this.languagesService = languagesService;
        this.skillsService = skillsService;
        this.chatService = chatService;
    }






    @GetMapping("/regFormCompany")
    public String regFormCompany(Model model, HttpSession session) {
        if (session.getAttribute("company_id")!=null) {
            return "redirect:/successcompany";
        }
        model.addAttribute("newCompany", new Company());
        model.addAttribute("newLoginCompany", new LoginCompany());
        return "regcompany.jsp";
    }

    @PostMapping("/registercompany")
    public String registerCompany(@Valid @ModelAttribute("newCompany") Company newCompany,
                                  BindingResult result, Model model, HttpSession session) {
        Company regCompany = companyService.register(newCompany, result);
        if(result.hasErrors()) {
            model.addAttribute("newLogin", new LoginCompany());
            return "regcompany.jsp";
        }
        session.setAttribute("company_id", regCompany.getId());
        return "redirect:/successcompany";
    }



    @GetMapping("/company/loginform")
    public String companyLogin(Model model, HttpSession session) {
        if (session.getAttribute("company_id")!=null) {
            return "redirect:/success";
        }
        model.addAttribute("newCompany", new Company());
        model.addAttribute("newLoginCompany", new LoginCompany());
        return "companylogin.jsp";
    }



    @PostMapping ("/logincompany")
    public String companyLogin(@Valid @ModelAttribute("newLoginCompany") LoginCompany newLoginCompany,
                               BindingResult result, Model model, HttpSession session) {
        Company logCompany = companyService.login(newLoginCompany, result);
        if(result.hasErrors()) {
            model.addAttribute("newCompany", new Company());
            return "companylogin.jsp";
        }
        session.setAttribute("company_id", logCompany.getId());
        return "redirect:/successcompany";
    }
    @GetMapping("/successcompany")
    public String successCompany(Model model, HttpSession session) {
        if (session.getAttribute("company_id")!=null) {
            Long companyId = (Long) session.getAttribute("company_id");
            Company currentCompany = companyService.findCompanyById(companyId);
            model.addAttribute("currentCompany", currentCompany);
            return "companysucess.jsp";
        }
        return "redirect:/";
    }
    @GetMapping("/logoutcompany")
    public String logoutCompany(HttpSession session) {
        session.invalidate();
        return "redirect:/logincompany";
    }

    @GetMapping("/showalldev")
    public String showingAllDevelopers(Model model, HttpSession session) {
        if (session.getAttribute("company_id")!=null) {
            Long companyId = (Long) session.getAttribute("company_id");
            Company currentCompany = companyService.findCompanyById(companyId);
            model.addAttribute("currentCompany", currentCompany);
            model.addAttribute("allDevelopers", userServ.allUsers());

            return "alldevs.jsp";
        }
        return "redirect:/";
    }






    @GetMapping("/byskillsform/new")
    public String hireBySkillsForm(@ModelAttribute("skills") Skills skills, Model model, HttpSession session){


        return "hirebyskillsform.jsp";
    }

    @PostMapping("/companyskills")
    public String companyskills( @Valid @ModelAttribute("skills") Skills skills, BindingResult result, HttpSession session) {

        if (result.hasErrors()) {
            return "hirebyskillsform.jsp";
        } else {
            int[] Req = new int[]{skills.getCommitment(),skills.getCommunicationSkills(),skills.getLeaderShip(),skills.getProblemSolving(),skills.getResearchSkills(),skills.getSelfSufficient(), skills.getTeamWork(),skills.getTimeManagement(),skills.getWorkingUnderPressure()};
            session.setAttribute("companyreq",Req);
            return "redirect:/comparingJobVsDev";
        }
    }




    @GetMapping("/comparingJobVsDev")

    public  String companySkillsVsDevs(HttpSession session,Model model){
        int[] companyReq= (int[]) session.getAttribute("companyreq");


        int[] employeeSkills = new int[]{28, 48, 40, 90, 80, 27, 40,79,90};

        JSONArray jsonArray = new JSONArray(companyReq);
        JSONArray jsonArray2 = new JSONArray(employeeSkills);

        model.addAttribute("company",jsonArray);
        model.addAttribute("employee",jsonArray2);

        return "SkillCharTesting.jsp";
    }








}