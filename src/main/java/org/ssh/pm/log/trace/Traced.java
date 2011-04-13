/*
 * $HeadURL: https://springside.googlecode.com/svn/springside3/trunk/examples/showcase/src/main/java/org/springside/examples/showcase/log/trace/Traced.java $
 * $Id: Traced.java 1105 2010-06-25 08:37:40Z calvinxiu $
 * Copyright (c) 2009 by Drutt, all rights reserved.
 */

package org.ssh.pm.log.trace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识一个方法将通过AOP进行Traced.
 *
 * @see TraceAspect
 *
 * @author George
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Traced {
}
