package com.example.wecode.controllers;

import com.example.wecode.models.*;
import com.example.wecode.services.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class UserController {
    private final UserService userServ;
    private final CompanyService companyService;

    private final CategoryService categoryService;
    private final FeedBackService feedBackService;
    private final LanguagesService languagesService;
    private final SkillsService skillsService;

    private final ChatService chatService;

    public UserController(UserService userServ, CompanyService companyService, CategoryService categoryService, FeedBackService feedBackService, LanguagesService languagesService, SkillsService skillsService, ChatService chatService) {
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


    //    updating info
    @GetMapping("/update")
    public String updatingForm(Model model, HttpSession session ) {
        if (session.getAttribute("user_id")!=null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(userId);
            model.addAttribute("currentUser", currentUser);
            int x = 0;
            if (session.getAttribute("user_id")!=null){x=1;}
            if (session.getAttribute("user_id")==null){x=0;}
            model.addAttribute("x" , x);
            model.addAttribute("allcategories", categoryService.allCategories());
            return "updateuserprofile.jsp";
        }
        return "redirect:/";
    }

    @PatchMapping("/updateuser")
    public String update(Model model ,  HttpSession session ,
                           @RequestParam("userName") String userName,
                           @RequestParam("email") String email,
                           @RequestParam("idNum") Integer idNum,
                           @RequestParam("experience") Integer experience,
                           @RequestParam("location") String location,
                           @RequestParam("cv") String cv,
                           @RequestParam("status") String status,
                           @RequestParam("image") String image,
                           @RequestParam("category") Long category,
                           @RequestParam("python") boolean python,
                           @RequestParam("java") boolean java,
                           @RequestParam("javascript") boolean javascript,
                           @RequestParam("php") boolean php,
                           @RequestParam("net") boolean net,
                           @RequestParam("Cp") boolean Cp,
                           @RequestParam("C") boolean C,
                           @RequestParam("Cs") boolean Cs

    )
    {

        Long userId = (Long) session.getAttribute("user_id");
        User currentUser = userServ.findUserById(userId);

        Category categoryy = categoryService.findCategory(category);
        if (python == true)
        {
            Long c=1L;
            Languages languagess = languagesService.findLanguages(c);
            List<User> users = languagess.getUsers();
            users.add(currentUser);
            languagess.setUsers(users);
        }
        if (java == true)
        {
            Long c=2L;
            Languages languagesss = languagesService.findLanguages(c);
            List<User> users = languagesss.getUsers();
            users.add(currentUser);
            languagesss.setUsers(users);
        }
        if (javascript == true)
        {
            Long c=3L;
            Languages languageess = languagesService.findLanguages(c);
            List<User> users = languageess.getUsers();
            users.add(currentUser);
            languageess.setUsers(users);
        }
        if (php == true)
        {
            Long c=4L;
            Languages f = languagesService.findLanguages(c);
            List<User> users = f.getUsers();
            users.add(currentUser);
            f.setUsers(users);
        }
        if (net == true)
        {
            Long c=5L;
            Languages e = languagesService.findLanguages(c);
            List<User> users = e.getUsers();
            users.add(currentUser);
            e.setUsers(users);
        }
        if (Cp == true)
        {
            Long c=6L;
            Languages m = languagesService.findLanguages(c);
            List<User> users = m.getUsers();
            users.add(currentUser);
            m.setUsers(users);
        }
        if (C == true)
        {
            Long c=7L;
            Languages o = languagesService.findLanguages(c);
            List<User> users = o.getUsers();
            users.add(currentUser);
            o.setUsers(users);
        }
        if (Cs == true)
        {
            Long c=8L;
            Languages q = languagesService.findLanguages(c);
            List<User> users = q.getUsers();
            users.add(currentUser);
            q.setUsers(users);
        }
        currentUser.setUserName(userName);
        currentUser.setEmail(email);
        currentUser.setIdNum(idNum);
        currentUser.setExperience(experience);
        currentUser.setLocation(location);
        currentUser.setCv(cv);
        currentUser.setImage(image);

        currentUser.setStatus(Boolean.parseBoolean(status));
        currentUser.setCategory(categoryy);

        userServ.updateUser(currentUser);

        return "redirect:/success";
    }







    @GetMapping("/success")
    public String success(Model model, HttpSession session) {
        if (session.getAttribute("user_id")!=null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(userId);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("userId" , userId);
            int x = 0;
            if (session.getAttribute("user_id")!=null){x=1;}
            if (session.getAttribute("user_id")==null){x=0;}
            model.addAttribute("x" , x);

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
            model.addAttribute("userId" , userId);
            int x = 0;
            if (session.getAttribute("user_id")!=null){x=1;}
            if (session.getAttribute("user_id")==null){x=0;}
            model.addAttribute("x" , x);

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
    public String conactus(@ModelAttribute("feedback") FeedBack feedback, Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        model.addAttribute("userId" , userId);
        int x = 0;
        if (session.getAttribute("user_id")!=null){x=1;}
        if (session.getAttribute("user_id")==null){x=0;}
        model.addAttribute("x" , x);
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
            int x = 0;
            if (session.getAttribute("user_id")!=null){x=1;}
            if (session.getAttribute("user_id")==null){x=0;}
            model.addAttribute("x" , x);
            Category category = categoryService.findCategory(id);
            model.addAttribute("category", category.getUsers());

            return "catdev.jsp";
        }
        return "redirect:/";
    }


    @GetMapping("/company/{id}")
    public String companyInfo(@PathVariable("id") Long id, Model model, HttpSession session){
        if (session.getAttribute("user_id")!=null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(userId);
            model.addAttribute("currentUser", currentUser);

            Company comp = companyService.findCompanyById(id);
            model.addAttribute("comp", comp);

            return "companyinfo.jsp";
        }
        return "redirect:/";
    }





    @GetMapping("/dev/{id}")
    public String Devinfo(@PathVariable("id") Long id, Model model, HttpSession session){
        if (session.getAttribute("user_id")!=null) {
            Long userId = (Long) session.getAttribute("user_id");
            User currentUser = userServ.findUserById(id);
            List<Languages> languages = currentUser.getLanguages();
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("langs" , languages );
            User developer = userServ.findUserById(id);
            model.addAttribute("developer", developer);

            Skills y=developer.getSkills();
            int[] Req = new int[]{y.getCommitment(),y.getCommunicationSkills(),y.getLeaderShip(),y.getProblemSolving(),y.getResearchSkills(),y.getSelfSufficient(), y.getTeamWork(),y.getTimeManagement(),y.getWorkingUnderPressure()};
            JSONArray jsonArray2 = new JSONArray(Req);
            model.addAttribute("employee",jsonArray2);



            return "devinfo.jsp";
        }
        return "redirect:/";
    }



    @GetMapping("/skills/new")
    public String NewSkills(@ModelAttribute("skills") Skills skills, Model model, HttpSession session){
        int x = 0;
        if (session.getAttribute("user_id")!=null){x=1;}
        if (session.getAttribute("user_id")==null){x=0;}
        model.addAttribute("x" , x);

        Long userId = (Long) session.getAttribute("user_id");
        User currentUser = userServ.findUserById(userId);
        model.addAttribute("currentUser", currentUser);
        return "skills.jsp";
    }

    @PostMapping("/createskills")
    public String createSkills( @Valid @ModelAttribute("skills") Skills skills, BindingResult result) {

        if (result.hasErrors()) {
            return "skills.jsp";
        } else {
            skillsService.createSkills(skills);
            return "redirect:/skills/new";
        }
    }





















































}

