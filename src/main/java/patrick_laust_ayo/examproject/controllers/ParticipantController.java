package patrick_laust_ayo.examproject.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.DepartmentRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.services.ExceptionHandler;
import patrick_laust_ayo.examproject.services.UserCreator;
import patrick_laust_ayo.examproject.services.UserEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class ParticipantController {

    private UserCreator userCreator;
    private UserEditor userEditor;

    @GetMapping("/participant_login_page")
    public String renderLoginParticipant(){
        return "participant_login.html";
    }

    @GetMapping("/participant_join_project")
    public String renderParticipantJoinProject(){
        return "participant_join_projcet.html";
    }

    @PostMapping("/login_through_{project.getTitle}")
    public String joinProject(@RequestParam(name="participant_ID") String id,
                              @RequestParam(name="password") String password,
                              @RequestParam(name="project_title") String projectTitle, Model model) {

        ExceptionHandler handler = new ExceptionHandler();
        ProjectRepository repo = new ProjectRepository();

        if (handler.allowLogin(password)) {
            Project project = repo.findProject(projectTitle);

            return "/" ;
        }
        else {
            model.addAttribute("Exception","Wrong password!");
            return "/participant_join_project";
        }
    }

    @PostMapping("/{project.getTitle()}/create_participant")
    public String addParticipantsToProject(@PathVariable("{project.getTitle()}") String projectTitle,
                                                @RequestParam(name = "department_name") String departmentName,
                                                @RequestParam(name = "amount") int amount, HttpServletRequest request) {

        DepartmentRepository repo = new DepartmentRepository();
        Department department = repo.findDepartment(departmentName);

        for (int i = 0; i < amount; i++) {
            userCreator.createParticipant(projectTitle,departmentName);
        }

        HttpSession session = request.getSession();

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

        id = handler.stringInputToDbInsure(id);
        String inputException = handler.isLengthAllowedInDatabase(id);

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "redirect:/project/" + project.getTitle() + "/" + inputException;
        }

        password = handler.stringInputToDbInsure(password);
        inputException = handler.isLengthAllowedInDatabase(password);

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "redirect:/project/" + project.getTitle() + "/" + inputException;
        }

        name = handler.stringInputToDbInsure(name);
        inputException = handler.isLengthAllowedInDatabase(name);

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "redirect:/project/" + project.getTitle() + "/" + inputException;
        }

        position = handler.stringInputToDbInsure(position);
        inputException = handler.isLengthAllowedInDatabase(position);

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