package com.eduardoguedes.FileVault.S3.File.Upload.API;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageRepository {

  //void init();

  void store(MultipartFile file);

  default Stream<Path> loadAll() {
    return null;
  }

  Path load(String filename);

  Resource loadAsResource(String filename);

  //void deleteAll();
}
