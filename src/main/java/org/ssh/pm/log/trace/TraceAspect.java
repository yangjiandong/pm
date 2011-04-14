package org.ssh.pm.log.trace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 控制Trace的Aspect.
 *
 * @author calvin
 */
@Aspect
public class TraceAspect {

    /**
     * 定义WebService层方法
     */
    @Pointcut("execution(public * org.ssh.pm.ws.server.impl.*Impl.*(..)) ")
    public void webServiceMethod() {
    }

    /**
     * 为WebService入口方法加入TraceID控制.
     */
    @Around("webServiceMethod()")
    public Object traceAround(ProceedingJoinPoint pjp) throws Throwable {
        TraceUtils.beginTrace();
        try {
            return pjp.proceed();
        } finally {
            TraceUtils.endTrace();
        }
    }

    /**
     * 对有@Traced标记的方法,记录其执行参数及返回结果.
     */
    @Around("execution(@Traced * *(..))")
    public Object logAground(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> sourceClass = pjp.getSignature().getDeclaringType();
        Logger logger = LoggerFactory.getLogger(sourceClass);
        Object result = null;

        try {
            logger.debug(pjp.toShortString());
            result = pjp.proceed();
            return result;
        } finally {
            if (result != null && logger.isDebugEnabled()) {
                logger.debug("{} return {}", pjp.getSignature().toShortString(), result.toString());
            }
        }
    }
}
