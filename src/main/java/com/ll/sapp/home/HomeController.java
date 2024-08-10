package com.ll.sapp.home;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/debugSession")
    @ResponseBody
    public String showSession(HttpSession session) {
        String debug = "";

        while (session.getAttributeNames().hasMoreElements()) {
            String key = session.getAttributeNames().nextElement();
            debug = key + " : " + session.getAttribute(key);
        }

        return debug;
    }
}
