package com.lexx.demos.webapps;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String printHello(ModelMap model) {
        model.addAttribute("message_hello", "Hello Spring MVC Framework!");
        return "hello";
    }

    @RequestMapping(value = "/noroc", method = RequestMethod.GET)
    public ModelAndView printNoroc() {
        return new ModelAndView("noroc", "message_noroc", "Noroc Spring MVC Framework!");
    }

    @RequestMapping(value="/js", method = RequestMethod.GET, produces = "application/javascript")
    public String runJs(ModelMap model) {
        return "js";
    }



}