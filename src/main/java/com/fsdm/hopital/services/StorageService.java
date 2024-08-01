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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.net.ftp.FTP;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final HttpServletRequest request;
    private final ResourceFileRepository resourceFileRepository;
    private final FTPClient ftpClient;
    private final UserService userService;
    @Value("${server.port:8070}")
    private String serverPort;
    public String uploadFile(MultipartFile file){
        try{
            var fileUniqueName = System.currentTimeMillis() +file.getOriginalFilename();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            boolean status = ftpClient.appendFile(fileUniqueName, file.getInputStream());
            if(!status) throw new AppException(ProcessingException.INVALID_OPERATON);
            return fileUniqueName;
        }catch (Exception e){
           return null;
        }
    }
    public String uploadImage(MultipartFile file){
        try{
            InputStream scaledInputStream = scaleImage(file.getInputStream(), 200, 200);
            var fileUniqueName = System.currentTimeMillis() +file.getOriginalFilename();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            boolean status = ftpClient.appendFile(fileUniqueName, scaledInputStream);
            if(!status) throw new AppException(ProcessingException.INVALID_OPERATON);
            return getImageDownloadLink(fileUniqueName);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public InputStream scaleImage(InputStream inputStream, int width, int height) throws Exception {
        BufferedImage originalImage = ImageIO.read(inputStream);
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage scaledBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledBufferedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        // Convert the scaled image back to InputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(scaledBufferedImage, "jpg", os);
        os.flush();
        return new ByteArrayInputStream(os.toByteArray());
    }

    @SneakyThrows
    public Resource downloadFile(String fileName){
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        File tempFile = File.createTempFile("temp" + UUID.randomUUID(), null);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            ftpClient.retrieveFile(fileName, fos);
        }
        byte[] fileBytes = Files.readAllBytes(tempFile.toPath());
        Resource resource = new ByteArrayResource(fileBytes);
        tempFile.delete();
        return resource;
    }

    public ResourceFile saveFileMetaData(String fileName,String originalName, String contentType,Long ownerId,User creator,double size) {
        User owner = userService.getUserById(ownerId);
        ResourceFile resourceFile = new ResourceFile();
        resourceFile.setName(fileName);
        resourceFile.setOriginalName(originalName);
        resourceFile.setContentType(contentType);
        resourceFile.setUrl(getMediaDownloadLink(fileName));
        resourceFile.setOwner(owner);
        resourceFile.setCreator(creator);
        resourceFile.setSize(size);
        return resourceFileRepository.save(resourceFile);
    }
    public String getServerLink(){
        try {
            if(!serverPort.isBlank() && !serverPort.equals("80") && !serverPort.equals("443") && !serverPort.equals("8080")){
                return String.format("%s://%s:%s/", request.getScheme(), request.getServerName(),request.getServerPort());
            }
            return String.format("%s://%s/", request.getScheme(), request.getServerName());
        } catch (Exception e) {
            return "http://localhost:8080/";
        }
    }
    public String getMediaDownloadLink(String fileName) {
        return "/media/download/" + fileName;
    }
    public String getImageDownloadLink(String fileName) {
        return "/media/" + fileName;
    }

    @SneakyThrows
    public void deleteDocumentById(Long id) {
        ResourceFile resourceFile = resourceFileRepository.findById(id)
                .orElseThrow(() -> new AppException(ProcessingException.INVALID_OPERATON));
        try {
            ftpClient.deleteFile(resourceFile.getName());
            resourceFileRepository.delete(resourceFile);
        } catch (IOException e) {
            throw new AppException(ProcessingException.INVALID_OPERATON);
        }
    }
}
