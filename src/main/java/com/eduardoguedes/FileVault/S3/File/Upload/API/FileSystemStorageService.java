package com.eduardoguedes.FileVault.S3.File.Upload.API;

import com.eduardoguedes.FileVault.S3.File.Upload.API.infra.exception.StorageException;
import com.eduardoguedes.FileVault.S3.File.Upload.API.infra.exception.StorageFileNotFoundException;
import com.eduardoguedes.FileVault.S3.File.Upload.API.infra.properties.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageRepository{

  private final Path rootLocation;

  @Autowired
  public FileSystemStorageService(StorageProperties properties) {
    if(properties.getLocation().trim().length() == 0) {
      throw new StorageException("File upload location can not be empty.");
    }
    this.rootLocation = Paths.get(properties.getLocation());
  }

  @Override
  public void store(String path, MultipartFile file) {
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file.");
      }

      Path destinationFile = this.rootLocation.resolve(Paths.get(path, file.getOriginalFilename()))
              .normalize().toAbsolutePath();

      if (!destinationFile.startsWith(this.rootLocation.toAbsolutePath())) {
        throw new StorageException("Cannot store file outside current directory");
      }

      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException exception) {
      throw new StorageException("Failed to store file", exception);
    }
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.rootLocation, 1)
              .filter(path -> !path.equals(this.rootLocation))
              .map(this.rootLocation::relativize);
    }
    catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }

  }


  @Override
  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      Path file = load(filename);

      Resource resource = new UrlResource(file.toUri());

      if(resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);
      }

    } catch (MalformedURLException exception) {
      throw new StorageFileNotFoundException("Could not read file: " + filename);
    }

  }

  @Override
  public void newDir(String dir) {
    Path path = Paths.get(this.rootLocation.toString() + "/" + dir);
    try {
      Files.createDirectories(path);
      System.out.println("Create new dir with success");
    } catch (IOException error) {
      System.out.println("Failed of create new dir: " + error.getMessage());
      throw new StorageException("Failed to read stored files", error);
    }

  }

}
