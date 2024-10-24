package com.eduardoguedes.FileVault.S3.File.Upload.API;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class FileVaultS3FileUploadApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileVaultS3FileUploadApiApplication.class, args);
	}

}
