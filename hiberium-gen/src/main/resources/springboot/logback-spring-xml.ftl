<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property name="LOGS_FOLDER" value="./logs" />

    <appender name="Console" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>%d %highlight(%-5level) [%blue(%t)] %yellow(%-40.40logger{39}) : %msg%n%throwable</Pattern>
        </layout>
    </appender>

    <appender name="RollingFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${r"${LOGS_FOLDER}"}/${project_name}.log</file>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <Pattern>%d %-5level [%thread] %logger{39} : %m%n</Pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${r"${LOGS_FOLDER}"}/archived/${project_name}.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
    </appender>

    <root level="INFO">
        <appender-ref ref="RollingFile" />
        <appender-ref ref="Console" />
    </root>
</configuration>