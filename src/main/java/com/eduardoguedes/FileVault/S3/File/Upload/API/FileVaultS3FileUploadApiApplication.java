package com.eduardoguedes.FileVault.S3.File.Upload.API;

import com.eduardoguedes.FileVault.S3.File.Upload.API.infra.properties.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class FileVaultS3FileUploadApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileVaultS3FileUploadApiApplication.class, args);
	}

}
