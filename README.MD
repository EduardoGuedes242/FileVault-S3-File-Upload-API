
# Upload e Download de Arquivos - EC2 AWS

Esta é uma aplicação desenvolvida em **Java** com **Spring Boot**, utilizando a infraestrutura de **EC2 da AWS**. A aplicação permite o **upload** e **download** de arquivos de forma simples.

## **Tecnologias Utilizadas**
- **Java**
- **Spring Boot**
- **AWS EC2**

---

## Instalação

A aplicação está hospedada em um servidor **EC2 na AWS**. Portanto, **não é necessário realizar nenhuma instalação local** para utilizar o sistema.

---

## Como Usar

### Passo 1: Criar Diretório

O primeiro passo é criar um diretório personalizado para armazenar seus arquivos. Este diretório será usado tanto para uploads quanto para downloads.

**Requisição:**
```
POST http://localhost:8080/files/new-dir
```

**Cabeçalho:**
```
"path": "nome_da_sua_pasta"
```

Se a requisição for bem-sucedida, você receberá um **STATUS_CODE 201**, indicando que a pasta foi criada com sucesso.

---

### Passo 2: Upload de Arquivo

Para enviar um arquivo ao servidor, utilize a seguinte requisição:

**Requisição:**
```
POST http://localhost:8080/files/upload
```

**Cabeçalho:**
```
"path": "nome_da_sua_pasta"
"file": "seu_arquivo"
```

Se o upload for bem-sucedido, você receberá um **STATUS_CODE 201**, indicando que o arquivo foi enviado corretamente.

---

### Passo 3: Listar Arquivos

Você pode listar todos os arquivos dentro do seu diretório com a seguinte requisição:

**Requisição:**
```
GET http://localhost:8080/files/list/nome_da_sua_pasta
```

**Resposta (STATUS_CODE 200):**
```
[
 "imagem.png",
 "arquivo.txt",
 "instalacao.zip"
]
```

---

### Passo 4: Baixar Arquivo

Para fazer o download de um arquivo específico, utilize a seguinte requisição:

**Requisição:**
```
GET http://localhost:8080/files/download/nome_da_sua_pasta/seu_arquivo.extensao
```

Exemplo:
```
GET http://localhost:8080/files/download/guedes/imagem.png
```

Se o download for bem-sucedido, o arquivo será baixado diretamente.
