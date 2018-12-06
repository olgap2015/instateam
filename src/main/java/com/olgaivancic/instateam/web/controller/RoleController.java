package com.olgaivancic.instateam.web.controller;

import com.olgaivancic.instateam.model.Role;
import com.olgaivancic.instateam.service.RoleService;
import com.olgaivancic.instateam.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/roles")
    public String listRoles(Model model) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("newRole", new Role());
        return "role/roles";
    }

    @RequestMapping("/roles/{role.id}/edit")
    public String editRole(@PathVariable Long roleId, Model model) {
        // TODO: find the role to edit
        Role role  = roleService.findById(roleId);
        model.addAttribute("role", role);

        // TODO: redesign roles_edit file
        return "role/roles_edit";
    }

    @RequestMapping(value = "/roles/new", method = RequestMethod.POST)
    public String addRole(Role role, RedirectAttributes redirectAttributes) {
        // TODO: validate the data

        // Save the new role
        roleService.save(role);

        // Add a flash message to the redirect
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Role successfully added!", FlashMessage.Status.SUCCESS));

        // Redirect to the roles view page
        return "redirect:/roles";
    }


}
