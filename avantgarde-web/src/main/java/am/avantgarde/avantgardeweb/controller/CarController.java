package am.avantgarde.avantgardeweb.controller;

import am.avantgarde.avantgardecommon.model.*;
import am.avantgarde.avantgardecommon.repository.*;
import am.avantgarde.avantgardeweb.security.SpringUser;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.List;

@Controller
public class CarController {


    @Value("C:\\Users\\edona\\IdeaProjects\\avantgarde\\src\\main\\resources\\static\\media\\620x485")
    private String bigPicDir;

    @Value("C:\\Users\\edona\\IdeaProjects\\avantgarde\\src\\main\\resources\\static\\media\\115x85")
    private String smallPicDir;

    @Value("C:\\Users\\edona\\IdeaProjects\\avantgarde\\src\\main\\resources\\static\\media\\270x230")
    private String mainPicDir;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CarImageRepository carImageRepository;

    @GetMapping("/addCars")
    public String addCars(ModelMap map) {

        map.addAttribute("brands", brandRepository.findAll());
        map.addAttribute("colors", Color.values());
        map.addAttribute("bodyTypes", BodyType.values());
        return "addCars";
    }

    @PostMapping("/addCar")
    public String addCar(@ModelAttribute Car car, @ModelAttribute CarImages carImages,
                         @RequestParam("carImages") List<MultipartFile> bigFile,
                         @RequestParam("picture") MultipartFile main) throws IOException {
        //car detail page images


        String mainFileName = System.currentTimeMillis() + "_" + main.getOriginalFilename();
        File mainPic = new File(mainPicDir + File.separator + mainFileName);
        main.transferTo(mainPic);
        car.setPicUrl(mainFileName);

        carRepository.save(car);

            List<CarImages> carImage = new ArrayList<>();
        for (MultipartFile big: bigFile) {
            CarImages carImages1 = new CarImages();
            String bigFileName = System.currentTimeMillis() + "_" + big.getOriginalFilename();
            File bigPicFile = new File(bigPicDir + File.separator + bigFileName);
            big.transferTo(bigPicFile);
            carImages1.setCar(car);
            carImages1.setPicUrl(bigFileName);
            carImage.add(carImages1);
        }
        carImageRepository.saveAll(carImage);

        return "redirect:/addCars";
    }



    @GetMapping("/getCarDetails")
    public String getCarDetails(@RequestParam("id") int id, @AuthenticationPrincipal SpringUser springUser, ModelMap map){

        if(springUser != null && springUser.getUser() != null) {
            Optional<User> user = userRepository.findById(springUser.getUser().getId());
            if (user.isPresent())
                map.addAttribute("user", user);
        }

            Optional<Car> byId = carRepository.findById(id);
            if (byId.isPresent()) {
                map.addAttribute("car", byId.get());
                map.addAttribute("carImages", carImageRepository.findByCarId(byId.get().getId()));
                return "carDetails";
            }
        return "redirect:/";
    }

    @GetMapping("/getCarMainImage")
    public void getMainImage(HttpServletResponse response, @RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(mainPicDir + File.separator + picUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @GetMapping("/getCarSmallImage")
    public void getSmallImage(HttpServletResponse response, @RequestParam("smallPic") String picUrl) throws IOException {
        InputStream in = new FileInputStream(smallPicDir + File.separator + picUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @GetMapping("/getCarImages")
    public void getBigImage(HttpServletResponse response, @RequestParam("carImages") String picUrl) throws IOException {
        InputStream in = new FileInputStream(bigPicDir + File.separator + picUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

}
