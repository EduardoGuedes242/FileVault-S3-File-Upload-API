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

  @GetMapping("/download/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
    // Carregar o arquivo a partir do serviço de armazenamento
    Resource resource = storageRepository.loadAsResource(fileName);

    // Determina o tipo de arquivo (MIME type) dinamicamente
    Path filePath = resource.getFile().toPath();
    String contentType = Files.probeContentType(filePath);

    // Caso o tipo de conteúdo não seja identificado, define um tipo padrão (opcional)
    if (contentType == null) {
      contentType = "application/octet-stream";  // Tipo genérico para download
    }

    // Retorna o arquivo com o cabeçalho correto para download
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


}
