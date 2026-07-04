package com.example.myapplication.model;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String birthday;
    private String role;
    private List<SavedBuild> savedBuilds;

    public User() {}

    public User(String id, String name, String email, String phone, String birthday) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.role = "member";

    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getBirthday() { return birthday; }
    public String getRole() { return role; }
    public List<SavedBuild> getSavedBuilds() { return savedBuilds; }


    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setRole(String role) { this.role = role; }
    public void setSavedBuilds(List<SavedBuild> savedBuilds) { this.savedBuilds = savedBuilds; }
}
