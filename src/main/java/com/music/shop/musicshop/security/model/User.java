package com.music.shop.musicshop.security.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.music.shop.musicshop.model.Lable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Document
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class User implements UserDetails {
    @Id
    private String id;
    private String username;
    private String password;
    private Boolean enabled;
    private List<Lable> lableList= new ArrayList<>();

    public void addLableToList(Lable lable) {
    this.lableList.add(lable); }

    private List<Role> roles = new ArrayList<>();
    public void addRoleToList (Role role) {
        this.roles.add(role);
    }

    private List<Message> messages = new ArrayList<>();
    public void addMessageToList (Message message) {
        this.messages.add(message);
    }

    public User(String id, String username, String password, Boolean enabled, List<Role> roles, List<Message> messages) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.messages = messages;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

}
