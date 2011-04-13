package org.ssh.pm.log.trace;

import java.util.Enumeration;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * 控制是否输出DEBUG信息到Trace Appender的MBean, 可用JMX或JSP调用.
 *
 * @author jeff zhu
 */
@ManagedResource(objectName = TraceMBean.TRACE_MBEAN_NAME, description = "Trace Management Bean")
public class TraceMBean {

    /**
     * TraceMbean的注册名称.
     */
    public static final String TRACE_MBEAN_NAME = "Showcase:name=trace,type=Trace";

    private String traceLoggerName = "org.springside.examples.showcase";

    @ManagedAttribute(description = "Check if the trace is open or not")
    public boolean getTraceStatus() {
        Logger logger = Logger.getLogger(traceLoggerName);
        Level level = logger.getEffectiveLevel();
        return level.getSyslogEquivalent() >= Level.DEBUG.getSyslogEquivalent();
    }

    @ManagedOperation(description = "Start trace")
    public void startTrace() {
        Logger logger = Logger.getLogger(traceLoggerName);
        logger.setLevel(Level.DEBUG);
        setLoggerAppendersLevel(logger, Level.DEBUG);
    }

    @ManagedOperation(description = "Stop trace")
    public void stopTrace() {
        Logger logger = Logger.getLogger(traceLoggerName);
        logger.setLevel(Level.INFO);
        setLoggerAppendersLevel(logger, Level.OFF);
    }

    @SuppressWarnings("unchecked")
    private void setLoggerAppendersLevel(Logger logger, Level level) {
        Enumeration e = logger.getAllAppenders();
        while (e.hasMoreElements()) {
            AppenderSkeleton appender = (AppenderSkeleton) e.nextElement();
            appender.setThreshold(level);
        }
    }

    public void setTraceLoggerName(String traceLoggerName) {
        this.traceLoggerName = traceLoggerName;
    }
}