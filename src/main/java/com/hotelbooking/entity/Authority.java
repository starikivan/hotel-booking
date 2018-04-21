package com.hotelbooking.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authorities", schema = "BOOKING_HOTELS_SCHEMA")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private String authority;
}
