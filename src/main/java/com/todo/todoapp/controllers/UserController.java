package com.todo.todoapp.controllers;

import com.todo.todoapp.models.User;
import com.todo.todoapp.models.dto.UserCreateDTO;
import com.todo.todoapp.models.dto.UserUpdateDTO;
import com.todo.todoapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = this.userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO obj) {
        User user = this.userService.fromDTO(obj);
        User newUser = this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO obj, @PathVariable Long id) {
        // Seta o ID do usuário com o parâmetro para garantir que o id do usuário atualizado seja o mesmo que veio da URL
        obj.setId(id);
        User user = this.userService.fromDTO(obj);
        this.userService.update(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
