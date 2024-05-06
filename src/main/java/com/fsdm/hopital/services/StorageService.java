package com.fsdm.hopital.services;
import com.fsdm.hopital.entities.ResourceFile;
import com.fsdm.hopital.repositories.ResourceFileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.net.ftp.FTP;

import java.io.FileOutputStream;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final ResourceFileRepository resourceFileRepository;
    private final FTPClient ftpClient;
    @Value("${sftp.remoteFilesDirectory}")
    private String remoteDirectory;

    public String uploadFile(MultipartFile file){
        try{
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.appendFile(remoteDirectory+file.getOriginalFilename(), file.getInputStream());
            ResourceFile resourceFile = new ResourceFile();
            resourceFile.setName(file.getOriginalFilename());
            resourceFile.setUrl("/download/"+remoteDirectory+file.getOriginalFilename());
            resourceFile.setType(file.getContentType());
            resourceFileRepository.save(resourceFile);
            return resourceFile.getUrl();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "File upload failed";
    }
    public Resource downloadFile(String fileName){
        try{
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.retrieveFile(remoteDirectory+fileName, new FileOutputStream(fileName));
            return new FileSystemResource(fileName);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
