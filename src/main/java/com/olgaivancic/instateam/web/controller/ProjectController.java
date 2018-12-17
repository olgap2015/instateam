package com.olgaivancic.instateam.web.controller;

import com.olgaivancic.instateam.model.Collaborator;
import com.olgaivancic.instateam.model.Project;
import com.olgaivancic.instateam.model.Role;
import com.olgaivancic.instateam.service.ProjectService;
import com.olgaivancic.instateam.service.RoleService;
import com.olgaivancic.instateam.web.FlashMessage;
import com.olgaivancic.instateam.web.ProjectStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private RoleService roleService;

    // Home page - index of all of the projects
    @RequestMapping("/")
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("active", ProjectStatus.ACTIVE.getHexCode());
        model.addAttribute("archived", ProjectStatus.ARCHIVED.getHexCode());
        model.addAttribute("notstarted", ProjectStatus.NOTSTARTED.getHexCode());
        return "project/index";
    }

    // Renders project details page
    @RequestMapping("/projects/{projectId}")
    public String projectDetails(@PathVariable Long projectId, Model model) {
        Project project = projectService.findById(projectId);
        model.addAttribute("project", project);
        // Create a map of rolesNeeded to Collaborators of the project
        Map<String,String> mapOfRolesToCollaborators = new TreeMap<>();
        for (Role role : project.getRolesNeeded()) {
            if (project.getCollaborators().size() == 0) {
                mapOfRolesToCollaborators.put(role.getName(), "[Unassigned]");
            } else {
                for (Collaborator collaborator : project.getCollaborators()) {
                    if (role.equals(collaborator.getRole())) {
                        mapOfRolesToCollaborators.put(role.getName(), collaborator.getName());
                        // If there is no collaborator assigned to a particular role yet, map the role to the String "Unassigned"
                    } else mapOfRolesToCollaborators.put(role.getName(), "[Unassigned]");
                }
            }
        }
        model.addAttribute("mapOfRolesToCollaborators", mapOfRolesToCollaborators);
        System.out.println(mapOfRolesToCollaborators);
        return "project/project_detail";
    }

    // Renders a page to add new project
    @RequestMapping(value = "projects/new", method = RequestMethod.GET)
    public String addProject(Model model) {
        if (!model.containsAttribute("project")) {
            model.addAttribute("project", new Project());
        }
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("action", "new");
        model.addAttribute("submit", "Add");
        return "project/edit_project";
    }

    // Post method to save a new project
    @RequestMapping(value = "projects/new", method = RequestMethod.POST)
    public String saveProject(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project", result);
            redirectAttributes.addFlashAttribute("project", project);
            return "redirect:/projects/new";
        }
        projectService.save(project);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Projects is successfully saved!", FlashMessage.Status.SUCCESS));

        return "redirect:/";
    }

    // Renders a page to edit a project
    @RequestMapping(value = "projects/{projectId}/edit", method = RequestMethod.GET)
    public String editProject(@PathVariable Long projectId, Model model) {
        if (!model.containsAttribute("project")) {
            model.addAttribute("project", projectService.findById(projectId));
        }
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("action", "update");
        model.addAttribute("submit", "Update");
        return "project/edit_project";
    }

    // Post method to save a new project
    @RequestMapping(value = "projects/{projectId}/update", method = RequestMethod.POST)
    public String updateProject(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project", result);
            redirectAttributes.addFlashAttribute("project", project);
            return String.format("redirect:/projects/%s/edit", project.getId());
        }
        projectService.save(project);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Projects is successfully updated!", FlashMessage.Status.SUCCESS));

        return "redirect:/";
    }

    @RequestMapping("/projects/{projectId}/edit-collaborators")
    public String editProjectCollaborators(@PathVariable Long projectId, Model model) {
        // TODO: Implement this method

        return "project/project_collaborators";
    }
}
