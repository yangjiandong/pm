/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * $Id: AppenderUtils.java 1066 2010-04-30 16:25:19Z calvinxiu $
 */
package org.ssh.pm.log;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import com.google.common.collect.Maps;

/**
 * Log4j Appender工具类, 转换Logging Event到字符串或Map.
 *
 * @author calvin
 */
public class AppenderUtils {

    public static final String MESSAGE = "message";
    public static final String LEVEL = "level";
    public static final String LOG_TIME = "log_time";
    public static final String LOGGER_NAME = "loggerName";
    public static final String THREAD_NAME = "threadName";

    public static final PatternLayout DEFAULT_PATTERN_LAYOUT = new PatternLayout("%d [%t] %-5p %c - %m");

    /**
     * 使用默认的layoutPattern转换事件到日志字符串.
     */
    public static String convertEventToString(LoggingEvent event) {
        return DEFAULT_PATTERN_LAYOUT.format(event);
    }

    /**
     * 根据参数中的layoutPattern转换事件到日志字符串.
     */
    public static String convertEventToString(LoggingEvent event, String layoutPattern) {
        return new PatternLayout(layoutPattern).format(event);
    }

    /**
     * 将事件转换到Map, Map中的Key参见本类中定义的常量.
     */
    public static Map<String, Object> convertEventToMap(LoggingEvent event) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(MESSAGE, event.getMessage());
        map.put(LEVEL, event.getLevel().toString());
        map.put(LOGGER_NAME, event.getLoggerName());
        map.put(THREAD_NAME, event.getThreadName());
        map.put(LOG_TIME, new Date(event.getTimeStamp()));
        return map;
    }
}
