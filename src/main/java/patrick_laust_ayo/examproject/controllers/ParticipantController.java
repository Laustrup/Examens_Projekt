package patrick_laust_ayo.examproject.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.services.UserCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ParticipantController {

    private UserCreator userCreator;


    @PostMapping
    public String createParticipant(@RequestParam() int id, String name, String position, HttpServletRequest request){

        HttpSession session = request.getSession();

        //userCreator.createParticipant(name, String password, String managerName) ;

        return "redirct:/projectPage";
    }

    @PostMapping
    public String updateParticipant(@RequestParam()int id, String name, String position, HttpServletRequest request){

        return "";
    }
}
