package patrick_laust_ayo.examproject.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
    @RequestMapping("/logout")
    public class LogoutController {

        @RequestMapping(method= RequestMethod.POST)
        public String logout(HttpSession session) {
            session.invalidate();
            return "redirect:/login";
        }
    }


