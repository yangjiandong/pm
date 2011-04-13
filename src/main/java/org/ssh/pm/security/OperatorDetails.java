package org.ssh.pm.security;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.ssh.pm.common.entity.Role;

/**
 * 扩展SpringSecurity的WebAuthenticationDetails类, 增加登录时间属性和角色属性.
 *
 * @author calvin
 */
public class OperatorDetails extends User {
    private static final long serialVersionUID = 1919464185097508773L;

    private Date loginTime;

    private List<Role> roleList;

    private Long userId;

    private String loginName;

    public OperatorDetails(String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities)
            throws IllegalArgumentException {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
