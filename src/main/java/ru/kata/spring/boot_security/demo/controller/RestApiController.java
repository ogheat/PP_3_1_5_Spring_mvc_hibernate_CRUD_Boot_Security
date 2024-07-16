package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController()
@RequestMapping("/api")
public class RestApiController {

    private UserService userService;

    @Autowired
    public RestApiController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/all")
    public List<User> showAllUsers() {
        return userService.getUsers();
    }


    @GetMapping("/user/{id}")
    public User showUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/user/add")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>("User added successfully", HttpStatus.OK);
    }

    @PostMapping("/user/edit")
    public ResponseEntity<String> editUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>("User edited successfully", HttpStatus.OK);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
