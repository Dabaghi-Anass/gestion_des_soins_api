package com.fsdm.hopital.services;
import com.fsdm.hopital.entities.ResourceFile;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.ResourceFileRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.net.ftp.FTP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final HttpServletRequest request;
    private final ResourceFileRepository resourceFileRepository;
    private final FTPClient ftpClient;
    private final UserService userService;
    @Value("${sftp.remoteFilesDirectory}")
    private String remoteDirectory;
    @Value("${server.port:8070}")
    private String serverPort;
    public static boolean isDirectoryExists(FTPClient ftpClient, String directoryPath) {
        boolean isExist = false;
        try{
            if (ftpClient.changeWorkingDirectory(directoryPath)) {
                int returnCode = ftpClient.getReplyCode();
                if (FTPReply.isPositiveCompletion(returnCode)) {
                    isExist = true;
                }
            }
        } catch (IOException e){
            isExist = false;
        }

        return isExist;
        //hello world
    }
    public String uploadFile(MultipartFile file, String userDirectory){
        try{
            var fileUniqueName = System.currentTimeMillis() +file.getOriginalFilename();
            var remoteFileName= fileUniqueName;
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            boolean status = ftpClient.appendFile(remoteFileName, file.getInputStream());
            if(!status) throw new AppException(ProcessingException.INVALID_OPERATON);
            return fileUniqueName;
        }catch (Exception e){
           return null;
        }
    }
    @SneakyThrows
    public Resource downloadFile(String fileName){
        try{
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.retrieveFile(fileName, new FileOutputStream(fileName));
            return new FileSystemResource(fileName);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResourceFile saveFileMetaData(String fileName, String contentType,Long ownerId) {
        User owner = userService.getUserById(ownerId);
        ResourceFile resourceFile = new ResourceFile();
        resourceFile.setName(fileName);
        resourceFile.setContentType(contentType);
        resourceFile.setUrl(getMediaDownloadLink(fileName));
        resourceFile.setOwner(owner);
        return resourceFileRepository.save(resourceFile);
    }
    public String getServerLink(){
        try {
            if(serverPort != null && !serverPort.isBlank()){
                return String.format("%s://%s:%s/", request.getScheme(), request.getServerName(),request.getServerPort());
            }
            return String.format("%s://%s/", request.getScheme(), request.getServerName());
        } catch (Exception e) {
            return "http://localhost:8080/";
        }
    }
    public String getMediaDownloadLink(String fileName) {
        return getServerLink() + "media/download/" + fileName;
    }
}
