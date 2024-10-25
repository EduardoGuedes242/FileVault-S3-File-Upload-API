package com.eduardoguedes.FileVault.S3.File.Upload.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("files")
public class FileUploadController {

  private final StorageRepository storageRepository;

  @Autowired
  public FileUploadController(StorageRepository storageRepository) {
    this.storageRepository = storageRepository;
  }

  @GetMapping("/list")
  public ResponseEntity<List<String>> listAvailableFiles() {
      List<String> fileNames = storageRepository.loadAll()
              .map(path -> path.getFileName().toString())
              .collect(Collectors.toList());

      return ResponseEntity.ok().body(fileNames);
  }

  @GetMapping("/download/{path}/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(
           @PathVariable("path") String path,
           @PathVariable("fileName") String fileName) throws IOException {
    Resource resource = storageRepository.loadAsResource(path + "/" + fileName);

    Path filePath = resource.getFile().toPath();
    String contentType = Files.probeContentType(filePath);

    if (contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .contentType(MediaType.parseMediaType(contentType))
            .body(resource);
  }

  @PostMapping("/upload")
  public ResponseEntity<String> handleFileUpload(
           @RequestParam("path") String path,
           @RequestParam("file") MultipartFile file) {

    storageRepository.store(path, file);

    return new ResponseEntity<String>("File Import", HttpStatus.CREATED);
  }

  @PostMapping("/new-dir")
  public ResponseEntity<String> createNewDir(@RequestParam("path") String nameDir) {
    storageRepository.newDir(nameDir);
    return new ResponseEntity<String>("new dir create with success", HttpStatus.CREATED);
  }

}
