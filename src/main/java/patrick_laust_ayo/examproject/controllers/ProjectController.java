package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import patrick_laust_ayo.examproject.models.*;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.services.ExceptionHandler;
import patrick_laust_ayo.examproject.services.ProjectCreator;
import patrick_laust_ayo.examproject.services.ProjectEditor;
import patrick_laust_ayo.examproject.services.UserCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class ProjectController {

    private ProjectCreator projectCreator = new ProjectCreator();
    private ProjectEditor projectEditor = new ProjectEditor();
    private ExceptionHandler handler = new ExceptionHandler();


    @GetMapping("/create_project/{projectManager.getUsername}")
    public String renderCreateProject(Model model, HttpServletRequest request) {
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

        session.setAttribute("project", projectCreator.createProject(title, ((ProjectManager)session.getAttribute("projectManager")).getUsername()));

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

    @GetMapping("/projectpage-{project.getTitle()}/{participant.getId()}")
    public String renderProjectpage(@PathVariable(name = "project.getTitle()") String projectTitle,
                                    @PathVariable(name = "participant.getId()") String userId,
                                    HttpServletRequest request,Model model) {

        if (handler.isParticipantPartOfProject(userId,projectTitle)) {
            HttpSession session = request.getSession();
            Project project = new ProjectCreator().getProject(projectTitle);

            session.setAttribute("project",project);
            model.addAttribute("project",project);
            model.addAttribute("participant",new UserCreator().getParticipant(userId));

            return "project_page";
        }
        else {
            model.addAttribute("Exception","You are not a participant of this project...");
            return "/participant_dashboard/" + userId;
        }
    }

    @GetMapping("/delete_{project.getTitle()/{projectManager.getPassword()}")
    public String deleteProject(@PathVariable(name = "project.getTitle()") String projectTitle,
                                @PathVariable(name = "projectManager.getPassword()") String password,
                                Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (((ProjectManager)session.getAttribute("projectManager")).getPassword().equals(password)) {
            new ProjectEditor().deleteProject(projectTitle);
            model.addAttribute("message",projectTitle + " has been deleted.");
            return "/" + ((ProjectManager)session.getAttribute("projectManager")).getId();
        }
        else {
            model.addAttribute("message","Password not correct...");
            return "/projectpage-" + projectTitle + "/" + ((Participant)session.getAttribute("participant")).getId();
        }
    }


    //
    //

    @PostMapping("/add_phase_to_{project.getTitle()}")
    public String addPhase(@PathVariable(name="project.getTitle()") String projectTitle,
                                HttpServletRequest request, Model model) {

        Phase phase = projectCreator.createPhase(projectTitle);

        HttpSession session = request.getSession();
        session.setAttribute("phase",phase);
        session.setAttribute("project",projectCreator.getProject(projectTitle));

        return "/projectpage-"+ projectTitle + "/" + phase.getTitle();
    }

    @PostMapping("/direct_to_phase")
    public String directToPhase(@RequestParam(name="phase_title") String phaseTitle,
                                @RequestParam(name="project_title") String projectTitle,
                                HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute("phase",projectCreator.getPhase(phaseTitle, projectTitle));

        return "/projectpage-" + projectTitle + "/" + phaseTitle;
    }

    // TODO Create phase html
    @GetMapping("/projectpage-{project.getTitle()}/{phase.getTitle()}")
    public String renderPhase() {
        return "phase";
    }

    @PostMapping("/add_assignment_to_{project.getTitle()}")
    public String addAssignment(@RequestParam(name="title") String title,
                                @RequestParam(name="start") String start,
                                @RequestParam(name="end") String end,
                                HttpServletRequest request, Model model) {

        Assignment assignment = projectCreator.createAssignment(title,start,end);

        HttpSession session = request.getSession();
        session.setAttribute("assignment",assignment);

        return "/projectpage-" + ((Project)session.getAttribute("project")).getTitle() + "/" + assignment.getTitle();
    }

    @PostMapping("/direct_to_assignment")
    public String directToAssignment(@RequestParam(name="assignment_title") String assignmentTitle,
                                HttpServletRequest request) {

        HttpSession session = request.getSession();
        //session.setAttribute("assignment",projectCreator.getAssignment(assignmentTitle));
        return "/projectpage-" + ((Project)session.getAttribute("project")).getTitle() + "/" + assignmentTitle;
    }

    // TODO Create assignment html
    @GetMapping("/projectpage-{project.getTitle()}/{assignment.getTitle()}")
    public String renderAssignment() {
        return "assignment";
    }

}
