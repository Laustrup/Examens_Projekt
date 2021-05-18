package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.services.ProjectCreator;
import patrick_laust_ayo.examproject.services.ProjectEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ProjectController {

    ProjectCreator projectCreator = new ProjectCreator();
    ProjectEditor projectEditor = new ProjectEditor();

    //TODO hvad gives med når man opretter projekt udover titel?
    @PostMapping("/create-project")
    public String createProject(@RequestParam(name = "title") String title, String managerName, HttpServletRequest request){

        HttpSession session = request.getSession();

        Project newProject = projectCreator.createProject(title, managerName);




return "";
    }

}
