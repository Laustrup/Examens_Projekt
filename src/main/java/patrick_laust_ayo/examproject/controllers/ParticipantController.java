package patrick_laust_ayo.examproject.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.services.UserCreator;
import patrick_laust_ayo.examproject.services.UserEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ParticipantController {

    private UserCreator userCreator;
    private UserEditor userEditor;

    @GetMapping("/login")
    public String renderLoginParticipant(){
        return "participant_login";

    }

    @PostMapping("/update_participant")
    public String updateParticipant(@RequestParam(name="participant_ID") int id, @RequestParam(name="participant_password") String password,
                                    @RequestParam(name="name") String name, @RequestParam(name="position") String position,
                                    @RequestParam(name="department") Department department, HttpServletRequest request){

        HttpSession session = request.getSession();

        Participant participant = (Participant)session.getAttribute("participant_updated");

        if (participant == null) {
        //   userEditor.updateParticipant(id, name, position, formerName);
        }
      // session.setAttribute("participant_updated", userEditor.updateParticipant(id, password, name, position, department));
        String pName = participant.getName();

        return "update_participant" + pName;
    }

    //Login existing participant
    @PostMapping ("/participant_login")
    public String loginParticipant(@RequestParam(name="participant_ID") int id, @RequestParam(name="particiant_password")
            String password, HttpServletRequest request) {

        HttpSession session = request.getSession();




return "";
    }

    @GetMapping("/projectpage/{pName}")
    public String renderProjectpage (HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();

return "";
}
}