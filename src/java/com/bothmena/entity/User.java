package com.bothmena.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;

/**
 * Created by Aymen Ben Othmen on 27/07/16.
 */

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", nullable = false, length = 15, unique = true)
    @Length(min = 3, max = 15, message = "username: length must be between 2 and 15")
    private String username;

    @Column(name = "firstname", nullable = false, length = 31)
    @Length(min = 2, max = 15, message = "firstname: length must be between 2 and 15")
    public String firstname;

    @Column(name = "lastname", nullable = false, length = 31)
    @Length(min = 2, max = 15, message = "lastname: length must be between 2 and 15")
    private String lastname;

    @Column(name = "role", nullable = false, length = 15, unique = true)//ROLE_ADMIN, ROLE_VIEWER, ROLE_USER, ...
    @Length(min = 3, max = 15, message = "Role: length must be between 2 and 15")
    private String role;

    @Column(name = "password", nullable = false, length = 18 )
    @NotBlank(message = "password: this property must not be blank")
    private String password;

    @Column(name = "salt", nullable = false)
    private String salt;

    public User() {
        this("", "", "");
    }

    public User(String username) {
        this(username, "", "");
    }

    public User(String username, String firstname, String lastname) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    public void setPassword(String password) {
        setSalt();
        this.password = BCrypt.hashpw(password, salt);
    }

    public String getSalt() {
        return salt;
    }

    private void setSalt() {
        this.salt = BCrypt.gensalt(12);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
