package com.todo.todoapp.services;

import com.todo.todoapp.models.User;
import com.todo.todoapp.models.dto.UserCreateDTO;
import com.todo.todoapp.models.dto.UserUpdateDTO;
import com.todo.todoapp.models.enums.ProfileEnum;
import com.todo.todoapp.repositories.UserRepository;
import com.todo.todoapp.security.UserSpringSecurity;
import com.todo.todoapp.services.exceptions.AuthorizationException;
import com.todo.todoapp.services.exceptions.DataBindingViolationException;
import com.todo.todoapp.services.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        UserSpringSecurity userSpringSecurity = authenticated();
        if (!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId())) {
            throw new AuthorizationException("Acesso negado!");
        }

        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("Usuário " + id + " não encontrado!"));
    }

    @Transactional
    public User create(User user) {
        user.setId(null);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        user.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        return this.userRepository.save(user);
    }

    @Transactional
    public User update(User user) {
        User newUser = findById(user.getId());
        newUser.setPassword(user.getPassword());
        newUser.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        return this.userRepository.save(newUser);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível excluir, pois há entidades relacionadas!");
        }
    }

    public static UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    public User fromDTO(@Valid UserCreateDTO obj) {
        User user = new User();
        user.setUsername(obj.getUsername());
        user.setPassword(obj.getPassword());
        return user;
    }

    public User fromDTO(@Valid UserUpdateDTO obj) {
        User user = new User();
        user.setId(obj.getId());
        user.setPassword(obj.getPassword());
        return user;
    }
}
