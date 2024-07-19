package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/user")
public class UserRestController {
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "")
    public ModelAndView showUserDetails(Model model, Principal principal) {
        Authentication authentication = (Authentication) principal;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String email = principal.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("email", email);
        model.addAttribute("roles", authorities);
        return new ModelAndView("user.html");
    }

    @GetMapping("/{id}")
    public User showUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
