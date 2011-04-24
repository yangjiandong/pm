package org.ssh.pm.common.web;

import org.ssh.pm.common.entity.User;

import java.io.Serializable;


public class UserSession implements Serializable {
    private User account;

    public UserSession(User account) {
        this.account = account;
    }

    public User getAccount() {
        return account;
    }
}
