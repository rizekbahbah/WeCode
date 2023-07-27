package com.example.wecode.controllers;

import com.example.wecode.models.*;
import com.example.wecode.services.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.json.JSONArray;
@Controller
public class HomeController {
    private final UserService userServ;
    private final CompanyService companyService;

    private final CategoryService categoryService;
    private final FeedBackService feedBackService;
    private final LanguagesService languagesService;
    private final SkillsService skillsService;

    private final ChatService chatService;

    public HomeController(UserService userServ, CompanyService companyService, CategoryService categoryService, FeedBackService feedBackService, LanguagesService languagesService, SkillsService skillsService, ChatService chatService) {
        this.userServ = userServ;
        this.companyService = companyService;
        this.categoryService = categoryService;
        this.feedBackService = feedBackService;
        this.languagesService = languagesService;
        this.skillsService = skillsService;
        this.chatService = chatService;
    }
    @GetMapping("/loginpageuser")
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("user_id")!=null) {
            return "redirect:/success";
        }
        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "login.jsp";
    }
    @GetMapping("/")
    public String showHomepage(Model model, HttpSession session) {
        if (session.getAttribute("user_id")!=null) {
            return "redirect:/success";
        }
        return "success.jsp";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") User newUser,
                           BindingResult result, Model model, HttpSession session) {
        User regUser = userServ.register(newUser, result);
        if(result.hasErrors()) {
            model.addAttribute("newLogin", new LoginUser());
            return "login.jsp";
        }
        session.setAttribute("user_id", regUser.getId());
        return "redirect:/success";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin,
                        BindingResult result, Model model, HttpSession session) {
        User logUser = userServ.login(newLogin, result);
        if(result.hasErrors()) {
            model.addAttribute("newUser", new User());
            return "login.jsp";
        }
        session.setAttribute("user_id", logUser.getId());
        return "redirect:/success";
    }


    //updating user info
    @GetMapping("/userUpdateForm")
    public String updateUserForm(@ModelAttribute("user") User user, Model model, HttpSession session) {
        if (session.getAttribute("user_id") != null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(userId);
            model.addAttribute("currentUser", currentUser);


            return "userFormTest.jsp";
        }
        return "redirect:/";
    }

    @PutMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User user){
            userServ.updateUser(user);
            return "redirect:/updateUserForm";
        }

//    updating info
    @GetMapping("/update")
    public String updatingForm(Model model, HttpSession session ) {
        if (session.getAttribute("user_id")!=null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(userId);
            model.addAttribute("currentUser", currentUser);
            return "updateuserprofile.jsp";
        }
        return "redirect:/";
    }

    @PatchMapping("/updateuser")
    public String update(  HttpSession session ,
                         @RequestParam("userName") String userName,
                         @RequestParam("email") String email,
                         @RequestParam("idNum") Integer idNum,
                         @RequestParam("experience") Integer experience,
                         @RequestParam("location") String location,
                         @RequestParam("cv") String cv,
                         @RequestParam("image") String image)
    {
        Long userId = (Long) session.getAttribute("user_id");
        User currentUser = userServ.findUserById(userId);

        currentUser.setUserName(userName);
        currentUser.setEmail(email);
        currentUser.setIdNum(idNum);
        currentUser.setExperience(experience);
        currentUser.setLocation(location);
        currentUser.setCv(cv);
        currentUser.setImage(image);

//        currentUser.setStatus(Boolean.parseBoolean(status));
        System.out.println("7777777777777777777777777777");
        userServ.updateUser(currentUser);
        System.out.println("8888888888888888888888888888");
        return "redirect:/success";
    }



    @GetMapping("/test")
    public String testingyazan(Model model, HttpSession session) {
        if (session.getAttribute("user_id")!=null) {
            return "redirect:/success";
        }
        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "testingyazanlogin.jsp";
    }



    @GetMapping("/success")
    public String success(Model model, HttpSession session) {
        if (session.getAttribute("user_id")!=null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(userId);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("userId" , userId);
            return "success.jsp";
        }
        return "redirect:/";
    }

    @GetMapping("/categories")
    public String showCategories(Model model, HttpSession session) {
        if (session.getAttribute("user_id")!=null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(userId);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("allCategories", categoryService.allCategories());

            return "showCategories.jsp";
        }
        return "redirect:/";
    }


    @GetMapping("/companies")
    public String showCompanies(Model model, HttpSession session) {
        if (session.getAttribute("user_id")!=null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(userId);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("allCompanies", companyService.allCompanies());

            return "showCompanies.jsp";
        }
        return "redirect:/";
    }


    @GetMapping("/aboutus")
    public String aboutus(Model model, HttpSession session) {


        return "aboutUs.jsp";
    }
    @GetMapping("/contactus")
    public String conactus(@ModelAttribute("feedback") FeedBack feedback,Model model, HttpSession session) {
        model.addAttribute("allFeedback", feedBackService.allFeedBacks());


        return "contactus.jsp";
    }




//    contact us form

    @PostMapping("/contactus")
    public String createContact(@Valid @ModelAttribute("feedback") FeedBack feedback, BindingResult result) {
        if (result.hasErrors()) {
            return "contactus.jsp";
        }
        feedBackService.createFeedBack(feedback);
        return "redirect:/contactus";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }




    @GetMapping("/chat")
    public String chat(Model model, HttpSession session, @ModelAttribute("chat") Chat chat) {
        if (session.getAttribute("user_id")!=null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(userId);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("allMsges", chatService.allChats());

            return "chat.jsp";
        }
        return "redirect:/";
    }

//    chatform


    @PostMapping("/chat/new")
    public String createChat(@Valid @ModelAttribute("chat") Chat chat, BindingResult result) {
        if (result.hasErrors()) {
            return "chat.jsp";
        }
        chatService.createChat(chat);
        return "redirect:/chat";
    }



    @GetMapping("/category/{id}")
    public String categoryDev(@PathVariable("id") Long id, Model model, HttpSession session){
        if (session.getAttribute("user_id")!=null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(userId);
            model.addAttribute("currentUser", currentUser);

            Category category = categoryService.findCategory(id);
            model.addAttribute("category", category);

            return "catdev.jsp";
        }
        return "redirect:/";
    }

    @GetMapping("/dev/{id}")
    public String Devinfo(@PathVariable("id") Long id, Model model, HttpSession session){
        if (session.getAttribute("user_id")!=null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(userId);
            model.addAttribute("currentUser", currentUser);

            User developer = userServ.findUserById(id);
            model.addAttribute("developer", developer);

            return "devinfo.jsp";
        }
        return "redirect:/";
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







    @GetMapping("/SkillCharTesting")
    public  String comparingReqWithSkills(HttpSession session,Model model){
        int[] companyReq = new int[]{35, 39, 70, 65, 85, 55, 78,90,56};
        int[] employeeSkills = new int[]{28, 48, 40, 90, 80, 27, 40,79,90};

        JSONArray jsonArray = new JSONArray(companyReq);
        JSONArray jsonArray2 = new JSONArray(employeeSkills);

        model.addAttribute("company",jsonArray);
        model.addAttribute("employee",jsonArray2);

        return "SkillCharTesting.jsp";
    }










































}
