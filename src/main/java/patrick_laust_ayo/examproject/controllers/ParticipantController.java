package patrick_laust_ayo.examproject.controllers;

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

    @PostMapping
    public String loginParticipant(@RequestParam(name="id") int id, HttpServletRequest request){

        HttpSession session = request.getSession();

        if (userCreator.doesParticipantExist(id) == false){

        }

    }
    @GetMapping("/projectpage/{pName}")
    public String renderProjectpage ()




    @PostMapping("/updateParticipant")
    public String updateParticipant(@RequestParam(name="participant_ID") int id, @RequestParam(name="participant_password") String password,
                                    @RequestParam(name="name") String name, @RequestParam(name="position") String position,
                                    @RequestParam(name="department") Department department, HttpServletRequest request){

        HttpSession session = request.getSession();

        Participant participant = userEditor.updateParticipant(id, password, name, position, department);

        session.setAttribute("participant_updated", participant);

        String pName = participant.getName();

        return "redirect:/projectpage/" + pName;
    }
}
