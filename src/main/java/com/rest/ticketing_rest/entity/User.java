package com.rest.ticketing_rest.entity;

import com.rest.ticketing_rest.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity{
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String userName;

    private String phone;
    private String passWord;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    private Role role;
}
