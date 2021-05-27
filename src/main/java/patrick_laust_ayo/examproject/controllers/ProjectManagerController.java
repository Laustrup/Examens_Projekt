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
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.services.ExceptionHandler;
import patrick_laust_ayo.examproject.services.ProjectCreator;
import patrick_laust_ayo.examproject.services.UserCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class  ProjectManagerController {

    private UserCreator userCreator = new UserCreator();
    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    @GetMapping("/create_new_projectmanager")
    public String renderCreateProjectManager(){
        return "create_projectmanager.html";
    }

    @PostMapping("/create_projectmanager")
    public String createProjectManagerLogin(@RequestParam(name="username") String username,
                                            @RequestParam(name="password") String password,
                                            HttpServletRequest request, Model model){


        if (exceptionHandler.doesUserIdExist(username)) {
            model.addAttribute("Exception", "This username already exist. Please choose another.");
            System.out.println(exceptionHandler.doesUserIdExist(username));
            return "create_projectmanager.html";
        }

        HttpSession session = request.getSession();

        String inputException = exceptionHandler.isLengthAllowedInDatabase(username,"username");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "create_projectmanager.html";
        }

        inputException = exceptionHandler.isLengthAllowedInDatabase(password,"participant_password");
        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "create_projectmanager.html";
        }

        ProjectManager projectManager = userCreator.createManager(username, password);
        session.setAttribute("username", username);
        session.setAttribute("password", password);
        session.setAttribute("projectManager", projectManager);

        model.addAttribute("projectManager",projectManager);

        return "redirect:/create_project/" + projectManager.getUsername();

    }

    @GetMapping("/manager_login")
    public String renderProjectManagerLogin(){
        return "projectmanager_login";
    }

    @PostMapping("/allow_password")
    public String loginProjectManager(@RequestParam(name="manager_password") String password,
                                        @RequestParam(name="manager_username") String username,
                                      HttpServletRequest request,Model model) {

        ExceptionHandler handler = new ExceptionHandler();

        if (handler.allowLogin(username, password)) {

            HttpSession session = request.getSession();
            session.setAttribute("projectManager",userCreator.getProjectManager(username));
            session.setAttribute("participant",userCreator.getParticipant(username));
            model.addAttribute("projectManager",((ProjectManager) session.getAttribute("projectManager")));
            System.out.println(((ProjectManager) session.getAttribute("projectManager")).getId());
            return "redirect:/manager_dashboard/" + username;
        }
        else {
            model.addAttribute("Exception","Wrong username or password!");
            return "/manager_login";
        }
    }

    @GetMapping("/manager_dashboard/{manager-id}")
    public String renderDashboard(@PathVariable("manager-id") String userId,
                                  Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        ProjectManager projectManager = userCreator.getProjectManager(userId);
        ProjectCreator projectCreator = new ProjectCreator();

        //ArrayList<Project> projects = projectCreator.getProjects(userId); //vi kalder det id i metoden

        //session.setAttribute("projects",projects);

        //model.addAttribute("projects",projects);
        model.addAttribute("projectManager", projectManager);

        return "projectmanager_dashboard";
    }

    @PostMapping("/{project.getTitle()}/add_participant")
    public String addParticipantsToProject(@PathVariable("project.getTitle()") String projectTitle,
                                           @RequestParam(name = "department_name") String departmentName,
                                           @RequestParam(name = "amount") int amount, HttpServletRequest request) {

        HttpSession session = request.getSession();

        for (int i = 0; i < amount; i++) {
            userCreator.createParticipant(projectTitle,departmentName);
        }

        // TODO Perhaps key is for wrong participant...
        Participant participant = (Participant) session.getAttribute("participant");

        return "redirect://project_page/" + projectTitle + "/" + participant.getName();
    }

}
