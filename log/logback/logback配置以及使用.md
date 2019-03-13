logback 使用
====
一、logback.xml 配置
----
0. 将logback.xml文件放到resource目录下
1. 标准输出配置：
    ```
    <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>[%d %-5level %t %logger{1}] %msg%n</pattern>
        </encoder>
    </appender>
    ```
2. 系统日志配置
    ```
    <!-- System log -->
    <appender name="file.log.sys"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <append>true</append>
        <file>./logs/ing/sys.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>./logs/sys/%d{yyyyMMdd}.sys.log
            </fileNamePattern>
        </rollingPolicy>
        <encoder>
            <pattern>[%d %-5level %t %logger{1}] %msg%n</pattern>
        </encoder>
    </appender>
     ```   
3. 自定义类型日志输出
    ``` 
    <appender name="file.log.sql"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <append>true</append>
        <file>./logs/ing/sql.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>./logs/sys/%d{yyyyMMdd}.sql.log
            </fileNamePattern>
        </rollingPolicy>
        <encoder>
            <pattern>[%d %-5level %t %logger{1}] %msg%n</pattern>
        </encoder>
    </appender>
    ```
4. 配置默认输出以及级别
    ``` 
    <root>
        <level value="INFO"/>
        <appender-ref ref="file.log.sys"/>
        <appender-ref ref="stdout"/>
    </root>
    ```
5. 配置自定义输出以及级别
    ``` 
    <logger name="slowSql" level="INfo" additivity="false">
        <appender-ref ref="file.log.sql" />
    </logger>
    ```
* 整体配置文件如下：
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <configuration>
    
        <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
                <pattern>[%d %-5level %t %logger{1}] %msg%n</pattern>
            </encoder>
        </appender>
    
        <!-- System log -->
        <appender name="file.log.sys"
                  class="ch.qos.logback.core.rolling.RollingFileAppender">
            <append>true</append>
            <file>./logs/ing/sys.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>./logs/sys/%d{yyyyMMdd}.sys.log
                </fileNamePattern>
            </rollingPolicy>
            <encoder>
                <pattern>[%d %-5level %t %logger{1}] %msg%n</pattern>
            </encoder>
        </appender>
    
        <appender name="file.log.sql"
                  class="ch.qos.logback.core.rolling.RollingFileAppender">
            <append>true</append>
            <file>./logs/ing/sql.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>./logs/sys/%d{yyyyMMdd}.sql.log
                </fileNamePattern>
            </rollingPolicy>
            <encoder>
                <pattern>[%d %-5level %t %logger{1}] %msg%n</pattern>
            </encoder>
        </appender>
    
        <root>
            <level value="INFO"/>
            <appender-ref ref="file.log.sys"/>
            <appender-ref ref="stdout"/>
        </root>
        <logger name="slowSql" level="INfo" additivity="false">
            <appender-ref ref="file.log.sql" />
        </logger>
    </configuration> 
    ```
二、springboot 整合lombok 使用 logback
----
1. 使用默认日志输出
    ``` 
    @Service
    @Slf4j
    public class AreaCodeService {
        /**
         * <p>使用日志方法</p> 
         */
        public List<SelectOptionModel> method() {
            log.info("输出日志，时间为：{}", LocalDateTime.now());
        }
    }

    ```
2. 使用自定义日志输出
    ``` 
    @Service
    @Slf4j(topic="slowSql")
    public class AreaCodeService {
        /**
         * <p>使用日志方法</p> 
         */
        public List<SelectOptionModel> method() {
            log.info("输出日志，时间为：{}", LocalDateTime.now());
        }
    }

    ```