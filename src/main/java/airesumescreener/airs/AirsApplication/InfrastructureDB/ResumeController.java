package airesumescreener.airs.AirsApplication.InfrastructureDB;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/resumecontroller")
public class ResumeController {

    private final ResumeRepository resumerepository;


    public ResumeController(ResumeRepository resumerepository) {
        this.resumerepository = resumerepository;
    }

    @PostMapping("/upload")
    public String uploadResume(@RequestParam("file") MultipartFile file){
        resumerepository.processResume(file);
        return "File received! Check your IntelliJ logs for the text.";
    }

    @PostMapping("/match")
    public List<String> matchJob(@RequestBody String jobDescription) {
        System.out.println("Received Job Description: " + jobDescription);
        return resumerepository.findMatchingResume(jobDescription);
    }

    @PostMapping("/rankmatch")
    public List<String> rankmatch(@RequestBody String jd){
        return resumerepository.findMatchingResume(jd);
    }
}
