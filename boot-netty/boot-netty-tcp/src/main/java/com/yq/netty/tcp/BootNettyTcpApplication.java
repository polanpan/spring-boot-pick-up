package com.polan.netty.tcp;

import com.polan.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/4/22 13:14
 */
@SpringBootApplication
public class BootNettyTcpApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootNettyTcpApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
