package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.services.UserCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class  ProjectManagerController {

    UserCreator userCreator = new UserCreator();

    @GetMapping("/create_projectmanager")
    public String renderCreateProjectManager(){
        return "create_projectmanager";
    }

    @PostMapping("/create_projectmanager")
    public String createProjectManagerLogin(@RequestParam(name="username") String username,
                                          @RequestParam(name="password") String password, HttpServletRequest request){
        HttpSession session = request.getSession();

        ProjectManager projectManager = userCreator.createManager(username, password);

        session.setAttribute("manager_password", password);

        return "redirect:/create_project";
    }

    @GetMapping("/manager_login")
    public String renderProjectManagerLogin(HttpServletRequest request){
    return "projectmanager_login";
    }

    @PostMapping("/manager_login")
      public String loginProjectManager(@RequestParam(name="password") String password, HttpServletRequest request){

      HttpSession session = request.getSession();
      
      session.getAttribute("manager_password");

      return ""

    /* @PostMapping  skal indgå som en del af create project
    public String createParticipant(@RequestParam(name="project_title") String projectTitle,  HttpServletRequest request){

        userCreator.createParticipant(projectTitle);

        return "redirct:/projectPage";

    */
}
