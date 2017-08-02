/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sukeban.user.management.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserStatus {

    @JsonProperty
    private User user;
    @JsonProperty
    private String status;

    public UserStatus() {
    }
    
    UserStatus(User user, String status) {
        this.user = user;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    

}
