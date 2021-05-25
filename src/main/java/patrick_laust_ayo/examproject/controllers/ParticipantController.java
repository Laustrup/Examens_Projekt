package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.services.ExceptionHandler;
import patrick_laust_ayo.examproject.services.ProjectCreator;
import patrick_laust_ayo.examproject.services.UserCreator;
import patrick_laust_ayo.examproject.services.UserEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class ParticipantController {

    private UserCreator userCreator = new UserCreator();
    private UserEditor userEditor;

    @GetMapping("/participant_login_page")
    public String renderLoginParticipant(){
        return "participant_login.html";
    }

    @PostMapping("/participant_join_project")
    public String renderParticipantJoinProject(@RequestParam (name="participant_id") String userId,
                                               @RequestParam (name="participant_password") String password,
                                               HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        ExceptionHandler exceptionHandler = new ExceptionHandler();


        try {
            if (exceptionHandler.allowLogin(userId, password)){
                session.setAttribute("participant", userCreator.getParticipant(userId));
                return "redirect:/participant_dashboard/" + userId;
            }
        }
        catch (Exception e){
            System.out.println("Login is denied " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/participant_login_page";
    }

    @GetMapping("/participant_dashboard/{participant.getUserId()}")
    public String renderDashboard(@PathVariable (name="participant.getUserId()") String userId,
                                  Model model){
        ProjectCreator projectCreator = new ProjectCreator();
        model.addAttribute("projects", projectCreator.getProjects(userId));

        return "participant_dashboard";
    }

    @PostMapping("/join/{project.getTitle()}")
    public String joinProject(@PathVariable(name="project.getTitle()") String projectTitle,
                              @RequestParam(name="participant_ID") String id,
                              @RequestParam(name="password") String password, Model model,
                              HttpServletRequest request) {
        //TODO kan ikke få pathvariablen til at virke, den kan ikke finde ud af hvad project.getTitle er
        ExceptionHandler handler = new ExceptionHandler();
        ProjectRepository projectRepo = new ProjectRepository();
        ProjectCreator projectCreator = new ProjectCreator();
        HttpSession session = request.getSession();
        try {

            model.addAttribute("project",(Project) session.getAttribute("project"));
        }
        catch (Exception e){
            System.out.println("cannot add project to model (participant controller " + e.getMessage());
            e.printStackTrace();
        }
        if (handler.allowLogin(id, password)) {
          //  projectRepo.addParticipantToProject(userCreator.getParticipant(id),projectCreator.getProject(projectTitle));
            return "/" + projectTitle  + "/" + userCreator.getParticipant(id);
        }
        else {
            model.addAttribute("Exception","Wrong password!");
            return "/participant_join_project";
        }
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

    // TODO participant in endpoint is not set yet
    @PostMapping("/update_participant/{participant.getUserId}")
    public String updateParticipant(@RequestParam(name="participant_ID") String id, @RequestParam(name="participant_password") String password,
                                    @RequestParam(name="name") String name, @RequestParam(name="position") String position,
                                    @PathVariable("participant.getUserId") String formerUserId, HttpServletRequest request, Model model){

        ExceptionHandler handler = new ExceptionHandler();

        HttpSession session = request.getSession();
        Project project = (Project) session.getAttribute("project");

        if (handler.doesUserIdExist(id)) {
            model.addAttribute("User_id exists","User id already exists, please write another.");

            return "redirect:/project/" + project.getTitle() + "/User id already exists, please write another.";
        }

        String inputException = handler.isLengthAllowedInDatabase(id,"user_id");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "redirect:/project/" + project.getTitle() + "/" + inputException;
        }

        inputException = handler.isLengthAllowedInDatabase(password,"participant_password");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "redirect:/project/" + project.getTitle() + "/" + inputException;
        }

        inputException = handler.isLengthAllowedInDatabase(name,"pariticipant_name");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "redirect:/project/" + project.getTitle() + "/" + inputException;
        }

        inputException = handler.isLengthAllowedInDatabase(position,"position");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "redirect:/project/" + project.getTitle() + "/" + inputException;
        }

        session.setAttribute("current_participant", userEditor.updateParticipant(id, password, name, position, formerUserId));

        return "redirect://project_page/" + project.getTitle() + "/" + ((Participant)session.getAttribute("current_participant")).getName();

    }

    @GetMapping("/project/{project.getTitle()}/{Exception}")
    public String backToProjectWithException() {
        return "project";
    }

    /*
    //Login existing participant
    @PostMapping ("/participant_login")
    public String loginParticipant(@RequestParam(name="participant_ID") int id, @RequestParam(name="particiant_password") {
        String password, HttpServletRequest request)

        HttpSession session = request.getSession();

        return "";
    }

     */

    @GetMapping("/projectpage/{pName}")
    public String renderProjectpage (HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();

        return "";
    }
}