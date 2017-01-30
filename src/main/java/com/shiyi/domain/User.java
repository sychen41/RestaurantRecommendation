package com.shiyi.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Shiyi on 1/26/2017.
 */
@Entity
public class User {
     @Id
     private String username;
     private String password;
     private String firstName;
     private String lastName;

     public User() {}

     public User(String username, String password, String firstName, String lastName) {
          this.username = username;
          this.password = password;
          this.firstName = firstName;
          this.lastName = lastName;
     }

     public String getUsername() {
          return username;
     }

     public void setUsername(String username) {
          this.username = username;
     }

     public String getPassword() {
          return password;
     }

     public void setPassword(String password) {
          this.password = password;
     }

     public String getFirstName() {
          return firstName;
     }

     public void setFirstName(String firstName) {
          this.firstName = firstName;
     }

     public String getLastName() {
          return lastName;
     }

     public void setLastName(String lastName) {
          this.lastName = lastName;
     }
}
