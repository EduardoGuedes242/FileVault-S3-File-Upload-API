package com.eduardoguedes.FileVault.S3.File.Upload.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
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

  @GetMapping("/download/{dir}/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(
           @PathVariable("dir") String dir
          ,@PathVariable("fileName") String fileName) throws IOException {
    Resource resource = storageRepository.loadAsResource(dir + "/" + fileName);

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

  @GetMapping("/download/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable() String fileName) throws IOException {
    Resource resource = storageRepository.loadAsResource(fileName);

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
  public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {

    storageRepository.store(file);

    return new ResponseEntity<String>("File Import", HttpStatus.CREATED);
  }

  @PostMapping("/new-dir")
  public ResponseEntity<String> createNewDir(@RequestParam("name") String nameDir) {
    storageRepository.newDir(nameDir);
    return new ResponseEntity<String>("new dir create with success", HttpStatus.CREATED);
  }

}
