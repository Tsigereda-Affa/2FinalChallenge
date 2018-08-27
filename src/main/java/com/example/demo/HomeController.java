package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    DmessageRepository dmessageRepository;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistrationPage(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {

        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message",
                    "User Account Successfully Created");
        }
        return "redirect:/";
}

    @RequestMapping("/")
    public String listUsers(Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        return "list";
    }
    @GetMapping("/login")
    public String login(Model model) {

        //model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/processLogin")
    public String login(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {

            return "login";
        }
        userRepository.save(user);
        return "redirect:/";
    }

    @RequestMapping("/secure")
    public String secure(HttpServletRequest request,
                         Authentication authentication,
                         Principal principal) {
        Boolean isAdmin = request.isUserInRole("ADMIN");
        Boolean isUser = request.isUserInRole("USER");
        UserDetails userDetails = (UserDetails)
                authentication.getPrincipal();
        String username = principal.getName();
        return "secure";
    }

//    @GetMapping("/addRegistration")
//    public String registrationForm(Model model) {
////        model.addAttribute("messages", messageRepository.findAll());
//        model.addAttribute("user", new User());
//        return "registration";
//    }
//
//    @PostMapping("/processRegistration")
//    public String processRegistrationForm(@Valid User user, BindingResult result, Model model) {
//        if (result.hasErrors()) {
////            model.addAttribute("messages", messageRepository.findAll());
//            return "registration";
//        }
//        userRepository.save(user);
//        return "redirect:/";
//    }

    @GetMapping("/add")
    public String messageForm(Model model) {
        model.addAttribute("message", new Message());
        return "messageform";
    }

    @PostMapping("/processMessage")
    public String processForm(@ModelAttribute Message message, BindingResult result, Model model)
    {
//        String username = getUser().getUsername();
//        message.setUsername(username);
//        messageRepository.save(message);
//        model.addAttribute("messages", messageRepository.findByUsername(username));
//        return "";

        if (result.hasErrors()) {

            return "messageform";
        }
        String username = getUser().getUsername();
        message.setUsername(username);
        messageRepository.save(message);
        model.addAttribute("messages", messageRepository.findByUsername(username));
        return "redirect:/add";

    }

    @RequestMapping("/edit/{username}")
    public String userUpdate(@PathVariable("username") String username, Model model) {
//        model.addAttribute("user", userService.getCurrentUser());
//        model.addAttribute("user", messageRepository.findByUsername(username));
//        username = userService.getCurrentUser().getUsername();
        model.addAttribute("user", userRepository.findByUsername(username));
        return "registration";
    }
//    @RequestMapping("/delete/{id}")
//    public String delMessage(@PathVariable("id") long id){
//        messageRepository.deleteById(id);
//        return "redirect:/secure";
//    }
    @RequestMapping("/profile/{username}")
    public String userProfile(@PathVariable("username") String username, Model model) {
//        username = userService.getCurrentUser().getUsername();
        model.addAttribute("user", userRepository.findByUsername(username));
        return "show";
    }

    @GetMapping("/dmessage")
    public String dmessageForm(Model model) {
        model.addAttribute("dmessage", new Dmessage());
        return "dmessageform";
    }

    @PostMapping("/processDmessage")
    public String processDmessageForm(@ModelAttribute Dmessage dmessage, BindingResult result, Model model)
    {
//        String username = getUser().getUsername();
//        message.setUsername(username);
//        messageRepository.save(message);
//        model.addAttribute("messages", messageRepository.findByUsername(username));
//        return "";

        if (result.hasErrors()) {

            return "dmessageform";
        }
        String username = getUser().getUsername();
//        dmessage.setSendto(sendto);
        dmessage.setUsername(username);
        dmessageRepository.save(dmessage);
       // model.addAttribute("dmessages", dmessageRepository.findBysendto(sendto));
        model.addAttribute("dmessages", dmessageRepository.findByusername(username));
     return "redirect:/dmessage";
//        return "redirect:/";


    }

    @RequestMapping("/direct/{username}")
    public String dmessageDetail(@PathVariable("username") String username, Model model) {
//       User username = userService.getCurrentUser();
        model.addAttribute("demessage", dmessageRepository.findByusername(username));
        return "dshow";
    }

private User getUser(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentusername = authentication.getName();
    User user = userRepository.findByUsername(currentusername);
    return user;
    }
}