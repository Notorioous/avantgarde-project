package am.avantgarde.avantgardeweb.controller;

import am.avantgarde.avantgardecommon.model.Brand;
import am.avantgarde.avantgardecommon.model.News;
import am.avantgarde.avantgardecommon.model.User;
import am.avantgarde.avantgardecommon.repository.BrandRepository;
import am.avantgarde.avantgardecommon.repository.NewsRepository;
import am.avantgarde.avantgardeweb.security.SpringUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice()
public class HeaderController {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private NewsRepository newsRepository;

    @ModelAttribute("brandList")
    public List<Brand> getBrands() {
        return brandRepository.findAll();
    }

    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null && springUser.getUser() != null) {
            return springUser.getUser();
        }
        return null;

    }

    @ModelAttribute("newsList")
    public List<News> getNews(){
        return newsRepository.findAll();
    }

}
