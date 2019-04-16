package am.avantgarde.avantgardeweb.controller;

import am.avantgarde.avantgardecommon.model.User;
import am.avantgarde.avantgardecommon.repository.*;
import am.avantgarde.avantgardeweb.security.SpringUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CarImageRepository carImageRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarAnnouncementRepository carRepo;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal SpringUser springUser, ModelMap map) {
//stex normal e or debugy kmtni springi classneri mej vortev null pointer kqce.

        if (springUser != null && springUser.getUser() != null) {
            Optional<User> user = userRepository.findById(springUser.getUser().getId());
            if (user.isPresent()) {
                map.addAttribute("top10cars", carRepository.findTop10ByOrderByIdDesc());
                map.addAttribute("user", springUser.getUser());
                map.addAttribute("active", carRepo.findTop1ByOrderByIdDesc());
                map.addAttribute("announces",carRepo.findAll());
                map.addAttribute("brands", brandRepository.findAll());
                return "home";
            }
        }

        map.addAttribute("top10cars", carRepository.findTop10ByOrderByIdDesc());
        map.addAttribute("announces", carRepo.findAll());
        map.addAttribute("active", carRepo.findTop1ByOrderByIdDesc());
        map.addAttribute("brands", brandRepository.findAll());
        return "home";

    }


    @GetMapping("/register")
    public String regist() {
        return "signUp";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/404")
    public String error() {
        return "404";
    }

}
