package am.avantgarde.avantgardeweb.controller;

import am.avantgarde.avantgardecommon.model.Brand;
import am.avantgarde.avantgardecommon.model.Car;
import am.avantgarde.avantgardecommon.model.User;
import am.avantgarde.avantgardecommon.repository.BrandRepository;
import am.avantgarde.avantgardecommon.repository.CarRepository;
import am.avantgarde.avantgardecommon.repository.UserRepository;
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
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BrandController {

    @Value("C:\\Users\\edona\\IdeaProjects\\qwe\\avantgarde\\src\\main\\resources\\static\\media\\brands")
    private String brandPicDir;


    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/brands")
    public String main(ModelMap map,
                       @RequestParam("page") Optional<Integer> page,
                       @RequestParam("size") Optional<Integer> size,
                       Locale locale,
                       @AuthenticationPrincipal SpringUser springUser,
                       @RequestParam("id") int id) {
        if (springUser != null && springUser.getUser() != null) {
            Optional<User> user = userRepository.findById(springUser.getUser().getId());
            if (user.isPresent())
                map.addAttribute("user", user);
        }
        Optional<Brand> byId = brandRepository.findById(id);
        if (byId.isPresent()) {
//            map.addAttribute("cars",carRepository.findByBrandId(byId.get().getId()));
            map.addAttribute("brands", brandRepository.findAll());
            map.addAttribute("brand", byId.get());
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(3);
            Page<Car> all = carRepository.findByBrandId(byId.get().getId(), PageRequest.of(currentPage - 1, pageSize));
            map.addAttribute("carPage", all);
            int totalPages = all.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                map.addAttribute("pageNumbers", pageNumbers);


                return "carListByBrand";
            }
        }

        return "redirect:/";
    }


    @PostMapping("/addBrand")
    public String addBrand(@ModelAttribute Brand brand, @RequestParam("picture") MultipartFile file) throws IOException {
        String brandPic = System.currentTimeMillis() + '_' + file.getOriginalFilename();
        File file1 = new File(brandPicDir + File.separator + brandPic);
        file.transferTo(file1);
        brand.setPicUrl(brandPic);

        brandRepository.save(brand);

        return "redirect:/addCars";

    }

    @GetMapping("/getBrandImage")
    public void getImageAsByteArray(HttpServletResponse response, @RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(brandPicDir + File.separator + picUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

}
