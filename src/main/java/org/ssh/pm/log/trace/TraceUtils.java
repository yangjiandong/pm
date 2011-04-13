/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * $Id: TraceUtils.java 693 2009-12-12 10:17:34Z calvinxiu $
 */
package org.ssh.pm.log.trace;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.MDC;

/**
 * 系统运行时打印方便调试与追踪信息的工具类.
 *
 * 使用MDC存储traceID, 一次trace中所有日志都自动带有该ID,
 * 可以方便的用grep命令在日志文件中提取该trace的所有日志.
 *
 * 需要在log4j.properties中将ConversionPattern添加%X{traceId},如:
 * log4j.appender.stdout.layout.ConversionPattern=%d [%c] %X{traceId}-%m%n
 *
 * @author calvin
 */
public class TraceUtils {

    public static final String TRACE_ID_KEY = "traceId";
    public static final int TRACE_ID_LENGTH = 8;

    /**
     * 开始Trace, 默认生成本次Trace的ID(8字符长)并放入MDC.
     */
    public static void beginTrace() {
        String traceId = RandomStringUtils.randomAlphanumeric(TRACE_ID_LENGTH);
        MDC.put(TRACE_ID_KEY, traceId);
    }

    /**
     * 开始Trace, 将traceId放入MDC.
     */
    public static void beginTrace(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    /**
     * 结束一次Trace.
     * 清除traceId.
     */
    public static void endTrace() {
        MDC.remove(TRACE_ID_KEY);
    }
}
