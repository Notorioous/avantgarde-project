package am.avantgarde.avantgardeweb.controller;

import am.avantgarde.avantgardecommon.model.CarAnnouncement;
import am.avantgarde.avantgardecommon.repository.CarAnnouncementRepository;
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
public class NewsController {

    @Value("C:\\Users\\edona\\IdeaProjects\\avantgarde\\src\\main\\resources\\static\\media\\main-slider")
    private String announcePicDir;

    @Autowired
    private CarAnnouncementRepository carRepo;

    @GetMapping("/addNews")
    public String addNews(){
        return "addNews";
    }

    @PostMapping("/addAnnouncement")
    public String addAnnouncement(@ModelAttribute CarAnnouncement carAnnouncement, @RequestParam("picture")MultipartFile file) throws IOException {

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
