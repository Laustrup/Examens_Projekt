package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private ProjectCreator projectCreator = new ProjectCreator();
    private ProjectEditor projectEditor = new ProjectEditor();
    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    @GetMapping("/create_project/{projectManager.getUsername}")
    public String renderCreateProject(Model model, HttpServletRequest request) {

           HttpSession session = request.getSession();
           String username = (String) session.getAttribute("username");

           model.addAttribute("username",username);

           return "create_project.html";

    }

    @PostMapping("/create-project")
    public String createProject(@RequestParam(name = "title") String title, HttpServletRequest request,Model model) {

        HttpSession session = request.getSession();

        String inputException = exceptionHandler.isLengthAllowedInDatabase(title);

        title = exceptionHandler.stringInputToDbInsure(title);

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "project_page.html";
        }
        String username = (String) session.getAttribute("username");

        session.setAttribute("project", projectCreator.createProject(title, username));

        return "project_page.html";


    }


    @PostMapping("/direct_project_page")
    public String directToProjectPage(@RequestParam(name = "title") String title, Model model) {
        ProjectRepository projectRepository = new ProjectRepository();

        model.addAttribute("project",projectRepository.findProject(title));

        return "redirect:/project_page/" + title;
    }

    @GetMapping("/project_page/{project.getTitle}")
    public String renderProjectPage(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();

        model.addAttribute("project",session.getAttribute("project"));

        return "project.html";

    }

}
