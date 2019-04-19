package am.avantgarde.avantgardeweb.controller;

import am.avantgarde.avantgardecommon.model.*;
import am.avantgarde.avantgardecommon.repository.*;
import am.avantgarde.avantgardecommon.util.DateUtil;
import am.avantgarde.avantgardeweb.security.SpringUser;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class NewsController {

    @Value("C:\\Users\\edona\\IdeaProjects\\qwe\\avantgarde\\src\\main\\resources\\static\\media\\main-slider")
    private String announcePicDir;

    @Value("C:\\Users\\edona\\IdeaProjects\\avantgarde-project\\avantgarde-web\\src\\main\\resources\\static\\news-images\\main-image")
    private String mainPicDir;

    @Value("C:\\Users\\edona\\IdeaProjects\\avantgarde-project\\avantgarde-web\\src\\main\\resources\\static\\news-images\\other")
    private String otherPicDir;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarAnnouncementRepository carRepo;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsImageRepository newsImageRepository;

    @GetMapping("/newsDetails")
    public String newsDetail(@RequestParam("id") int id, ModelMap map) {
        Optional<News> byId = newsRepository.findById(id);
        if (byId.isPresent()) {
            map.addAttribute("news", byId.get());
            map.addAttribute("newsImages",newsImageRepository.findByNewsId(byId.get().getId()));
            return "news-detail";
        }
        return "redirect:/";
    }

    @GetMapping("/news")
    public String main(ModelMap map,
                       @RequestParam("page") Optional<Integer> page,
                       @RequestParam("size") Optional<Integer> size,
                       Locale locale) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Page<News> all = newsRepository.findAllByOrderByIdDesc(PageRequest.of(currentPage - 1, pageSize));
        map.addAttribute("newsPage", all);
        int totalPages = all.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            map.addAttribute("pageNumbers", pageNumbers);
            map.addAttribute("popNews", newsRepository.findTop5ByOrderById());
            return "news";
        }
        return "redirect:/";
    }

    /*
    Add News
     */

    @GetMapping("/addNews")
    public String addNews(ModelMap map) {
        map.addAttribute("brands",brandRepository.findAll());
        map.addAttribute("cars",carRepository.findAll());
        return "addNews";
    }

    @PostMapping("/addNews")
    public String addNews(@AuthenticationPrincipal SpringUser user, @ModelAttribute News news, @ModelAttribute NewsImages newsImages,
                          @RequestParam("newsImages") List<MultipartFile> images,
                          @RequestParam("picture") MultipartFile main) throws IOException {

        String mainFileName = System.currentTimeMillis() + '_' + main.getOriginalFilename();
        File mainPic = new File(mainPicDir + File.separator + mainFileName);
        main.transferTo(mainPic);
        news.setPicUrl(mainFileName);
        news.setUser(user.getUser());
        news.setCreatedDate(DateUtil.convertDateToString(new Date()));

        newsRepository.save(news);

        List<NewsImages> newsImage = new ArrayList<>();
        for (MultipartFile image : images) {
            NewsImages newsImages1 = new NewsImages();
            String images1 = System.currentTimeMillis() + '_' + image.getOriginalFilename();
            File imageFile = new File(otherPicDir + File.separator + images1);
            image.transferTo(imageFile);
            newsImages1.setNews(news);
            newsImages1.setPicUrl(images1);
            newsImage.add(newsImages1);
        }

        newsImageRepository.saveAll(newsImage);

        return "redirect:/addNews";
    }

    /*
    News Images
     */
    @GetMapping("/getNewsMainImage")
    public void getNewsMainImage(HttpServletResponse response, @RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(mainPicDir + File.separator + picUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @GetMapping("/getNewsOtherImages")
    public void getNewsOtherImage(HttpServletResponse response, @RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(otherPicDir + File.separator + picUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    /*
    Announcement
     */

    @PostMapping("/addAnnouncement")
    public String addAnnouncement(@ModelAttribute CarAnnouncement carAnnouncement, @RequestParam("picture") MultipartFile file) throws IOException {

        String pic = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File mainPic = new File(announcePicDir + File.separator + pic);
        file.transferTo(mainPic);
        carAnnouncement.setPicUrl(pic);

        carRepo.save(carAnnouncement);

        return "redirect:/";
    }

    @GetMapping("/getAnnouncementImage")
    public void getMainImage(HttpServletResponse response, @RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(announcePicDir + File.separator + picUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }
}
