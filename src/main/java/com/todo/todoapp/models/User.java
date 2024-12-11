package com.todo.todoapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.todo.todoapp.models.enums.ProfileEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = User.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    public static final String TABLE_NAME = "user";

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "username", length = 100, nullable = false, unique = true)
    @Size(min = 2, max = 100)
    @NotBlank
    private String username;

    @Column(name = "password", length = 60, nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY)
    @Size(min = 8, max = 60)
    @NotBlank
    private String password;

    // Um user pode ter várias tasks, One -> user / Many -> tasks
    @OneToMany(mappedBy = "user")
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<Task> tasks = new ArrayList<Task>();

    // Garante que sempre vá buscar os perfis quando for buscar os usuários
    @Column(name = "profile", nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonProperty(access = Access.WRITE_ONLY)
    @CollectionTable(name = "user_profile")
    private Set<Integer> profiles = new HashSet<>();

    public Set<ProfileEnum> getProfiles() {
        return this.profiles.stream().map(profile -> ProfileEnum.toEnum(profile)).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum profileEnum) {
        this.profiles.add(profileEnum.getCode());
    }
}
