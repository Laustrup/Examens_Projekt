package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.services.ExceptionHandler;
import patrick_laust_ayo.examproject.services.ProjectCreator;
import patrick_laust_ayo.examproject.services.ProjectEditor;
import patrick_laust_ayo.examproject.services.UserCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ProjectController {

    private ProjectCreator projectCreator = new ProjectCreator();
    private ProjectEditor projectEditor = new ProjectEditor();
    private ExceptionHandler handler = new ExceptionHandler();


    @GetMapping("/create_project/{projectManager.getUsername}")
    public String renderCreateProject(Model model, HttpServletRequest request) {

           HttpSession session = request.getSession();
           String username = (String) session.getAttribute("username");

           model.addAttribute("username",username);

           return "create_project.html";

    }

    @PostMapping("/create-project")
    public String createProject(@RequestParam(name = "title") String title,
                                HttpServletRequest request,Model model) {

        HttpSession session = request.getSession();

        String inputException = handler.isLengthAllowedInDatabase(title,"title");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "project_page.html";
        }
        if (handler.doesProjectExist(title)) {
            model.addAttribute("Exception","Project already exists...");
            return "project_page.html";
        }
        String username = (String) session.getAttribute("username");

        session.setAttribute("project", projectCreator.createProject(title, username));
        session.setAttribute("projectTitle", title);

        return "redirect:/project_page/" + title + "/" +
                ((ProjectManager) session.getAttribute("projectManager")).getUsername() + "/" +
                ((ProjectManager) session.getAttribute("projectManager")).getPassword();

    }

    @PostMapping("/direct_project_page")
    public String goToChoosenProjectPage(@RequestParam(name = "projectTitle") String title, Model model, HttpServletRequest request) {
        ProjectRepository projectRepository = new ProjectRepository();
        HttpSession session = request.getSession();

        model.addAttribute("project",projectRepository.findProject(title));
        model.addAttribute("participant",session.getAttribute("current_participant"));

        return "redirect:/project_page/" + title + "/" + ((Participant) session.getAttribute("current_participant")).getId();
    }

    @GetMapping("/project_page/{project.getTitle()}/{participant.getId()}")
    public String renderProjectPage(@PathVariable(name = "project.getTitle()") String projectTitle,
                                    Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        model.addAttribute("project",session.getAttribute("project"));
/*
        if (handler.isParticipantPartOfProject(userId,projectTitle)) {
            //TODO
        }
*/
        return "project_page.html";
    }

}
