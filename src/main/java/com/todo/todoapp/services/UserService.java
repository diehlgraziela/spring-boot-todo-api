package com.todo.todoapp.services;

import com.todo.todoapp.models.User;
import com.todo.todoapp.repositories.TaskRepository;
import com.todo.todoapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);

        return user.orElseThrow(() -> new RuntimeException("Usuário " + id + " não encontrado!"));
    }

    @Transactional
    public User create(User user) {
        user.setId(null);
        if(userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("Usuário já existe!");
        }
        return this.userRepository.save(user);
    }

    @Transactional
    public User update(User user) {
        User newUser = findById(user.getId());
        newUser.setPassword(user.getPassword());
        return this.userRepository.save(newUser);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir, pois há entidades relacionadas!");
        }
    }
}
