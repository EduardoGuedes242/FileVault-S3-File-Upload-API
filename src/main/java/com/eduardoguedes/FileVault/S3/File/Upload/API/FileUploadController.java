package com.eduardoguedes.FileVault.S3.File.Upload.API;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(summary = "Listar todos os arquivos da pasta", method = "GET")
  @GetMapping("/list/{path}")
  public ResponseEntity<List<String>> listAvailableFiles(@PathVariable("path") String pathDir) {
      List<String> fileNames = storageRepository.loadAll(pathDir)
              .map(path -> path.getFileName().toString())
              .collect(Collectors.toList());

      return ResponseEntity.ok().body(fileNames);
  }

  @Operation(summary = "Download do arquivo", method = "GET")
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

  @Operation(summary = "Upload de arquivos", method = "POST")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "File Import")})
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> handleFileUpload(
          @Parameter(description = "Nome do diretório onde o arquivo será armazenado", required = true)
          @RequestParam("path") String path,

          @Parameter(description = "Arquivo a ser enviado, essa informação tem que ser passado como paratro igual o *PATH*", required = true)
          @RequestParam("file") MultipartFile file) {

    storageRepository.store(path, file);

    return new ResponseEntity<String>("File Import", HttpStatus.CREATED);
  }

  @Operation(summary = "Criando novo diretório", method = "POST")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "New dir create with success")})
  @PostMapping("/new-dir")
  public ResponseEntity<String> createNewDir(
          @Parameter(description = "Nome do diretório onde o arquivo será armazenado", required = true)
          @RequestParam("path") String nameDir) {
    storageRepository.newDir(nameDir);
    return new ResponseEntity<String>("New dir create with success", HttpStatus.CREATED);
  }

}
