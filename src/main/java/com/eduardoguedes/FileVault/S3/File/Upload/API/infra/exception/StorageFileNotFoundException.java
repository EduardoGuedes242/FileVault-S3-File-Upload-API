package com.eduardoguedes.FileVault.S3.File.Upload.API.infra.exception;

public class StorageFileNotFoundException extends StorageException {

  public StorageFileNotFoundException(String message) {
    super(message);
  }

  public StorageFileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}