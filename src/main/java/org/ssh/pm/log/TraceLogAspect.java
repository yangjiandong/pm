package org.ssh.pm.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springside.modules.log.TraceUtils;

/**
 * 为WebService方法加入TraceUtils控制的Aspect.
 *
 * @author calvin
 */
@Aspect
public class TraceLogAspect {

    @Pointcut("execution(public * org.springside.examples.showcase.ws.server.impl.*Impl.*(..)) ")
    public void webServiceMethod() {
    }

    @Around("webServiceMethod()")
    public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
        TraceUtils.beginTrace();
        try {
            return pjp.proceed();
        } finally {
            TraceUtils.endTrace();
        }
    }
}
