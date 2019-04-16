package am.avantgarde.avantgardeweb.controller;

import am.avantgarde.avantgardecommon.model.Brand;
import am.avantgarde.avantgardecommon.repository.BrandRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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

@Controller
public class BrandController {

    @Value("C:\\Users\\edona\\IdeaProjects\\avantgarde\\src\\main\\resources\\static\\media\\brands")
    private String brandPicDir;


    @Autowired
    private BrandRepository brandRepository;


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
