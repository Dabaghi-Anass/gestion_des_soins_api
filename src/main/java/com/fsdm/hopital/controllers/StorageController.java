package com.fsdm.hopital.controllers;
import com.fsdm.hopital.auth.jwt.JwtUtils;
import com.fsdm.hopital.entities.Profile;
import com.fsdm.hopital.entities.ResourceFile;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.ResourceFileRepository;
import com.fsdm.hopital.services.ProfileService;
import com.fsdm.hopital.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;
    private final ProfileService profileService;
    private final JwtUtils jwtUtils;
    private final ResourceFileRepository resourceFileRepository;

    @PostMapping("/upload/{user_id}")
    @SneakyThrows
    public ResourceFile uploadFile(@RequestHeader("x-auth") String token,MultipartFile file, @PathVariable Long user_id){
        User creator = jwtUtils.extractUserFromJwt(token);
        String filePath = storageService.uploadFile(file);
        if(filePath == null) throw new AppException(ProcessingException.INVALID_OPERATON);
        else{
            long size = file.getSize();
            return storageService.saveFileMetaData(filePath,file.getOriginalFilename(), file.getContentType(),user_id,creator,size / 1024.0);
        }
    }

    @GetMapping("/download/{fileName}")
    @SneakyThrows
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName){
        ResourceFile resourceFile = resourceFileRepository.findByName(fileName);
        if(resourceFile == null) throw new AppException(ProcessingException.INVALID_OPERATON);
        Resource resource = storageService.downloadFile(resourceFile.getName());
        MediaType contentType = MediaType.parseMediaType(resourceFile.getContentType());
        if(contentType.equals(MediaType.IMAGE_PNG) || contentType.equals(MediaType.IMAGE_JPEG))
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(resource);
    }
    @GetMapping("/{fileName}")
    @SneakyThrows
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName){
        Resource resource = storageService.downloadFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    @PostMapping("/upload-image/{user_id}")
    @SneakyThrows
    public ResponseEntity<String> uploadImage(MultipartFile file, @PathVariable Long user_id){
        String imageUrl = storageService.uploadImage(file);
        if(imageUrl == null) throw new AppException(ProcessingException.INVALID_OPERATON);
        profileService.updateProfileImage(imageUrl,user_id);
        return ResponseEntity.ok(imageUrl);
    }

    @GetMapping("/user-files/all/{user_id}")
    public List<ResourceFile> listAllFiles(@PathVariable Long user_id){
        return resourceFileRepository.findAllByUserId(user_id);
    }
    @GetMapping("/user-files/{user_id}")
    public List<ResourceFile> listFiles(@PathVariable Long user_id){
        Pageable page = PageRequest.of(0,6);
        return resourceFileRepository.findAllByUserId(user_id,page);
    }
    @DeleteMapping("/delete-doc/{id}")
    public void deleteDocument(@PathVariable Long id){
        storageService.deleteDocumentById(id);
    }
}
