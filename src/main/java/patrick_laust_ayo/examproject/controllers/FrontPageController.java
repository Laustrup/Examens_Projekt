package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class FrontPageController {

    @GetMapping("/")
    public String indexPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        model.addAttribute("fullyBooked", session.getAttribute("fullyBooked"));
        return "index";
    }

    @PostMapping("/log_out")
    public String logout(HttpServletRequest request){
        HttpSession httpSession = request.getSession();
        httpSession.invalidate();
        return "redirect:/";
    }
}
