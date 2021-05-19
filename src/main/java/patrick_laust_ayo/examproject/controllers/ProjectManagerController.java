package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;
import patrick_laust_ayo.examproject.services.ExceptionHandler;
import patrick_laust_ayo.examproject.services.UserCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class  ProjectManagerController {

    private UserCreator userCreator = new UserCreator();
    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    @GetMapping("/create_projectmanager")
    public String renderCreateProjectManager(){
        return "create_projectmanager";
    }

    @PostMapping("/create_projectmanager")
    public String createProjectManagerLogin(@RequestParam(name="username") String username,
                                            @RequestParam(name="password") String password,
                                            HttpServletRequest request, Model model){


        if (exceptionHandler.doesProjectManagerUsernameExist(username)) {
            model.addAttribute("userAlreadyExist", "This username already exist. Please choose another.");
            return "create_projectmanager.html";
        }

        HttpSession session = request.getSession();

        username = exceptionHandler.stringInputToDbInsure(username);
        String inputException = exceptionHandler.isLengthAllowedInDatabase(username);

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "create_projectmanager.html";
        }

        password = exceptionHandler.stringInputToDbInsure(password);
        inputException = exceptionHandler.isLengthAllowedInDatabase(password);
        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "create_projectmanager.html";
        }

        ProjectManager projectManager = userCreator.createManager(username, password);
        session.setAttribute("username", username);
        session.setAttribute("password", password);
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
                                      Model model) {

        ExceptionHandler handler = new ExceptionHandler();

        if (handler.allowLogin(password)) {
            return "/" + username;
        }
        else {
            model.addAttribute("Exception","Wrong password!");
            return "projectmanager_login";
        }
    }

    @GetMapping("/{projectManager.getUsername}")
    public String renderDashboard(@PathVariable("projectManager.getUsername") String username,
                                  Model model, HttpServletRequest request) {

        ProjectManagerRepository repo = new ProjectManagerRepository();
        ProjectManager projectManager = userCreator.getProjectManager(username);
        model.addAttribute("projectManager",projectManager);

        HttpSession session = request.getSession();
        session.setAttribute("projectManager",projectManager);
        session.setAttribute("current_participant",projectManager);

        return "project_manager_dashboard";
    }

}
