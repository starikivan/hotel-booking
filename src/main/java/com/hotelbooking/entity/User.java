package com.hotelbooking.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "users")
@Data
public class User {
    @Id
    private String username;
    private String password;
    private boolean enabled;
    private String firstName;
    private String lastName;
}