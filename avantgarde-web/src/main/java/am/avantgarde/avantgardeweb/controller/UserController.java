package am.avantgarde.avantgardeweb.controller;

import am.avantgarde.avantgardecommon.model.User;
import am.avantgarde.avantgardecommon.model.UserType;
import am.avantgarde.avantgardecommon.repository.UserRepository;
import am.avantgarde.avantgardeweb.security.SpringUser;
import am.avantgarde.avantgardecommon.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user){

        String token = UUID.randomUUID().toString();

        user.setToken(token);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        emailService.SendSimpleMessage(user.getEmail(),
                "Բարի Գալուստ " + user.getFirstname(),
                "Для завершения регистрации перейдите по ссылке " + "\n http://localhost:8080/confirm?token="  +  token);

        userRepository.save(user);

        return "redirect:/";

    }

    @GetMapping("/confirm")
    public String confirmReg(@RequestParam("token") String token){

        User byToken = userRepository.findByToken(token);

        Optional<User> user = userRepository.findById(byToken.getId());
        if(user.isPresent()){
            byToken.setActive(true);
            userRepository.save(byToken);
            return "redirect:/";
        }
        return "";

    }

    @PostMapping("/successLogin")
    public String login(@AuthenticationPrincipal SpringUser springUser){

        User user = springUser.getUser();

        if(user.isActive() && user.getUserType() == UserType.USER){
            return "redirect:/account";
        }

        return "redirect:/adminPage";
    }

    @PostMapping("/failureLogin")
    public String failLog(ModelMap map){
        map.addAttribute("message","Email or password is invalid!");
        return "login";
    }

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal SpringUser springUser,ModelMap map){
        User user = springUser.getUser();

        map.addAttribute("user",user);
        return "account";
    }

    @GetMapping("/adminPage")
    public String adminPage(@AuthenticationPrincipal SpringUser springUser, ModelMap map){
        map.addAttribute("user",springUser.getUser());
        return "adminPage";
    }

}
