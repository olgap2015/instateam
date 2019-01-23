package com.olgaivancic.instateam.web.controller;

import com.olgaivancic.instateam.model.Collaborator;
import com.olgaivancic.instateam.model.Project;
import com.olgaivancic.instateam.model.Role;
import com.olgaivancic.instateam.service.CollaboratorService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CollaboratorService collaboratorService;

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
        for (Collaborator collaborator : project.getCollaborators()) {
            mapOfRolesToCollaborators.put(collaborator.getRole().getName(), collaborator.getName());
        }
        // Each role that hasn't been assigned yet, add to the map with the value of "Unassigned"
        for (Role role : project.getRolesNeeded()) {
            if (!mapOfRolesToCollaborators.containsKey(role.toString())) {
                mapOfRolesToCollaborators.put(role.getName(), "[Unassigned]");
            }
        }
        model.addAttribute("mapOfRolesToCollaborators", mapOfRolesToCollaborators);
        model.addAttribute("active", ProjectStatus.ACTIVE.getHexCode());
        model.addAttribute("archived", ProjectStatus.ARCHIVED.getHexCode());
        model.addAttribute("notstarted", ProjectStatus.NOTSTARTED.getHexCode());
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
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Project is successfully saved!", FlashMessage.Status.SUCCESS));

        return "redirect:/";
    }

    // Renders a page to edit a project
    @RequestMapping(value = "projects/{projectId}/edit", method = RequestMethod.GET)
    public String editProject(@PathVariable Long projectId, Model model) {
        Project project = projectService.findById(projectId);
        if (!model.containsAttribute("project")) {
            model.addAttribute("project", project);
        }
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("projectRoles", project.getRolesNeeded());
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("action", String.format("%s/update", project.getId()));
        model.addAttribute("submit", "Update");
        return "project/edit_project";
    }

    // Post method to update a project
    @RequestMapping(value = "projects/{projectId}/update", method = RequestMethod.POST)
    public String updateProject(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project", result);
            redirectAttributes.addFlashAttribute("project", project);
            return String.format("redirect:/projects/%s/edit", project.getId());
        }
        projectService.save(project);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Projects is successfully updated!", FlashMessage.Status.SUCCESS));

        return String.format("redirect:/projects/%s", project.getId());
    }

    // Renders a page to edit project collaborators
    @RequestMapping("/projects/{projectId}/edit-collaborators")
    public String editProjectCollaborators(@PathVariable Long projectId, Model model) {
        // TODO: FIX - current collaborator is not a selected option when the page is rendered
        Project project = projectService.findById(projectId);
        if (!model.containsAttribute("project")) {
            model.addAttribute("project", project);
        }
        if (!model.containsAttribute("projectRoles")) {
            model.addAttribute("projectRoles", project.getRolesNeeded());
        }
        List<Collaborator> allCollaborators = collaboratorService.findAll();
        Map<String, List<Collaborator>> mapOfRolesNeededToCollaboratorsWithThatRole = new TreeMap<>();
        // Map of roles needed for the project to List of all the collaborators who have this role
        for (Role role : project.getRolesNeeded()) {
            List<Collaborator> collaboratorsWithTheSameRole = new ArrayList<>();
            for (Collaborator collaborator : allCollaborators) {
                if (role.equals(collaborator.getRole())) {
                    collaboratorsWithTheSameRole.add(collaborator);
                }
            }
            mapOfRolesNeededToCollaboratorsWithThatRole.put(role.getName(), collaboratorsWithTheSameRole);
        }
        model.addAttribute("mapOfRolesNeededToCollaboratorsWithThatRole", mapOfRolesNeededToCollaboratorsWithThatRole);
        return "project/project_collaborators";
    }

    // Post method to save changes to project collaborators
    @RequestMapping(value = "/projects/{projectId}/update-collaborators", method = RequestMethod.POST)
    public String updateProjectCollaborators (@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project", result);
            redirectAttributes.addFlashAttribute("project", project);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Form contains invalid data and couldn't be processed!", FlashMessage.Status.FAILURE));
            return String.format("redirect:/projects/%s/edit-collaborators", project.getId());
        }
        projectService.save(project);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Project collaborators are successfully updated!", FlashMessage.Status.SUCCESS));

        return String.format("redirect:/projects/%s", project.getId());
    }

    // Post method to delete a project
    @RequestMapping(value = "/projects/{projectId}/delete", method = RequestMethod.POST)
    public String deleteProject(@PathVariable Long projectId, RedirectAttributes redirectAttributes) {
        Project project = projectService.findById(projectId);
        // Check to see whether project has collaborators assigned if yes, delete all the collaborators
        List<Collaborator> collaborators = project.getCollaborators();
        if (project.getCollaborators().size() > 0) {
            for (int i = collaborators.size() - 1; i >= 0; i--   ) {
                collaborators.remove(i);
            }
        }
        // Delete all the rolesNeeded
        List<Role> rolesNeeded = project.getRolesNeeded();
        for (int i = rolesNeeded.size() - 1; i >= 0; i--   ) {
            rolesNeeded.remove(i);
        }
        projectService.save(project);
        projectService.delete(project);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Project deleted!", FlashMessage.Status.SUCCESS));
        return "redirect:/";
    }






}
