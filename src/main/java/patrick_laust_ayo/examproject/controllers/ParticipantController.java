package patrick_laust_ayo.examproject.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.services.ExceptionHandler;
import patrick_laust_ayo.examproject.services.UserCreator;
import patrick_laust_ayo.examproject.services.UserEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ParticipantController {

    private UserCreator userCreator;
    private UserEditor userEditor;

    @GetMapping("/login")
    public String renderLoginParticipant(){
        return "participant_login.html";
    }

    @PostMapping("/update_participant")
    public String updateParticipant(@RequestParam(name="participant_ID") String id, @RequestParam(name="participant_password") String password,
                                    @RequestParam(name="name") String name, @RequestParam(name="position") String position,
                                    HttpServletRequest request, Model model){

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

        // TODO Hvor er denne updated participant?
        Participant participant = (Participant) session.getAttribute("participant_updated");

        if (participant == null) {
            //userEditor.updateParticipant(id, name, position, formerName);
        }
        //session.setAttribute("participant_updated", userEditor.updateParticipant(id, password, name, position, department));
        String pName = participant.getName();

        return "redirect:/update_participant/" + pName;

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