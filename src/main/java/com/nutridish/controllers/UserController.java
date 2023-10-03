package com.nutridish.controllers;

import com.nutridish.dto.UserDTO;
import com.nutridish.dto.UserLoginDTO;
import com.nutridish.entities.FavoriteEntity;
import com.nutridish.entities.RecipeEntity;
import com.nutridish.entities.UserEntity;
import com.nutridish.pojos.JsonRes;
import com.nutridish.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public JsonRes<UserEntity> register(@Valid @RequestBody UserEntity user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public JsonRes<UserEntity> login(@Valid @RequestBody UserLoginDTO user) {
        String username = user.getUsername();
        String password = user.getPassword();

        return userService.login(username, password);
    }

    @GetMapping("/user/{id}")
    public JsonRes<UserEntity> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/user/{id}/update")
    public JsonRes<UserEntity> updateUser(@PathVariable Long id, @Valid @RequestBody UserEntity user) {
        return userService.updateUser(id, user);
    }

}
