package com.zwallet.zwalletapi.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.zwallet.zwalletapi.Config.Encryptor;
import com.zwallet.zwalletapi.Model.Dto.FileDto;
import com.zwallet.zwalletapi.Model.Entity.FileEntity;
import com.zwallet.zwalletapi.Repository.FileRepository;
import com.zwallet.zwalletapi.Service.FileServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/files")
@CrossOrigin(value = "*")
public class FileController {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

  @Autowired
  private FileServiceImpl fileServiceImpl;

  @Autowired
  private FileRepository fileRepository;

  @Autowired
  private Encryptor enc;

  @PostMapping("/upload/{id}")
  public FileDto uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String id) {
    Integer openId = enc.decryptString(id);
    String filename = fileServiceImpl.storeFile(file, openId);

    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/download/")
        .path(filename).toUriString();

    return new FileDto(filename, file.getContentType(), fileDownloadUri, file.getSize());
  }

  // @PostMapping("/uploads")
  // public List<FileDto> uploadFiles(@RequestParam("files") MultipartFile[]
  // files) {
  // List<FileDto> responseFiles = Arrays.asList(files).stream().map(file ->
  // uploadFile(file))
  // .collect(Collectors.toList());

  // return responseFiles;
  // }

  @GetMapping("/download/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable String filename, HttpServletRequest request) {
    Resource resource = fileServiceImpl.loadFileAsResource(filename);

    String contentType = null;
    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    } catch (Exception e) {
      // TODO: handle exception
      LOGGER.info("Could not determine file type.");
    }

    if (contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }

  @GetMapping
  public ResponseEntity<?> getList() {
    List<FileEntity> files = fileRepository.findAll();

    return ResponseEntity.ok().body(files);
  }
}