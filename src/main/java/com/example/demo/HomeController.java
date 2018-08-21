package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
        return "index";
    }

    @RequestMapping("/")
    public String listUsers(Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        return "list";
    }

    @RequestMapping("/login")
    public String login(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
//            model.addAttribute("messages", messageRepository.findAll());
            return "login";
        }
        userRepository.save(user);
        return "redirect:/addMessage";
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

    @GetMapping("/addRegistration")
    public String registrationForm(Model model) {
//        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/processRegistration")
    public String processRegistrationForm(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
//            model.addAttribute("messages", messageRepository.findAll());
            return "registration";
        }
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/addMessage")
    public String messageForm(Model model) {
        model.addAttribute("message", new Message());
        return "messageform";
    }

    @PostMapping("/processMessage")
    public String processMessageForm(@Valid Message message, BindingResult result, Model model) {
        if (result.hasErrors()) {

            return "messageform";
        }
        messageRepository.save(message);
        return "redirect:/";

    }

//    @RequestMapping("/update/{id}")
//    public String userUpdate(@PathVariable("id") long id, Model model) {
//        model.addAttribute("message", messageRepository.findById(id).get());
//        return "message";
//    }
//    @RequestMapping("/delete/{id}")
//    public String delMessage(@PathVariable("id") long id){
//        messageRepository.deleteById(id);
//        return "redirect:/secure";
//    }
//    @RequestMapping("/profile/{id}")
//    public String userProfile(@PathVariable("id") long id, Model model) {
//        model.addAttribute("user", userRepository.findById(id).get());
//        return "show";
   // }
}