package am.avantgarde.avantgardeweb.controller;

import am.avantgarde.avantgardecommon.model.Gender;
import am.avantgarde.avantgardecommon.model.User;
import am.avantgarde.avantgardecommon.model.UserType;
import am.avantgarde.avantgardecommon.repository.UserRepository;
import am.avantgarde.avantgardeweb.security.SpringUser;
import am.avantgarde.avantgardecommon.service.EmailService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController {

//    @Value("C:\\Users\\edona\\IdeaProjects\\avantgarde-project\\avantgarde-web\\src\\main\\resources\\static\\media\\persons")
//    private String userPicDir;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PostMapping("/registration")
    public String registration(@RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname,
                               @RequestParam("email") String email, @RequestParam("password") String password,
                               @RequestParam("gender") Gender gender) {

        User user = new User();

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(password);
        user.setGender(gender);
//        public String registration(@ModelAttribute User user){

        String token = UUID.randomUUID().toString();

        user.setToken(token);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        emailService.SendSimpleMessage(user.getEmail(),
                "Բարի Գալուստ " + user.getFirstname(),
                "Для завершения регистрации перейдите по ссылке " + "\n http://localhost:8080/confirm?token=" + token);

        userRepository.save(user);

        return "redirect:/";

    }

    @GetMapping("/confirm")
    public String confirmReg(@RequestParam("token") String token) {

        User byToken = userRepository.findByToken(token);

        Optional<User> user = userRepository.findById(byToken.getId());
        if (user.isPresent()) {
            byToken.setActive(true);
            userRepository.save(byToken);
            return "redirect:/";
        }
        return "404";

    }

    @PostMapping("/successLogin")
    public String login(@AuthenticationPrincipal SpringUser springUser) {

        User user = springUser.getUser();

        if (user.isActive() && user.getUserType() == UserType.USER) {
            LOGGER.info("User successfully logged in");
            return "redirect:/account";
        }

        LOGGER.info("Admin successfully logged in");

        return "redirect:/adminPage";
    }

    @PostMapping("/failureLogin")
    public String failLog(ModelMap map) {
        map.addAttribute("message", "Email or password is invalid!");
        return "login";
    }

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal SpringUser springUser, ModelMap map) {
        User user = springUser.getUser();

        map.addAttribute("user", user);
        return "account";
    }

    @GetMapping("/adminPage")
    public String adminPage(@AuthenticationPrincipal SpringUser springUser, ModelMap map) {
        map.addAttribute("user", springUser.getUser());
//        LOGGER.info("Admin login into admin page");
        return "adminPage";
    }

//    @GetMapping("/getUserPic")
//    public void getNewsMainImage(HttpServletResponse response, @RequestParam("picUrl") String picUrl) throws IOException {
//        InputStream in = new FileInputStream(userPicDir + File.separator + picUrl);
//        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//        IOUtils.copy(in, response.getOutputStream());
//    }

}
