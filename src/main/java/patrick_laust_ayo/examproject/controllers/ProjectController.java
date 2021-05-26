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
    public String renderCreateProject() {
        return "create_project.html";
    }

    @PostMapping("/create-project")
    public String createProject(@RequestParam(name = "title") String title,
                                HttpServletRequest request,Model model) {

        HttpSession session = request.getSession();
        String username = ((ProjectManager) session.getAttribute("projectManager")).getUsername();

        String inputException = handler.isLengthAllowedInDatabase(title,"title");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "create_project/" + username;
        }
        if (handler.doesProjectExist(title)) {
            model.addAttribute("Exception","Project already exists...");
            return "create_project/" + username;
        }

        session.setAttribute("project", projectCreator.createProject(title, ((ProjectManager)session.getAttribute("projectManager")).getUsername()));

        return "redirect:/project_page/" + title + "/" + username;

    }

    @PostMapping("/direct_project_page")
    public String goToChoosenProjectPage(@RequestParam(name = "projectTitle") String title, Model model, HttpServletRequest request) {
        ProjectRepository projectRepository = new ProjectRepository();
        HttpSession session = request.getSession();

        model.addAttribute("project",projectRepository.findProject(title));
        model.addAttribute("participant",session.getAttribute("participant"));

        return "redirect:/project_page/" + title + "/" + ((Participant) session.getAttribute("participant")).getId();
    }

    // TODO Does all three cases work with this endpoint? Redirect needs html
    @GetMapping("/projectpage-{project.getTitle()}/{participant.getId()}")
    public String renderProjectpage(@PathVariable(name = "project.getTitle()") String projectTitle,
                                    @PathVariable(name = "participant.getId()") String userId,
                                    HttpServletRequest request,Model model) {

        HttpSession session = request.getSession();

        // Aldrig logget ind
        if (((Participant) session.getAttribute("participant")).getId() == null) {
            // Needs create participant method in participant controller
        }
        // Får direkte adgang da han er del af projectet
        else if (handler.isParticipantPartOfProject(userId,projectTitle)) {

            Project project = new ProjectCreator().getProject(projectTitle);

            session.setAttribute("project",project);
            model.addAttribute("project",project);
            model.addAttribute("participant",new UserCreator().getParticipant(userId));

            return "project_page";
        }

        model.addAttribute("Exception","You are not a participant of this project...");
        return "redirect://login_to_project/" + userId+ "/" + projectTitle;
    }

    // TODO Create html
    @GetMapping("/accept_delete_of_{project.getTitle()}")
    public String renderDeleteProject() {
        return "accept_delete_of_project";
    }

    @PostMapping("/delete_{project.getTitle()/{projectManager.getPassword()}")
    public String deleteProject(@RequestParam(name = "project_title") String projectTitle,
                                @RequestParam(name = "password") String password,
                                @RequestParam(name = "user_id") String userId, Model model) {

        if (handler.allowLogin(userId, password)) {
            projectEditor.deleteProject(projectTitle);
            model.addAttribute("message","Project is now deleted");
            return "/" + userId;
        }

        model.addAttribute("message","Project manager is now deleted");
        return "/accept_delete_of_" + projectTitle;
    }

    //
    //

    @PostMapping("/add_phase_to_{project.getTitle()}")
    public String addPhase(@PathVariable(name="project.getTitle()") String projectTitle, HttpServletRequest request, Model model) {

        Phase phase = projectCreator.createPhase(projectTitle);

        HttpSession session = request.getSession();
        session.setAttribute("phase",phase);
        //session.setAttribute("project",projectCreator.getProject(projectTitle));

        return "/projectpage-" + projectTitle + "/" + phase.getTitle();
    }

    @GetMapping("/projectpage-{project.getTitle()}/{phase.getTitle()}")
    public String updatePhase(@RequestParam(name="new_title") String newTitle, HttpServletRequest request,Model model) {
        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();

        Phase phase = projectEditor.updatePhase(newTitle,phaseTitle, projectTitle);

        session.setAttribute("phase",phase);
        model.addAttribute("phase",phase);

        return "projectpage-"+projectTitle+"/"+phaseTitle;
    }

    // TODO Perhaps make submitvalue with both title and split in method?
    @PostMapping("/direct_to_phase")
    public String directToPhase(@RequestParam(name="phase_title") String phaseTitle, HttpServletRequest request) {

        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();

        Phase phase = projectCreator.getPhase(phaseTitle, projectTitle);

        session.setAttribute("phase",phase);

        return "/projectpage-" + projectTitle + "/" + phaseTitle;
    }

    // TODO Create phase html
    @GetMapping("/projectpage-{project.getTitle()}/{phase.getTitle()}")
    public String renderPhase(@PathVariable(name = "project.getTitle()") String projectTitle,
                              @PathVariable(name = "phase.getTitle()") String phaseTitle, Model model) {

        model.addAttribute("project",projectCreator.getProject(projectTitle));
        model.addAttribute("phase",projectCreator.getPhase(phaseTitle,projectTitle));

        return "phase";
    }

    @PostMapping("/add_assignment_to_{project.getTitle()}")
    public String addAssignment(@RequestParam(name="title") String title,
                                @RequestParam(name="start") String start,
                                @RequestParam(name="end") String end, HttpServletRequest request) {

        Assignment assignment = projectCreator.createAssignment(title,start,end);

        HttpSession session = request.getSession();
        session.setAttribute("assignment",assignment);

        return "/projectpage-" + ((Project)session.getAttribute("project")).getTitle() + "/" + assignment.getTitle();
    }

    @PostMapping("/direct_to_assignment")
    public String directToAssignment(@RequestParam(name="assignment_title") String assignmentTitle, HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute("assignment",projectCreator.getAssignment(assignmentTitle,((Phase)session.getAttribute("phase")).getTitle()));
        return "/projectpage-" + ((Project)session.getAttribute("project")).getTitle() + "/" + assignmentTitle;
    }

    // TODO Create assignment html
    @GetMapping("/projectpage-{project.getTitle()}/{assignment.getTitle()}")
    public String renderAssignment(@PathVariable(name = "project.getTitle()") String projectTitle,
                                   @PathVariable(name = "assignment.getTitle()") String assignmentTitle,
                                   Model model,HttpServletRequest request) {

        HttpSession session = request.getSession();
        model.addAttribute("project",(Project)session.getAttribute("project"));
        model.addAttribute("assignment",projectCreator.getAssignment(((Phase)session.getAttribute("phase")).getTitle(), projectTitle));

        return "assignment";
    }

}
