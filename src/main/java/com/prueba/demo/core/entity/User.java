package com.prueba.demo.core.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USER")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

     @Column(name = "id")
     @Id
     private String id;

     @Column(name = "name")
     private String name;

     @Column(name = "email")
     private String email;

     @Column(name = "active")
     private Integer active;
     
     @Column(name = "password")
     private String password;

     @Column(name = "date_create")
     private LocalDateTime dateCreate;    

     @Column(name = "date_modify")
     private LocalDateTime dateModify;    

     @Column(name = "date_last_login")
     private LocalDateTime dateLastLogin;

     
     
     
}
