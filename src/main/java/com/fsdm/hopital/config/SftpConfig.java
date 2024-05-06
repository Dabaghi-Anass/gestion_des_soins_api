package com.fsdm.hopital.config;

import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@Log4j
public class SftpConfig {
    @Value("${sftp.host}")
    private String host;
    @Value("${sftp.port:22}")
    private int port;
    @Value("${sftp.username}")
    private String user;
    @Value("${sftp.password}")
    private String password;
    @Value("${sftp.remoteFilesDirectory}")
    private String remoteFilesDirectory;
    @Value("${sftp.remoteImagesDirectory}")
    private String remoteImagesDirectory;

    @Bean
    @SneakyThrows
    public FTPClient ftpClient() {
        FTPClient ftpClient = new FTPClient();
        //for logging
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        try {
            ftpClient.connect(host, port);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new IOException("Exception in connecting to FTP Server");
            }
        } catch (IOException e) {
            throw new AppException(ProcessingException.INTERNAL_ERROR);
        }

        ftpClient.login(user, password);
        return ftpClient;
    }
}
