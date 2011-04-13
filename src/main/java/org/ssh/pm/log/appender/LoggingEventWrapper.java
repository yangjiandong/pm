/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * $Id: LoggingEventWrapper.java 1099 2010-05-29 14:33:47Z calvinxiu $
 */
package org.ssh.pm.log.appender;

import java.util.Date;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Log4j LoggingEvent的包装类, 提供默认的toString函数及更直观的属性访问方法.
 *
 * @author calvin
 */
public class LoggingEventWrapper {
    public static final PatternLayout DEFAULT_PATTERN_LAYOUT = new PatternLayout("%d [%t] %-5p %c - %m");

    private final LoggingEvent event;

    public LoggingEventWrapper(LoggingEvent event) {
        this.event = event;
    }

    /**
     * 使用默认的layoutPattern转换事件到日志字符串.
     */
    public String convertToString() {
        return DEFAULT_PATTERN_LAYOUT.format(event);
    }

    /**
     * 根据参数中的layoutPattern转换事件到日志字符串.
     */
    public String convertToString(String layoutPattern) {
        return new PatternLayout(layoutPattern).format(event);
    }

    public long getTimeStamp() {
        return event.getTimeStamp();
    }

    public Date getDate() {
        return new Date(event.getTimeStamp());
    }

    public String getThreadName() {
        return event.getThreadName();
    }

    public String getLoggerName() {
        return event.getLoggerName();
    }

    public String getLevel() {
        return event.getLevel().toString();
    }

    public String getMessage() {
        return (String) event.getMessage();
    }

    /**
     * 影响性能,慎用.
     */
    public String getClassName() {
        return event.getLocationInformation().getClassName();
    }

    /**
     * 影响性能,慎用.
     */
    public String getMethodName() {
        return event.getLocationInformation().getMethodName();
    }
}
