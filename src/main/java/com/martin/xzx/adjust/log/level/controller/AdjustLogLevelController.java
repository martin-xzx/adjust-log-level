package com.martin.xzx.adjust.log.level.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 日志级别动态调整
 *
 * @author: martin
 * @date: 2021/11/30
 */
@Controller
@RequestMapping("/adjust/log")
public class AdjustLogLevelController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdjustLogLevelController.class);
    /**
     * 可以修改的日志级别的范围
     **/
    private static final Set<String> ALL_LOG_LEVELS = new HashSet<>();

    static {
        ALL_LOG_LEVELS.add("TRACE");
        ALL_LOG_LEVELS.add("DEBUG");
        ALL_LOG_LEVELS.add("INFO");
        ALL_LOG_LEVELS.add("WARN");
        ALL_LOG_LEVELS.add("ERROR");
    }

    @RequestMapping("level")
    public void adjustLog(String level) {
        if (!ALL_LOG_LEVELS.contains(level)) {
            LOGGER.error("only accept error/info/debug log level,please check your log level:{}", level);
            throw new IllegalArgumentException("adjust log params is error");
        }

        modifyLogbackLevel(level);
        LOGGER.error("Error Log");
        LOGGER.warn("Warn Log");
        LOGGER.info("Info Log");
        LOGGER.debug("DEBUG Log");
        LOGGER.trace("TRACE Log");
    }

    private void modifyLogbackLevel(String level) {
        Level targetLevel = Level.toLevel(level);
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
        for (ch.qos.logback.classic.Logger logger : loggerList) {
            if (logger.getLevel() != null) {
                logger.setLevel(targetLevel);
            }
        }

        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(targetLevel);
    }
}
