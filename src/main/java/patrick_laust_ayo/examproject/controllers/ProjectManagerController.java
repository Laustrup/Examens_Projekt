package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.services.ExceptionHandler;
import patrick_laust_ayo.examproject.services.ProjectCreator;
import patrick_laust_ayo.examproject.services.UserCreator;
import patrick_laust_ayo.examproject.services.UserEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class  ProjectManagerController {

    private UserCreator creator = new UserCreator();
    private ExceptionHandler handler = new ExceptionHandler();
    private UserEditor editor = new UserEditor();

    @GetMapping("/create_new_projectmanager")
    public String renderCreateProjectManager(){
        return "create_projectmanager.html";
    }

    @PostMapping("/create_projectmanager")
    public String createProjectManagerLogin(@RequestParam(name="username") String username,
                                            @RequestParam(name="password") String password,
                                            HttpServletRequest request, Model model){


        if (handler.doesUserIdExist(username)) {
            model.addAttribute("Exception", "This username already exist. Please choose another.");
            System.out.println(handler.doesUserIdExist(username));
            return "create_projectmanager.html";
        }

        HttpSession session = request.getSession();

        String inputException = handler.isLengthAllowedInDatabase(username,"username");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "create_projectmanager.html";
        }

        inputException = handler.isLengthAllowedInDatabase(password,"participant_password");
        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "create_projectmanager.html";
        }

        ProjectManager projectManager = creator.createManager(username, password);
        session.setAttribute("username", username);
        session.setAttribute("password", password);
        session.setAttribute("projectManager", projectManager);

        model.addAttribute("projectManager",projectManager);

        return "redirect:/create_project/" + projectManager.getUsername();

    }

    @GetMapping("/manager_login")
    public String renderProjectManagerLogin(HttpServletRequest request){
    return "projectmanager_login";
    }

    @PostMapping("/allow_password")
    public String loginProjectManager(@RequestParam(name="password") String password,
                                        @RequestParam(name="username") String username,
                                      HttpServletRequest request,Model model) {

        ExceptionHandler handler = new ExceptionHandler();

        if (handler.allowLogin(username, password)) {

            HttpSession session = request.getSession();
            session.setAttribute("projectManager", creator.getProjectManager(username));
            session.setAttribute("participant", creator.getParticipant(username));

            return "/" + username;
        }
        else {
            model.addAttribute("Exception","Wrong username or password!");
            return "/manager_login";
        }
    }

    @GetMapping("/{projectManager.getId()}")
    public String renderDashboard(@PathVariable("projectManager.getId") String userId,
                                  Model model, HttpServletRequest request) {

        ProjectManager projectManager = creator.getProjectManager(userId);
        ProjectCreator projectCreator = new ProjectCreator();

        ArrayList<Project> projects = projectCreator.getProjects(userId); //vi kalder det id i metoden

        model.addAttribute("projects",projects);
        model.addAttribute("projectManager", projectManager);

        HttpSession session = request.getSession();
        session.setAttribute("projects",projects);

        return "projectmanager_dashboard";
    }

    @PostMapping("/{project.getTitle()}/add_participant")
    public String addParticipantsToProject(@PathVariable("project.getTitle()") String projectTitle,
                                           @RequestParam(name = "department_name") String departmentName,
                                           @RequestParam(name = "amount") int amount, HttpServletRequest request) {

        HttpSession session = request.getSession();

        for (int i = 0; i < amount; i++) {
            creator.createParticipant(projectTitle,departmentName);
        }

        // TODO Perhaps key is for wrong participant...
        Participant participant = (Participant) session.getAttribute("participant");

        return "redirect://project_page/" + projectTitle + "/" + participant.getName();
    }

    /*
    // TODO Use delete participant delete?
    @GetMapping("/accept_delete_of_{projectManager.getUsername()}")
    public String renderDeleteProjectManager() {
        return "accept_delete_of_project";
    }

    @PostMapping("/delete_{projectManager.getUsername()/{projectManager.getPassword()}")
    public String deleteProjectManager(@RequestParam(name = "project_title") String projectTitle,
                                       @RequestParam(name = "password") String password,
                                       @RequestParam(name = "user_id") String userId,
                                       Model model, HttpServletRequest request) {

        if (handler.allowLogin(userId, password)) {
            HttpSession session = request.getSession();
            editor.
            session.invalidate();
            model.addAttribute("message","Project manager is now deleted");
            return "/";
        }

        model.addAttribute("message","Project manager is now deleted");
    }

     */

}
