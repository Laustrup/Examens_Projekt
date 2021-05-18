package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.services.ExceptionHandler;
import patrick_laust_ayo.examproject.services.ProjectCreator;
import patrick_laust_ayo.examproject.services.ProjectEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ProjectController {

    ProjectCreator projectCreator = new ProjectCreator();
    ProjectEditor projectEditor = new ProjectEditor();
    ExceptionHandler exceptionHandler = new ExceptionHandler();
    Project project;

    //TODO hvad gives med når man opretter projekt udover titel?
    @GetMapping("/create_project")
    public String renderCreateProject(Model model, HttpServletRequest request) {

           HttpSession session = request.getSession();
           String username = (String) session.getAttribute("username");

           model.addAttribute("username",username);

           return "create_project";

    }

    @PostMapping("/create-project")
    public String createProject(@RequestParam(name = "title") String title, String username, HttpServletRequest request) {

        HttpSession session = request.getSession();

        title = exceptionHandler.isLengthAllowedInDatabase(title);
        title = exceptionHandler.stringInputToDbInsure(title);

        username = (String) session.getAttribute("username");
        Project newProject = projectCreator.createProject(title, username);

        session.setAttribute("projectTitle", title);

        return "project_page.html";
    }


        @PostMapping("/direct_project_page")
        public String directToProjectPage(@RequestParam(name = "title") String title) {
        ProjectRepository projectRepository = new ProjectRepository();
        project = projectRepository.findProject(title);
        return "redirect:/project_page/" + title;


        }

        @GetMapping("/project_page/{project.getTitle}")
                public String renderProjectPage(Model model, HttpServletRequest request) {
        

    }

}
