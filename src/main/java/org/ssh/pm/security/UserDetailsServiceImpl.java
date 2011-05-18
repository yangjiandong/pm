package org.ssh.pm.security;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.ssh.pm.common.entity.Role;
import org.ssh.pm.common.entity.User;
import org.ssh.pm.common.service.AccountManager;

import com.google.common.collect.Sets;

/**
 * 实现SpringSecurity的UserDetailsService接口,实现获取用户Detail信息的回调函数.
 *
 * 演示扩展SpringSecurity的User类加入loginTime信息.
 *
 * @author calvin
 */
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private AccountManager accountManager;

    /**
     * 获取用户Detail信息的回调函数.
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException,
            DataAccessException {

        User user = accountManager.findUserByLoginName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户" + username + " 不存在");
        }

        Set<GrantedAuthority> grantedAuths = obtainGrantedAuthorities(user);

        // showcase的User类中无以下属性,暂时全部设为true.
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        OperatorDetails userDetails = new OperatorDetails(user.getLoginName(),
                user.getPassword(), enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, grantedAuths);
        // 加入登录时间信息和用户角色
        userDetails.setLoginTime(new Date());
        userDetails.setRoleList(user.getRoleList());
        userDetails.setUserId(user.getId());
        userDetails.setLoginName(user.getLoginName());
        return userDetails;
    }

    /**
     * 获得用户所有角色的权限.
     */
    private Set<GrantedAuthority> obtainGrantedAuthorities(User user) {
        Set<GrantedAuthority> authSet = Sets.newHashSet();
        for (Role role : user.getRoleList()) {
            authSet.add(new GrantedAuthorityImpl("ROLE_" + role.getName()));
        }
        return authSet;
    }

    @Autowired
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }
}
