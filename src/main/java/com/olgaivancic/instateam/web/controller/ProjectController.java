package com.olgaivancic.instateam.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProjectController {

    // Home page - index of all of the projects
    @RequestMapping("/")
    public String listProjects(Model model) {
        // TODO: add attributes to the model and fix the Index file accordingly

        return "project/index";
    }

    //
}
