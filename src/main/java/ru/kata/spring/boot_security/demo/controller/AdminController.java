package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {


    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<String> deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("Success");
    }


    @PostMapping("/addUser")
    public ResponseEntity<String> saveUser(@ModelAttribute("user") User user, @RequestParam("roles") Collection<Long> roleIds) {
        userService.saveUserWithRoles(user, roleIds);
        return ResponseEntity.ok("Success");
    }


    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(@ModelAttribute User user, @RequestParam("roles") Collection<Long> roleIds) {
        userService.saveUserWithRoles(user, roleIds);
        return ResponseEntity.ok("Success");
    }
}
