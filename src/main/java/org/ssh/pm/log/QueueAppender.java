/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * $Id: QueueAppender.java 1050 2010-04-17 15:10:56Z calvinxiu $
 */
package org.ssh.pm.log;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.queue.QueuesHolder;

/**
 * 轻量级的Log4j异步Appender.
 *
 * 将所有消息放入QueueManager所管理的Blocking Queue中.
 *
 * @see QueuesHolder
 *
 * @author calvin
 */
public class QueueAppender extends org.apache.log4j.AppenderSkeleton {

    private static Logger logger = LoggerFactory.getLogger(QueueAppender.class);

    protected String queueName;

    protected BlockingQueue<LoggingEvent> queue;

    /**
     * AppenderSkeleton回调函数, 事件到达时将时间放入Queue.
     */
    @Override
    public void append(LoggingEvent event) {
        if (queue == null) {
            queue = QueuesHolder.getQueue(queueName);
        }

        boolean sucess = queue.offer(event);

        if (sucess) {
            if (logger.isDebugEnabled()) {
                logger.debug("put event ,{}", AppenderUtils.convertEventToString(event));
            }
        } else {
            logger.error("Put event to queue fail ,{}", AppenderUtils.convertEventToString(event));
        }
    }

    /**
     * AppenderSkeleton回调函数,关闭Logger时的清理动作.
     */
    public void close() {
    }

    /**
     * AppenderSkeleton回调函数, 设置是否需要定义Layout.
     */
    public boolean requiresLayout() {
        return false;
    }

    /**
     * Log4j根据getter/setter从log4j.properties中注入同名参数.
     */
    public String getQueueName() {
        return queueName;
    }

    /**
     * @see #getQueueName()
     */
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
