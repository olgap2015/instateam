package com.olgaivancic.instateam.web.controller;

import com.olgaivancic.instateam.model.Collaborator;
import com.olgaivancic.instateam.model.Project;
import com.olgaivancic.instateam.model.Role;
import com.olgaivancic.instateam.service.CollaboratorService;
import com.olgaivancic.instateam.service.ProjectService;
import com.olgaivancic.instateam.service.RoleService;
import com.olgaivancic.instateam.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private CollaboratorService collaboratorService;

    @Autowired
    private ProjectService projectService;

    // Roles view page
    @RequestMapping("/roles")
    public String listRoles(Model model) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        if (!model.containsAttribute("role")) {
            model.addAttribute("role", new Role());
        }
        return "role/roles";
    }

    // Post method to save new role
    @RequestMapping(value = "/roles/new", method = RequestMethod.POST)
    public String addRole(@Valid Role role, BindingResult result, RedirectAttributes redirectAttributes) {
        // Validate the data
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.role", result);
            redirectAttributes.addFlashAttribute("role", role);
            return "redirect:/roles";
        }

        // Save the new role
        roleService.save(role);

        // Add a flash message to the redirect
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Role successfully added!", FlashMessage.Status.SUCCESS));

        // Redirect to the roles view page
        return "redirect:/roles";
    }

    // Renders edit role page
    @RequestMapping("/roles/{roleId}/edit")
    public String editRole(@PathVariable Long roleId, Model model) {
        // Find the role to edit
        if (!model.containsAttribute("role")) {
            Role role = roleService.findById(roleId);
            model.addAttribute("role", role);
        }

        return "role/role_edit";
    }

    // Post method to update a role
    @RequestMapping(value = "/roles/{roleId}/update", method = RequestMethod.POST)
    public String updateRole(@Valid Role role, BindingResult result, RedirectAttributes redirectAttributes) {
        // Validate the incoming data
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.role", result);
            redirectAttributes.addFlashAttribute("role", role);
            return String.format("redirect:/roles/%s/edit", role.getId());
        }

        // Update role
        roleService.save(role);

        // Add a flash message on redirect
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Role successfully updated!", FlashMessage.Status.SUCCESS));

        return "redirect:/roles";
    }

    // Post method to delete a role
    @RequestMapping(value = "/roles/{roleId}/delete", method = RequestMethod.POST)
    public String deleteRole(@PathVariable Long roleId, RedirectAttributes redirectAttributes) {
        Role role = roleService.findById(roleId);
        // Delete all the collaborators with that role
        List<Collaborator> collaborators = collaboratorService.findAll();
        if (collaborators.size() > 0) {
            for (int i = collaborators.size() - 1; i >= 0; i--) {
                Collaborator col = collaborators.get(i);
                if (col.getRole().equals(role)) {
                    collaboratorService.delete(col);
                }
            }
        }
        // Delete that role from the rolesNeeded list from each project where the role occurs
        List<Project> projects = projectService.findAll();
        if (projects.size() > 0) {
            for (Project project : projects) {
                List<Role> rolesNeeded = project.getRolesNeeded();
                if (rolesNeeded.size() > 0) {
                    for (int i = rolesNeeded.size() - 1; i >= 0; i--) {
                        if (rolesNeeded.get(i).equals(role)) {
                            rolesNeeded.remove(i);
                            projectService.save(project);
                        }
                    }
                }
            }
        }
        roleService.delete(role);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Role deleted!", FlashMessage.Status.SUCCESS));
        return "redirect:/roles";
    }



}
