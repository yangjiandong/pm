package org.ssh.app.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.util.Assert;

public class SmartConcurrentSessionController implements ConcurrentSessionController, InitializingBean,
        MessageSourceAware {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource
        .getAccessor();
    private SessionRegistry sessionRegistry;
    private boolean exceptionIfMaximumExceeded = false;
    private int maximumSessions = 1;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(sessionRegistry, "SessionRegistry required");
        Assert.isTrue(maximumSessions != 0,
            "MaximumLogins must be either -1 to allow unlimited logins, or a positive integer to specify a maximum");
        Assert.notNull(this.messages, "A message source must be set");
    }

    protected void allowableSessionsExceeded(
        SessionInformation[] sessions, int allowableSessions,
        boolean sameIp) {
        if (!sameIp) {
            if (exceptionIfMaximumExceeded || (sessions == null)) {
                throw new ConcurrentLoginException(messages.getMessage(
                        "ConcurrentSessionControllerImpl.exceededAllowed",
                        new Object[] {new Integer(allowableSessions)},
                        "Maximum sessions of " + allowableSessions
                        + " for this principal exceeded"));
            }
        }

        SessionInformation leastRecentlyUsed = null;

        for (int i = 0; i < sessions.length; i++) {
            if ((leastRecentlyUsed == null)
                    || sessions[i].getLastRequest()
                                      .before(leastRecentlyUsed
                        .getLastRequest())) {
                leastRecentlyUsed = sessions[i];
            }
        }

        leastRecentlyUsed.expireNow();
    }

    public void checkAuthenticationAllowed(Authentication request)
        throws AuthenticationException {
        Assert.notNull(request,
            "Authentication request cannot be null (violation of interface contract)");

        Object principal = SessionRegistryUtils
            .obtainPrincipalFromAuthentication(request);
        String sessionId = SessionRegistryUtils
            .obtainSessionIdFromAuthentication(request);

        //
        // create smart principal
        //
        SmartPrincipal smartPrincipal = new SmartPrincipal(request);

        SessionInformation[] sessions = sessionRegistry.getAllSessions(smartPrincipal,
                false);

        int sessionCount = 0;

        if (sessions != null) {
            sessionCount = sessions.length;
        }

        int allowableSessions = getMaximumSessionsForThisUser(request);
        Assert.isTrue(allowableSessions != 0,
            "getMaximumSessionsForThisUser() must return either -1 to allow "
            + "unlimited logins, or a positive integer to specify a maximum");

        if (sessionCount < allowableSessions) {
            // They haven't got too many login sessions running at present
            return;
        } else if (allowableSessions == -1) {
            // We permit unlimited logins
            return;
        } else if (sessionCount == allowableSessions) {
            // Only permit it though if this request is associated with one of the sessions
            for (int i = 0; i < sessionCount; i++) {
                if (sessions[i].getSessionId().equals(sessionId)) {
                    return;
                }
            }
        }

        //
        // verify the ip value in the smartPrincipal
        //
        boolean sameIp = false;
        Object[] allPrincipals = sessionRegistry.getAllPrincipals();

        for (Object savedPrincipal : allPrincipals) {
            if (smartPrincipal.equals(savedPrincipal)) {
                sameIp = smartPrincipal.equalsIp((SmartPrincipal) savedPrincipal);

                break;
            }
        }

        allowableSessionsExceeded(sessions, allowableSessions, sameIp);
    }

    protected int getMaximumSessionsForThisUser(
        Authentication authentication) {
        return maximumSessions;
    }

    public void registerSuccessfulAuthentication(
        Authentication authentication) {
        Assert.notNull(authentication,
            "Authentication cannot be null (violation of interface contract)");

        Object principal = SessionRegistryUtils
            .obtainPrincipalFromAuthentication(authentication);
        String sessionId = SessionRegistryUtils
            .obtainSessionIdFromAuthentication(authentication);

        //
        // store username and ip
        //
        SmartPrincipal smartPrincipal = new SmartPrincipal(authentication);

        sessionRegistry.registerNewSession(sessionId, smartPrincipal);
    }

    public void setExceptionIfMaximumExceeded(
        boolean exceptionIfMaximumExceeded) {
        this.exceptionIfMaximumExceeded = exceptionIfMaximumExceeded;
    }

    public void setMaximumSessions(int maximumSessions) {
        this.maximumSessions = maximumSessions;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public SessionRegistry getSessionRegistry() {
        return sessionRegistry;
    }
}
