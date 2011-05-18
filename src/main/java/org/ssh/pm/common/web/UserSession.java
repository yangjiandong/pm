package org.ssh.pm.common.web;

import java.io.Serializable;

import org.ssh.pm.common.entity.PartDB;
import org.ssh.pm.common.entity.User;

public class UserSession implements Serializable {
    private static final long serialVersionUID = -5891346223278285869L;

    private User account;
    String clientIp;
    PartDB partDb;//所选分院
    private Long moduleId;//所选分系统

    public UserSession(User account) {
        this.account = account;
    }

    public User getAccount() {
        return account;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public PartDB getPartDb() {
        return partDb;
    }

    public void setPartDb(PartDB partDb) {
        this.partDb = partDb;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public void setAccount(User account) {
        this.account = account;
    }
}
