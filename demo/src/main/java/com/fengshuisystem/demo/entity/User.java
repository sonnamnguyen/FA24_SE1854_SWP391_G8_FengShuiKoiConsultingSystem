package com.fengshuisystem.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        Integer id;

        @Column(name = "username", unique = true, length = 50)
        String username;

        @Column(name = "password")
        String password;

        @Column(name = "email",  unique = true, length = 100)
        String email;

        @Column(name = "dob")
        LocalDate dob;

        @Column(name = "phone_number",  unique = true, length = 100)
        String phoneNumber;
        @Column(name = "avatar", length = 1000)
        String avatar;
        @Column(name = "gender", length = 10)
        String gender;

        @Column(name = "code", length = 100)
        String code;

        @Column(name = "status", nullable = false)
        boolean status;
        @Column(name = "create_date")
        LocalDate createDate;

        @ManyToMany(fetch = FetchType.EAGER, cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE,
                CascadeType.REFRESH,
                CascadeType.DETACH
        })
        @JoinTable(
                name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        Set<Role> roles;
}
