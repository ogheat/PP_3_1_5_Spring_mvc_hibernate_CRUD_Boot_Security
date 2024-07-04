package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {


    private UserServiceImpl userService;

    @Autowired
    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/")
    public String showUsers(ModelMap model, Principal principal) {
        Authentication authentication = (Authentication) principal;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String email = principal.getName();
        List<User> users;
        users = userService.getUsers();
        model.addAttribute("users", users);
        model.addAttribute("email", email);
        model.addAttribute("roles", authorities);
        return "users";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin/";
    }


    @PostMapping("/addUser")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam("roles") Collection<Long> roleIds) {
        userService.saveUserWithRoles(user, roleIds);
        return "redirect:/admin/";
    }


    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute User user, @RequestParam("roles") Collection<Long> roleIds) {
        userService.saveUserWithRoles(user, roleIds);
        return "redirect:/admin/";
    }
}
