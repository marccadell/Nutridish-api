package com.nutridish.services;

import com.nutridish.dto.UserDTO;
import com.nutridish.entities.FavoriteEntity;
import com.nutridish.entities.RecipeEntity;
import com.nutridish.entities.UserEntity;
import com.nutridish.pojos.JsonRes;
import com.nutridish.repositories.FavoriteRepository;
import com.nutridish.repositories.RecipeRepository;
import com.nutridish.repositories.UserRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JsonRes<UserEntity> register(UserEntity user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            return new JsonRes<>(false, 400, "Username is already taken", null);
        }

        UserEntity registeredUser = userRepository.save(user);

        return new JsonRes<>(true, 200, "User registered successfully", registeredUser);
    }

    public JsonRes<UserEntity> login(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return new JsonRes<>(true, 200, "Login successful", user);
        } else {
            return new JsonRes<>(false, 401, "Login failed", null);
        }
    }

    public JsonRes<UserEntity> getUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);

        if (user != null) {
            return new JsonRes<>(true, 200, "User found", user);
        } else {
            return new JsonRes<>(false, 404, "User not found", null);
        }
    }

    public JsonRes<UserEntity> updateUser(Long id, UserEntity user) {
        UserEntity existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return new JsonRes<>(false, 400, "User not found", null);
        } else if (!existingUser.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
            return new JsonRes<>(false, 400, "Username is already taken", null);
        } else {
            existingUser.setName(user.getName());
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            userRepository.save(existingUser);
            return new JsonRes<>(true, 200, "User updated successfully", existingUser);
        }
    }



}
