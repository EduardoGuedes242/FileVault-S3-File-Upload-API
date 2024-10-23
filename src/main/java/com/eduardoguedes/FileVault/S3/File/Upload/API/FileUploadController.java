package com.eduardoguedes.FileVault.S3.File.Upload.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {

  private final StorageRepository storageRepository;

  @Autowired
  public FileUploadController(StorageRepository storageRepository) {
    this.storageRepository = storageRepository;
  }

  @GetMapping("files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serverFile(@PathVariable String filename) {
    Resource file = storageRepository.loadAsResource(filename);

    if(file == null){
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @PostMapping("/")
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {

    storageRepository.store(file);
    redirectAttributes.addFlashAttribute("message",
            "You successfully uploaded " + file.getOriginalFilename() + "!");

    return "redirect:/";
  }


}
