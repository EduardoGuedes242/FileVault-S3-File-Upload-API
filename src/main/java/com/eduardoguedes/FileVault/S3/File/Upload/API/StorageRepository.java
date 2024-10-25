package com.eduardoguedes.FileVault.S3.File.Upload.API;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageRepository {

  void store(String path, MultipartFile file);

  default Stream<Path> loadAll() {
    return null;
  }

  Path load(String filename);

  Resource loadAsResource(String filename);

  void newDir(String dir);
}
