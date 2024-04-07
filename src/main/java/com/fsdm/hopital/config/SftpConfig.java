package com.fsdm.hopital.config;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j
public class SftpConfig {
//    @Value("${sftp.host}")
//    private String host;
//    @Value("${sftp.port:22}")
//    private int port;
//    @Value("${sftp.username}")
//    private String user;
//    @Value("${sftp.password}")
//    private String password;
//    @Bean
//    DefaultFtpSessionFactory defaultFtpSessionFactory() {
//        DefaultFtpSessionFactory defaultFtpSessionFactory = new DefaultFtpSessionFactory();
//        defaultFtpSessionFactory.setPassword(password);
//        defaultFtpSessionFactory.setUsername(user);
//        defaultFtpSessionFactory.setHost(host);
//        defaultFtpSessionFactory.setPort(port);
//        return defaultFtpSessionFactory;
//    }
//
//    @Bean
//    FtpRemoteFileTemplate ftpRemoteFileTemplate(DefaultFtpSessionFactory dsf) {
//        return new FtpRemoteFileTemplate(dsf);
//    }
}
