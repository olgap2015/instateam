package com.olgaivancic.instateam.web.controller;

import com.olgaivancic.instateam.model.Collaborator;
import com.olgaivancic.instateam.model.Role;
import com.olgaivancic.instateam.service.CollaboratorService;
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
public class CollaboratorController {
    @Autowired
    private CollaboratorService collaboratorService;

    @Autowired
    private RoleService roleService;

    // Renders the list of collaborators and a form to add a new one as well as a button for editing
    @RequestMapping("/collaborators")
    public String listCollaborators(Model model) {
        // TODO: Fetch and add the list of collaborators to the model
        List<Collaborator> collaborators = collaboratorService.findAll();
        model.addAttribute("collaborators", collaborators);
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        if (!model.containsAttribute("collaborator")) {
            model.addAttribute("collaborator", new Collaborator());
        }
        return "collaborator/collaborators";
    }

    // Post method to save a new collaborator
    @RequestMapping(value = "/collaborators/new", method = RequestMethod.POST)
    public String addCollaborator(@Valid Collaborator collaborator, BindingResult result, RedirectAttributes redirectAttributes) {
        // TODO: Add data validation
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.collaborator", result);
            redirectAttributes.addFlashAttribute("collaborator", collaborator);
            return "redirect:/collaborators";
        }

        System.out.println(collaborator);

        collaboratorService.save(collaborator);

        // Flash message that the save was a success
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Collaborator is successfully saved!", FlashMessage.Status.SUCCESS));

        return "redirect:/collaborators";
    }

    // Renders edit collaborator page
    @RequestMapping("/collaborators/{collaboratorId}/edit")
    public String editCollaborator(@PathVariable Long collaboratorId, Model model) {
        // Find the role to edit
        if (!model.containsAttribute("collaborator")) {
            Collaborator collaborator = collaboratorService.findById(collaboratorId);
            model.addAttribute("collaborator", collaborator);
        }
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);

        return "collaborator/collaborator_edit";
    }

    // Post method to update a collaborator
    @RequestMapping(value = "/collaborators/{collaboratorId}/update", method = RequestMethod.POST)
    public String updateCollaborator(@Valid Collaborator collaborator, BindingResult result, RedirectAttributes redirectAttributes) {
        // Validate the incoming data
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.collaborator", result);
            redirectAttributes.addFlashAttribute("collaborator", collaborator);
            return String.format("redirect:/collaborators/%s/edit", collaborator.getId());
        }

        // Update role
        collaboratorService.save(collaborator);

        // Add a flash message on redirect
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Collaborator successfully updated!", FlashMessage.Status.SUCCESS));

        return "redirect:/collaborators";
    }

    // Post method to delete a collaborator
    @RequestMapping(value = "/collaborators/{collaboratorId}/delete", method = RequestMethod.POST)
    public String deleteCollaborator(@PathVariable Long collaboratorId, RedirectAttributes redirectAttributes) {
        Collaborator collaborator = collaboratorService.findById(collaboratorId);
        collaboratorService.delete(collaborator);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Collaborator is successfully deleted!", FlashMessage.Status.SUCCESS));
        return "redirect:/collaborators";
    }

}
