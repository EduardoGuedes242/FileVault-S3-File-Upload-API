package com.eduardoguedes.FileVault.S3.File.Upload.API;

import com.eduardoguedes.FileVault.S3.File.Upload.API.infra.properties.StorageProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@OpenAPIDefinition(info = @Info(title = "Upload e Download de Arquivos - EC2 AWS", version = "1", description = "Esta é uma aplicação desenvolvida em **Java** com **Spring Boot**, utilizando a infraestrutura de **EC2 da AWS**. A aplicação permite o **upload e download** de arquivos de forma simples."))
public class FileVaultS3FileUploadApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileVaultS3FileUploadApiApplication.class, args);
	}

}
