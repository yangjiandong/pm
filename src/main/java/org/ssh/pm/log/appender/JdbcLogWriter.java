/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * $Id: JdbcAppenderTask.java 353 2009-08-22 09:33:28Z calvinxiu
 */
package org.ssh.pm.log.appender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.spi.LoggingEvent;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springside.modules.queue.BlockingConsumer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 将Queue中的log4j event写入数据库的消费者任务.
 *
 * 即时阻塞的读取Queue中的事件,达到缓存上限后使用Jdbc批量写入模式.
 * 如需换为定时读取模式,继承于PeriodConsumer稍加改造即可.
 *
 * @see BlockingConsumer
 *
 * @author calvin
 */
public class JdbcLogWriter extends BlockingConsumer {

    protected String sql;
    protected int batchSize = 10;

    protected List<LoggingEvent> eventsBuffer = Lists.newArrayList();
    protected SimpleJdbcTemplate jdbcTemplate;
    protected TransactionTemplate transactionTemplate;

    /**
     * 带Named Parameter的insert sql.
     *
     * Named Parameter的名称见AppenderUtils中的常量定义.
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * 批量读取事件数量, 默认为10.
     */
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * 根据注入的DataSource创建jdbcTemplate.
     */
    @Resource
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new SimpleJdbcTemplate(dataSource);
    }

    /**
     * 根据注入的PlatformTransactionManager创建transactionTemplate.
     */
    @Resource
    public void setDefaultTransactionManager(PlatformTransactionManager defaultTransactionManager) {
        transactionTemplate = new TransactionTemplate(defaultTransactionManager);
    }

    /**
     * 消息处理函数,将消息放入buffer,当buffer达到batchSize时执行批量更新函数.
     */
    @Override
    protected void processMessage(Object message) {
        LoggingEvent event = (LoggingEvent) message;
        eventsBuffer.add(event);

        if (logger.isDebugEnabled()) {
            logger.debug("get event: {}", new LoggingEventWrapper(event).convertToString());
        }

        //已到达BufferSize则执行批量插入操作
        if (eventsBuffer.size() >= batchSize) {
            updateBatch();
        }
    }

    /**
     * 将Buffer中的事件列表批量插入数据库.
     */
    @SuppressWarnings("unchecked")
    public void updateBatch() {
        try {
            //分析事件列表, 转换为jdbc批处理参数.
            int i = 0;
            Map[] paramMapArray = new HashMap[eventsBuffer.size()];
            for (LoggingEvent event : eventsBuffer) {
                paramMapArray[i++] = parseEvent(event);
            }
            final SqlParameterSource[] batchParams = SqlParameterSourceUtils.createBatch(paramMapArray);

            //执行批量插入,如果失败调用失败处理函数.
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        jdbcTemplate.batchUpdate(getActualSql(), batchParams);
                        if (logger.isDebugEnabled()) {
                            for (LoggingEvent event : eventsBuffer) {
                                logger.debug("saved event: {}", new LoggingEventWrapper(event).convertToString());
                            }
                        }
                    } catch (DataAccessException e) {
                        status.setRollbackOnly();
                        handleDataAccessException(e, eventsBuffer);
                    }
                }
            });

            //清除已完成的Buffer
            eventsBuffer.clear();
        } catch (Exception e) {
            logger.error("批量提交任务时发生错误.", e);
        }
    }

    /**
     * 退出清理函数,完成buffer中未完成的消息.
     */
    @Override
    protected void clean() {
        if (!eventsBuffer.isEmpty()) {
            updateBatch();
        }
        logger.debug("cleaned task {}", this);
    }

    /**
     * 分析Event, 建立Parameter Map, 用于绑定sql中的Named Parameter.
     */
    protected Map<String, Object> parseEvent(LoggingEvent event) {
        Map<String, Object> parameterMap = Maps.newHashMap();
        LoggingEventWrapper eventWrapper = new LoggingEventWrapper(event);

        parameterMap.put("thread_name", eventWrapper.getThreadName());
        parameterMap.put("logger_name", eventWrapper.getLoggerName());
        parameterMap.put("log_time", eventWrapper.getDate());
        parameterMap.put("level", eventWrapper.getLevel());
        parameterMap.put("message", eventWrapper.getMessage());
        return parameterMap;
    }

    /**
     * 可被子类重载的数据访问错误处理函数,如将出错的事件持久化到文件.
     */
    protected void handleDataAccessException(DataAccessException e, List<LoggingEvent> errorEventBatch) {
        if (e instanceof DataAccessResourceFailureException) {
            logger.error("database connection error", e);
        } else {
            logger.error("other database error", e);
        }

        for (LoggingEvent event : errorEventBatch) {
            logger.error("event insert to database error, ignore it: "
                    + new LoggingEventWrapper(event).convertToString(), e);
        }
    }

    /**
     * 可被子类重载的sql提供函数,可对sql语句进行特殊处理，如日志表的表名可带日期后缀 LOG_2009_02_31.
     */
    protected String getActualSql() {
        return sql;
    }
}
