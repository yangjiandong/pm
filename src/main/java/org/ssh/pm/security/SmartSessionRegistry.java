package org.ssh.pm.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistryImpl;

public class SmartSessionRegistry extends SessionRegistryImpl {
    public synchronized void registerNewSession(String sessionId, Object principal) {
        //
        // convert for SmartPrincipal
        //
        if (!(principal instanceof SmartPrincipal)) {
            principal = new SmartPrincipal(SecurityContextHolder.getContext().getAuthentication());
        }

        super.registerNewSession(sessionId, principal);
    }
}