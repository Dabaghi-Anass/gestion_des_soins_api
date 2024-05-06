package com.fsdm.hopital.controllers;
import com.fsdm.hopital.entities.ResourceFile;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.ResourceFileRepository;
import com.fsdm.hopital.services.StorageService;
import com.fsdm.hopital.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;
    private final ResourceFileRepository resourceFileRepository;

    @PostMapping("/upload/{user_id}")
    @SneakyThrows
    public ResourceFile uploadFile(MultipartFile file, @PathVariable Long user_id){
        var userDir = "user_dir_"+user_id+"/";
        String filePath = storageService.uploadFile(file,userDir);
        if(filePath == null) throw new AppException(ProcessingException.INVALID_OPERATON);
        else return storageService.saveFileMetaData(filePath, file.getContentType(),user_id);
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
}
