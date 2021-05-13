package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class  ProjectManagerController {

    @GetMapping("/create_manager_login")
    public String createManagerLogin(){
        return "create_projectmanager";
    }
    @PostMapping("/submit_login1")
    public String submitFirstManagerLogin(@RequestParam(name="username") String username,
                                          @RequestParam(name="password") String password, HttpServletRequest request){

    }

    @GetMapping("/manager_login")
    public String loginProjectManager(){
        return "projectmanager_login";
    }




    /* @PostMapping  skal indgå som en del af create project
    public String createParticipant(@RequestParam(name="project_title") String projectTitle,  HttpServletRequest request){

        userCreator.createParticipant(projectTitle);

        return "redirct:/projectPage";

    */
}
